//
//  TaskManager.m
//  libs
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMJobManager.h"
#import "DMJobQueue.h"
#import "DMHttpJob.h"
#import "DMNetHandler.h"
#import "DMHttpJob.h"
#import "DMGroupHandler.h"
#import "DMJobTypes.h"
#import "ServerManager.h"
#import "ImageParser.h"
#import "ImageSuccess.h"
#import "ImageErrors.h"
#import "DMCacheHandler.h"
#import "ArrayContainer.h"
#import "DMDiskCache.h"
#import "WeakSet.h"
#import "DMApiHandler.h"
#import "JsonParser.h"
#import "TextParser.h"
#import "CommonUtil.h"
#import "DMApiHandler.h"
#import "DMServerHandler.h"
#import "DMApiArrayParser.h"
#import "ApiPageParser.h"
#import "ApiDataParser.h"
#import "ApiObjectParser.h"
#import "ApiNotifier.h"
#import "DMNotifier.h"
#import "DMServers.h"
#import "DataSetterUtil.h"
#import "Alert.h"
#import "JavaServerHandler.h"
#import "PhpServerHandler.h"
#import "DMColorUtil.h"
#import "DMColorConfig.h"
#import "DMServerHandler.h"
#import <objc/runtime.h>
#import "DMExceptionHandler.h"
#import "RawParser.h"
#import "DMAccount.h"
#import "DMConfigReader.h"
#import <AdSupport/AdSupport.h>
#import "PushUtil.h"

__weak NSArray* g_servers;
__weak NSArray<DMServerHandler*>* g_handers;
__weak NSMutableDictionary* g_config;
__weak UIColor* g_itemColor;
__weak UIColor* g_tintColor;
__weak NSDictionary* g_colorConfig;



char* UIImageViewKey = "UIImageView";


@implementation UIImageView(DMHttpJob)

- (DMHttpJob *)job {
    return (DMHttpJob*)objc_getAssociatedObject(self, UIImageViewKey);
}

- (void)setJob:(DMHttpJob *)job {
    objc_setAssociatedObject(self, UIImageViewKey, job, OBJC_ASSOCIATION_RETAIN);
}

@end


@implementation DMServers
+(NSString*)formatUrl:(NSInteger)server url:(NSString*)url{
    return [NSString stringWithFormat:@"%@%@",g_servers[server],url];
}
+(NSInteger)startPosition:(NSInteger)server{
    return g_handers[server].startPosition;
}
+(NSString*)getUrl:(NSInteger)index{
    return g_servers[index];
}
@end

__weak DMJobManager* _manager;
@implementation DMColorConfig
+(UIColor*)itemHighlightColor{
    return g_itemColor;
}
+(UIColor*)tintColor{
    return g_tintColor;
}
+(UIColor*)item:(NSString*)key{
    return g_colorConfig[key];
}
@end

@interface DMJobManager ()
{
    
    ImageErrors* _imageErrors;
    //图片\网络下载任务
    DMJobQueue* _networkQueue;
    NSMutableArray<DMNetwork*>* _networkArray;
    id<DMCache> _cache;
    ArrayContainer* _parsers;
    NSMutableArray<DMGroupHandler*>* _groupHandlers;
    
    DMCacheHandler* _cacheHandler;
    DMJobQueue* _cacheQueue;
    
    NSString* _deviceID;
    
    ServerStatusMoniter* _moniter;
    DMApiHandler* _apiHandler;
    ArrayContainer* _successListener;
    NSMutableSet* _paddingTasks;
    
    DMJobQueue* _apiQueue;
    DMNotifier* _notifier;
    ArrayContainer* _apiParsers;
    NSMutableArray* _servers;
    ApiNotifier* _apiNotifier;
    //一些强引用
    NSMutableArray* _strongRef;
    
    //微信id
    NSString* _wxId;
    
}

@end

@implementation DMJobManager

+(DMJobManager*)sharedInstance{
    return _manager;
}
-(NSString*)wxId{
    return _wxId;
}
-(DMViewController*)createPayCashier:(Class)cashierControllerClass{
    return [_factory createPayCashier:cashierControllerClass];
}
-(void)loadConfig{
   
    //解析颜色配置
    NSDictionary* colors = [DMConfigReader getMap:@"colors"];
    NSMutableDictionary* colorConfig = [[NSMutableDictionary alloc]init];
    for (NSString* colorName in colors) {
        UIColor* color=[DMColorUtil colorWithString:colors[colorName]];
        colorConfig[colorName] = color;
    }
    g_itemColor = colorConfig[@"itemHighlight"];
    g_tintColor = colorConfig[@"tint"];
    g_colorConfig = colorConfig;
    [_strongRef addObject:colorConfig];
    
    
    //解析微信支付配置
    NSDictionary* info = [[NSBundle mainBundle]infoDictionary];
    //解析支付宝配置
    NSArray* urlTypes = info[@"CFBundleURLTypes"];
    for (NSDictionary* dic in urlTypes) {
        NSString* name = dic[@"CFBundleURLName"];
        if([name isEqualToString:@"weixin"]){
            //微信id
            NSArray* wxarray = dic[@"CFBundleURLSchemes"];
            _wxId = wxarray[0];
            break;
        }
    }
   
    
    Class clazz = NSClassFromString([DMConfigReader getString:@"userClass"]);
    
    [DMAccount setAccountClass:clazz];
    [DMAccount loadAccount];
    [DMAccount setLoginCaller:self];
    
}

-(void)callLoginController:(id<DMLoginDelegate>)delegate{
    [_loginCaller callLoginController:delegate];
}
-(NSString*)pushID{
    //这里推送id
    return [PushUtil getPushId];
}

-(void)clearCache:(DMApiJob*)task{
    [_cache clearCache:task.makeDataKey];
}



-(DMPayModel*)createPayModel:(NSString*)moduleName supportTypes:(NSArray<NSNumber*>*)supportTypes{
    DMPayModel* model = [[DMPayModel alloc]init];
    model.module = moduleName;
    model.factory = _factory;
    
    if(supportTypes){
        NSMutableArray* arr = [[NSMutableArray alloc]initWithCapacity:supportTypes.count];
        //如果有supportTypes
        for(NSNumber* type in supportTypes){
            
            id pay = [_factory createPay: [type integerValue]];
            [arr addObject:pay];
            
            
        }
        [model setPayType:arr];
    }
    
    return model;
}



-(id)initWithRegisterServerHandler:(id<DMServerRegisterDelegate>)delegate{
    if(self = [super init]){
        //   InstallUncaughtExceptionHandler();
        
        _manager = self;
        
        //所有弱引用必须用这个强引用列表来引用
        _strongRef = [[NSMutableArray alloc]init];
        [_strongRef addObject:[DataSetterUtil initData]];
        [self loadConfig];
        
        
        
        _servers = [[NSMutableArray alloc]init];
        g_servers = _servers;
        _notifier = [[DMNotifier alloc]init];
        _apiParsers = [[ArrayContainer alloc]init];
        _apiHandler = [[DMApiHandler alloc]init];
        _apiQueue  = [[DMJobQueue alloc]init];
        _cache = [[DMDiskCache alloc]init];
        _parsers = [[ArrayContainer alloc]init];
        _networkQueue = [[DMJobQueue alloc]init];
        _imageErrors = [[ImageErrors alloc]init];
        _networkArray = [[NSMutableArray alloc]init];
        _groupHandlers = [[NSMutableArray alloc]init];
        _cacheQueue = [[DMJobQueue alloc]init];
        _moniter = [[ServerStatusMoniter alloc]init];
        
        _successListener = [[ArrayContainer alloc]init];
        
        _cacheHandler = [[DMCacheHandler alloc]initWithNetQueue:_networkQueue apiQueue:_apiQueue cache:_cache parsers:_parsers.array moniter:_moniter successListeners:_successListener.array];
        _paddingTasks = [[NSMutableSet alloc]init];
        
        
        [_apiQueue setTaskHandlers:[NSArray arrayWithObject:_apiHandler]];
        [_apiQueue start];
        
        
        DMNetwork* network = [[DMNetwork alloc]init];
        [_networkArray addObject:network];
        _apiNotifier =[[ApiNotifier alloc]initWithTaskManager:self];
        
     
        if(delegate!=nil){
             NSDictionary* servers = [DMConfigReader getMap: @"servers"];
            NSArray* _serverConfigArray = [delegate registerServerHandler:_apiHandler parsers:_apiParsers.array delegate:_apiNotifier cache:_cache];
            NSInteger index = 0;
            for (NSString* server in _serverConfigArray) {
                [self registerServer:index++ url:servers[server]];
            }
        }else{
            PhpServerHandler* phpServerHandler = [[PhpServerHandler alloc]init];
            [phpServerHandler initParam:_apiParsers.array delegate:_apiNotifier cache:_cache];
            [_apiHandler registerServerHandler:0 handler:phpServerHandler];
            
            
            JavaServerHandler* javaServerHandler = [[JavaServerHandler alloc]init];
            [javaServerHandler initParam:_apiParsers.array delegate:_apiNotifier cache:_cache];
            [_apiHandler registerServerHandler:1 handler:javaServerHandler];
            
            NSDictionary* servers = [DMConfigReader getMap: @"servers"];
            NSArray* _serverConfigArray = @[servers[@"php"],servers[@"java"]];
            NSInteger index = 0;
            for (NSString* server in _serverConfigArray) {
                [self registerServer:index++ url:server];
            }
        }
        
        
        g_handers = _apiHandler.handlers;
        
        [_cacheQueue setTaskHandlers:[NSArray arrayWithObject:_cacheHandler]];
        [_cacheQueue start];
        
        for(int i=0; i < 4; ++i){
            network = [[DMNetwork alloc]init];
            [_networkArray addObject:network];
            
            DMGroupHandler* groupHandler = [[DMGroupHandler alloc]init];
            [_groupHandlers addObject:groupHandler];
            
            
            //第一个
            DMNetHandler* netHandler = [[DMNetHandler alloc]initWithNetwork:_networkArray[i] parsers:_parsers.array cache:_cache successListeners:_successListener.array errorDelegate:self];
            [groupHandler registerHandler:JobType_NormalHttp handler:netHandler];
            
            
            netHandler = [[DMNetHandler alloc]initWithNetwork:_networkArray[i] parsers:_parsers.array cache:_cache successListeners:_successListener.array errorDelegate:self];
            [groupHandler registerHandler:JobType_Image handler:netHandler];
            
        }
        
        
        [self registerSuccessListener:self forType:JobType_NormalHttp];
        [self registerSuccessListener:[[ImageSuccess alloc]initWithDelegate:self] forType:JobType_Image];
        [self registerSuccessListener:_apiNotifier forType:JobType_Api];
        
        [_apiParsers registerObject:DataType_ApiArray object:[[DMApiArrayParser alloc]init]];
        [_apiParsers registerObject:DataType_ApiObject object:[[ApiObjectParser alloc]init]];
        [_apiParsers registerObject:DataType_ApiPage object:[[ApiPageParser alloc]init]];
        
        [self registerParser:[[ImageParser alloc]initWithLock:self error:_imageErrors] forType:DataType_Image];
        [self registerParser:[[JsonParser alloc]init] forType:DataType_Json];
         [self registerParser:[[RawParser alloc]init] forType:DataType_Raw];
        [self registerParser:[[TextParser alloc]init] forType:DataType_Text];
        
        [self registerParser:[[ApiDataParser alloc]initWithParser:[_apiParsers objectAt:DataType_ApiArray]] forType:DataType_ApiArray];
        [self registerParser:[[ApiDataParser alloc]initWithParser:[_apiParsers objectAt:DataType_ApiObject]] forType:DataType_ApiObject];
        [self registerParser:[[ApiDataParser alloc]initWithParser:[_apiParsers objectAt:DataType_ApiPage]] forType:DataType_ApiPage];
        
        
        
        [_networkQueue setTaskHandlers:_groupHandlers];
        [_networkQueue start];
       
        
    }
    return self;
}


-(void)dealloc{
    
    [_cacheQueue stop];
    [_apiQueue stop];
    [_networkQueue stop];
    
    
    _wxId = nil;
    _strongRef = nil;
    _servers = nil;
    _notifier = nil;
    _apiParsers = nil;
    _imageErrors = nil;
    _networkQueue = nil;
    _networkArray = nil;
    _cache = nil;
    _parsers = nil;
    _groupHandlers = nil;
    _cacheHandler = nil;
    _cacheQueue = nil;
    _deviceID = nil;
    _moniter = nil;
    _apiHandler = nil;
    _successListener = nil;
    _paddingTasks = nil;
    _apiQueue = nil;
}


-(void)clearCache{
    [_cache clear];
}

-(void)registerServer:(NSInteger)index url:(NSString*)url{
    if(index==0){
        [_moniter setUrl:url];
    }
    
    [_apiHandler setServerUrl:url index:index];
    [_servers addObject:url];
    
}

-(void)addCacheTask:(DMHttpJob*)job{
    job.isRunning = YES;
    NSMutableSet* set = self.topController.taskSet;
    @synchronized(set) {
        [set addObject:job];
    }
    [_cacheQueue addTask:job];
}
-(void)addApiTask:(DMApiJob*)job{
    job.isRunning = YES;
    NSMutableSet* set = self.topController.taskSet;
    @synchronized(set) {
        [set addObject:job];
    }
    [_apiQueue addTask:job];
}
-(void)executeApi:(DMApiJob *)job{
    
    if(job.waitingMessage){
        [Alert wait:job.waitingMessage];
    }
    
    if(job.cachePolicy != DMCachePolicy_NoCache){
        //放入net
        [self addCacheTask:job];
    }else{
        //进入api
        [self addApiTask:job];
    }
}


-(void)addNetTask:(DMApiJob*)job{
    job.isRunning = YES;
    NSMutableSet* set = self.topController.taskSet;
    @synchronized(set) {
        [set addObject:job];
    }
    [_networkQueue addTask:job];
}
-(void)onDestroy:(DMViewController*)controller{
    NSMutableSet* set = controller.taskSet;
    @synchronized(set) {
        for (DMJob* job in set) {
            [job cancel];
            //直接交还到pool
        }
    }
}
-(void)releaseJob:(DMHttpJob*)job{
    job.isRunning = NO;
    job.extra = nil;
    [self onReleaseJob:self.topController.taskSet job:job];
}

-(void)onReleaseJob:(NSMutableSet*)set job:(id)job{
    @synchronized(set) {
        [set removeObject:job];
    }
}



-(BOOL)jobError:(DMHttpJob*)job{
    //错误
    if([job.delegate respondsToSelector:@selector(jobError:)]){
        [self performSelectorOnMainThread:@selector(onError:) withObject:job waitUntilDone:NO];
    }else{
        //这里完成
        [self releaseJob:job];
    }
    return YES;
}

-(void)onError:(DMHttpJob*)job{
    if([job isCanceled]){
        return;
    }
    @try {
        [job.delegate jobError:job];
    }
    @catch (NSException *exception) {
        [DMExceptionHandler handleException:exception];
    }
    @finally {
        [self releaseJob:job];
    }
    
}
-(void)onSuccess:(DMHttpJob*)job{
    
    @try {
        [job.delegate jobSuccess:job];
    }
    @catch (NSException *exception) {
       [DMExceptionHandler handleException:exception];
    }
    @finally {
        [self releaseJob:job];
    }
}


-(void)logout{
    [_apiHandler clearSession];
    [[DMAccount current]logout];
    
}

-(void)jobSuccess:(DMHttpJob*)job{
    if([job isCanceled]){
        return;
    }
    //成功后直接post
    if([job.delegate respondsToSelector:@selector(jobSuccess:)]){
        
        [self performSelectorOnMainThread:@selector(onSuccess:) withObject:job waitUntilDone:NO];
        
    }else{
        //这里完成
        [self releaseJob:job];
    }
}


-(void)post:(NSString*)url type:(DataTypes)type data:(NSDictionary *)data delegate:(id<DMJobDelegate>)delegate{
    DMHttpJob* job = [self getJob];
    job.url = url;
    job.delegate = delegate;
    job.type = JobType_NormalHttp;
    job.data = data;
    job.cachePolicy = DMCachePolicy_NoCache;
    job.dataType = type;
    [self addCacheTask:job];
}

-(void)postJson:(NSString *)url data:(NSDictionary *)data delegate:(id<DMJobDelegate>)delegate{
    [self post:url type:DataType_Json data:data delegate:delegate];
}

-(void)getJson:(NSString *)url delegate:(id<DMJobDelegate>)delegate{
    [self get:url type:DataType_Json delegate:delegate];
}
-(void)get:(NSString*)url delegate:(id<DMJobDelegate>)delegate{
    [self get:url type:DataType_Raw delegate:delegate];
}


-(void)get:(NSString*)url type:(DataTypes)type delegate:(id<DMJobDelegate>)delegate{
    DMHttpJob* job = [self getJob];
    job.url = url;
    job.delegate = delegate;
    job.type = JobType_NormalHttp;
    job.cachePolicy = DMCachePolicy_NoCache;
    job.dataType = type;
    [self addCacheTask:job];
}


-(void)loadImage:(NSString*)url image:(UIImageView*)image{
    if(!url){
        return;
    }
    image.image = nil;
    if([_imageErrors contains:url]){
        //错误
        return;
    }
    DMHttpJob* job = image.job;
    if(job){
        [job cancel];
    }
    job  = [self getJob];
    image.job = job;
    job.url = url;
    job.extra = image;
    job.type = JobType_Image;
    job.dataType = DataType_Image;
    job.cachePolicy = DMCachePolicy_Permanent;
    [self addCacheTask:job];
}





-(void)loadImage:(NSString*)url delegate:(id<DMJobDelegate>)delegate{
    if([_imageErrors contains:url]){
        //错误
        [delegate jobError:nil];
        return;
    }
    
    DMHttpJob* job = [self getJob];
    job.url = url;
    job.delegate = delegate;
    job.type = JobType_NormalHttp;
    job.dataType = DataType_Image;
    job.cachePolicy = DMCachePolicy_Permanent;
    [self addCacheTask:job];
}





-(DMHttpJob*)getJob{
    DMHttpJob* job = [[DMHttpJob alloc]init];
    //放入当前的controller里面
    return job;
}
-(void)start{
    [_moniter start];
}
-(void)stop{
    [_moniter stop];
}


-(NSString*)deviceID{
    if(!_deviceID){
        @synchronized(self){
            if(!_deviceID){
                 NSString *advertisingId = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
               // _deviceID =[CommonUtil md5:[[UIDevice currentDevice].identifierForVendor UUIDString]];
                _deviceID =[CommonUtil md5:advertisingId];
            }
        }
    }
    return _deviceID;
}

-(void)registerSuccessListener:(id<DMJobSuccess>)successListener forType:(NSInteger)type{
    [_successListener registerObject:type object:successListener];
}

-(void)registerParser:(id<DataParser>)parser forType:(NSInteger)type{
    [_parsers registerObject:type object:parser];
}

-(DMViewController*)topController{
    return (DMViewController*)[_rootViewController.navigationController topViewController];
}
-(DMApiJob*)createArrayJsonTask:(NSString *)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server{
    return [self createJsonTask:api cachePolicy:cachePolicy server:server dataType:DataType_ApiArray];
}

-(DMApiJob*)createJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server{
    return [self createJsonTask:api cachePolicy:cachePolicy server:server dataType:DataType_ApiObject];
}
-(DMApiJob*)createPageJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server{
    return [self createJsonTask:api cachePolicy:cachePolicy server:server dataType:DataType_ApiPage];
}
-(DMApiJob*)createJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server dataType:(DataTypes)dataType{
    DMApiJob* job = [[DMApiJob alloc]init];
    job.type = JobType_Api;
    job.dataType = dataType;
    job.server = server;
    job.cachePolicy = cachePolicy;
    job.api = api;
    return job;
}



@end
