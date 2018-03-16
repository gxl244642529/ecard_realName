//
//  JsonTaskManager.m
//  randycommonlibs
//
//  Created by randy ren on 14-7-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "JsonTaskManager.h"



#define USER_ID @"userID"
#define USER_ACC @"userAcc"
#define USER_PWD @"userPwd"


@interface JsonTaskManager()
{
    NSString* _pushID;
    __weak id _loginTarget;
    SEL _loginSuccess;
}
@end


@implementation JsonTaskManager

__weak static id<IJsonTaskManager> _instance;
+(BOOL)isEmpty:(id)str{
    if(!str)return YES;
    if([str isKindOfClass:[NSNull class]])return YES;
    
    NSString* s = str;
    return [s compare:@""]==0;
}

-(NSString*)getPushID{
  return [PushUtil getPushId];
}

-(void)loadImage:(NSString*)url listener:(NSObject<DMJobDelegate>*)listener{
    [[DMJobManager sharedInstance]loadImage:url delegate:listener];
}

+(void)setImageSrcSync:(UIImageView*)image src:(NSString*)src{
  [_instance setImageSrcSync:image src:src];
}



-(void)setImageSrcSync:(UIImageView*)imageView src:(NSString*)url{
  if( ![url hasPrefix:@"http"]){
    url = [DMServers formatUrl:0 url:url];
  }
  
  [[DMJobManager sharedInstance]loadImage:url image:imageView];
  
}


+(NSObject<IJsonTaskManager>*)sharedInstance{
    return _instance;
}
+(void)setInstance:(id)instance{
    _instance = instance;
}

-(float)getCacheSize{
   // return [[SDImageCache sharedImageCache] checkTmpSize];
    return 0;
}
-(void)setImageSrc:(UIImageView*)imageView src:(NSString*)url{
  
  
  if([url hasPrefix:@"/"]){
    url = [DMServers formatUrl:0 url:url];
  }
  
    [[DMJobManager sharedInstance]loadImage:url image:imageView];
}

-(void)setImageSrcDirect:(UIImageView*)imageView src:(NSString*)url{
    [[DMJobManager sharedInstance]loadImage:url image:imageView];
}
+(void)setImageSrcDirect:(UIImageView*)image src:(NSString*)src{
   [_instance setImageSrcDirect:image src:src];
}

//登录成功

-(DMPopType)onLoginSuccess{
    PerformSelector(_loginTarget, _loginSuccess, self);
    return DMPopBySelf;
}


-(void)onLoginCancel{
    _loginTarget = nil;
    _loginSuccess = nil;
}

+(void)setImagePath:(NSString*)path image:(UIImageView*)image{
    
    UIImage* img = [UIImage imageWithContentsOfFile:path];
    image.image = img;
    [image setImage:img];
   // [image setImageWithURL:[NSURL URLWithString:path]];
}



-(id)init{
    if(self=[super init]){
       
    }
    return self;
}

-(void)setPushID:(NSString*)pushID{
    
    if([pushID compare:_pushID]!=0){
        _pushID = pushID;
        [self uploadDeviceToken:_pushID];
    }
}

-(void)onUploadDeviceToenSuccess{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:_pushID forKey:@"push_id"];
    [def synchronize];

}
-(void)uploadDeviceToken:(NSString*)deviceToken{
    
}

-(void)start{

 }



-(void)networkStateChange{
    
}

-(void)destroy{
   
}

-(BOOL)isLogin{
    return [DMAccount isLogin];
}

/**
 *  登录
 */
-(void)logout{
    [self doLogout];
   
}


-(void)doLogout{
    [[DMJobManager sharedInstance]logout];
}





-(void)clearCache{
    [[DMJobManager sharedInstance]clearCache];
}

-(void)checkLogin:(UIViewController*)parent loginSuccess:(SEL)loginSuccess{
    if([self isLogin]){
        PerformSelector(parent, loginSuccess, self);
    }else{
        _loginTarget = parent;
        _loginSuccess = loginSuccess;
        //登录
        [self callLoginController:parent];
    }
    
    
}


-(void)checkLogin:(id)sender parent:(UIViewController*)parent loginSuccess:(SEL)loginSuccess{
    if([self isLogin]){
        PerformSelector(sender, loginSuccess, self);
    }else{
        _loginTarget = sender;
        //登录
        [self callLoginController:parent];
    }
}


-(void)callLoginController:(UIViewController*)parent{
    [DMAccount callLoginController:self];
}

-(void)saveToCache:(NSData*)data url:(NSString*)url{
   
}




-(ECardUserInfo*)createUserInfo{
    return nil;
}
-(void)beforeLogin:(JsonTask*)task account:(NSString*)userName password:(NSString*)password{
    
}
-(NSString*)getLoginApi{
    return NULL;
}
-(void)dealloc{
    [self destroy];
}


-(ECardUserInfo*)userInfo{
    return [DMAccount current];
}





-(ObjectJsonTask*)createObjectJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy{
     DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:api cachePolicy:cachePolicy server:0];
    
    ObjectJsonTask* otask =[[ObjectJsonTask alloc]init];
    [otask setJob:job];
    [otask setCachePolicy:cachePolicy];
    return otask;
}


-(ObjectJsonTask*)createObjectJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy factory:(NSInteger)factory{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:api cachePolicy:cachePolicy server:factory];
    ObjectJsonTask* otask =[[ObjectJsonTask alloc]init];
    [otask setJob:job];
    [otask setCachePolicy:cachePolicy];
    [otask setType:factory];
    return otask;
}



-(ArrayJsonTask*)createArrayJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy isPrivate:(BOOL)isPrivate {
      DMApiJob* job = [[DMJobManager sharedInstance]createArrayJsonTask:api cachePolicy:cachePolicy server:0];
    ArrayJsonTask* task = [[ArrayJsonTask alloc]init];
    [task setJob:job];
    [task setCachePolicy:cachePolicy];
    [task setPosition:Start_Position];
    
    return task;
}


-(ArrayJsonTask*)createArrayJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy isPrivate:(BOOL)isPrivate factory:(NSInteger)factory{
    DMApiJob* job = [[DMJobManager sharedInstance]createArrayJsonTask:api cachePolicy:cachePolicy server:factory];
    ArrayJsonTask* task = [[ArrayJsonTask alloc]init];
    [task setJob:job];
    [task setCachePolicy:cachePolicy];
    [task setPosition:Start_Position];
    [task setType:factory];
    
    return task;
}




-(NSString*)getDeviceID{
    return [DMJobManager sharedInstance].deviceID;
}

//停止相关网络操作
-(void)onDestroy:(id)obj{
    
}



@end
