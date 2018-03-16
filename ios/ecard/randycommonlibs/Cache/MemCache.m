//
//  MemCache.m
//  ecard
//
//  Created by randy ren on 14-4-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "MemCache.h"


@interface CacheData : NSObject
{
    
}
@property (nonatomic,retain)  NSDate* lastModified;


@property (nonatomic,retain) NSObject* data;

@end



@implementation CacheData

-(void)dealloc{
    self.lastModified = NULL;
    self.data = NULL;
}

-(id)init
{
    if(self=[super init]){
        self.lastModified = [NSDate date];
    }
    return self;
}

@end

//////////////////////

@interface MemCache()
{
    NSMutableDictionary* _dataMap;
    BOOL _isTimeOut;
}

@end

@implementation MemCache
-(id)init{
    if(self=[super init]){
        _isTimeOut=FALSE;
        _dataMap = [[NSMutableDictionary alloc]init];
    }
    return self;
}
-(void)remove:(NSString*)key{
    [_dataMap removeObjectForKey:key];
}
-(void)dealloc{
    _dataMap=NULL;
}

//直接放入,
-(BOOL)put:(NSString*)key value:(NSObject*)value
{
    CacheData* data = [[CacheData alloc]init];
    data.data = value;
    [_dataMap setValue:data forKey:key];
    return true;
}
//更新缓存时间
-(void)updateExpire:(NSString*)key{
    CacheData* data = [_dataMap valueForKey:key];
    if(data){
        data.lastModified = [NSDate date];
    }
}
//如果过期，则返回空
-(NSObject*)get:(NSString*)key{
   /*CacheData* data = [_dataMap valueForKey:key];
    if(data)
    {
       
        NSTimeInterval timeBetween = [[NSDate date] timeIntervalSinceDate:data.lastModified];
        _isTimeOut = timeBetween > 3 * 3600;
        if(!_isTimeOut){
            return data.data;
        }
    }*/
    return NULL;
}
-(NSObject*)getIgnoreTimeout:(NSString*)key{
  /* CacheData* data = [_dataMap valueForKey:key];
    if(data)
    {
        return data.data;
    }*/
    return NULL;

}
-(BOOL)contains:(NSString*)key{
   //return [_dataMap valueForKey:key]!=NULL;
    return FALSE;
}
//是否过期,此方法只能在get之后调用，判断本次取出的内容是否过期
-(BOOL)isTimeout{
    return _isTimeOut;
}
-(void)clear{
    _dataMap = [[NSMutableDictionary alloc]init];
}

@end
