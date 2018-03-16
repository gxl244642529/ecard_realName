//
//  JsonTask.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMApiJob.h"
#import "DMNotifier.h"
#import "DMJobManager.h"
#import "DMApiUtil.h"
#import "Alert.h"

@interface DMApiJob()
{
    NSMutableDictionary* _paramObject;
    NSString* _realApi;
    NSString* _errorNotification;
    NSString* _successNotification;
    NSString* _messageNotification;
    
}

@end

@implementation DMApiJob

-(void)dealloc{
    if(_waitingMessage){
        [Alert cancelWait];
    }
     NSLog(@"Api %@ dealloc",_api);
    _realApi = nil;
    self.clazz = nil;
    _paramObject = nil;
    self.waitingMessage = nil;
    self.serverMessage = nil;
    _errorNotification = nil;
    _successNotification = nil;
    _messageNotification = nil;
}

-(id)init{
    if(self = [super init]){
        _paramObject = [[NSMutableDictionary alloc]init];
    }
    return self;
}


-(void)putAll:(NSMutableDictionary*)param{
    _paramObject = param;
}

-(void)reload{
    _cacheKey = nil;
    self.isCanceled = NO;
    if([self.param objectForKey:@"position"]){
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            [[DMJobManager sharedInstance]clearCache:self];
            [[DMJobManager sharedInstance]addApiTask:self];
        });
    }else{
        [[DMJobManager sharedInstance]addApiTask:self];
    }
    
}
-(void)put:(NSString*)key value:(id)data{
    [_paramObject setValue:data forKey:key];
}
-(void)execute{
    if(_button){
        if( [[NSThread currentThread]isMainThread] ){
            [DMApiUtil disableButton:_button];
        }else{
            dispatch_async(dispatch_get_main_queue(), ^{
                [DMApiUtil disableButton:_button];
            });
        }
        
    }
    
    _cacheKey = nil;
    self.isCanceled = NO;
    
    [[DMJobManager sharedInstance]executeApi:self];
}

-(NSMutableDictionary*)param{
    return _paramObject;
}


-(NSString*)successNotification{
    if(!_successNotification){
        _successNotification =[NSString stringWithFormat:@"n_s_%@", _realApi];
    }
    return _successNotification;
}
-(NSString*)errorNotification{
    if(!_errorNotification){
        _errorNotification =[NSString stringWithFormat:@"n_e_%@", _realApi];
    }
    return _errorNotification;
}
-(NSString*)messageNotification{
    if(!_messageNotification){
        _messageNotification =[NSString stringWithFormat:@"n_m_%@",_realApi];
    }
    return _messageNotification;
}
-(void)setApi:(NSString *)api{
    _api = api;
    _realApi = [api stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
}

-(NSMutableString*)makeDataKey{
    //这里需要搜索条件支持
    
    NSArray* arr = [_paramObject allKeys];
    arr = [arr sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2){
        NSComparisonResult result = [obj1 compare:obj2];
        return result==NSOrderedDescending;
    }];
    NSMutableString* dataKey = [[NSMutableString alloc]initWithCapacity:30];
    [dataKey appendString:_realApi];
    for (NSString* key in arr) {
        if([key isEqualToString:@"deviceID"]){
            continue;
        }
        if([key isEqualToString:@"position"]){
            continue;
        }
        [dataKey appendFormat:@"_%@",_paramObject[key]];
    }
    
    return dataKey;
}


-(NSString*)makeCacheKey{
    NSMutableString* dataKey = [self makeDataKey];
    
    if([_paramObject objectForKey:@"position"]){
        
        [dataKey appendFormat:@"_%@",_paramObject[@"position"]];
        
    }
    
    return dataKey;
    
}

@end
