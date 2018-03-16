//
//  RModel.h
//  ecard
//
//  Created by randy ren on 15/5/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JsonTaskManager.h"
#import "CommonMacro.h"

#define API_SUCCESS(Name,Type) -(void)Name:(Type)result
#define API_ERROR(Name) -(void)Name:(NSString*)errorMessage;


#define DECLARE_API(Name) -(NSString*)Name;
#define DECLARE_API_API(Name) -(NSString*)api_##Name;


#define IMPLEMENT_API(Name) -(NSString*)Name{ return @#Name; }


#define IMPLEMENT_API2(Name,ApiName) -(NSString*)Name{ return [NSString stringWithFormat:@"%@/%@",@#ApiName,@#Name]; }

#define IMPLEMENT_API_API(Name) -(NSString*)api_##Name{ return @#Name; }
#define IMPLEMENT_API_API2(Name,ApiName) -(NSString*)api_##Name{ return [NSString stringWithFormat:@"%@/%@",@#ApiName,@#Name]; }


#define ON_ERROR(Name) -(void)Name:(NSString*)error



@interface RModel : NSObject<IRequestResult>

-(id)initWithObserver:(id)observer;
-(void)setObserver:(id)observer;

-(void)setObserver:(NSString*)key selector:(SEL)selector;

-(void)notifyObserver:(NSString*)key data:(id)data;

+(NSString*)makeError:(NSString*)api;
-(ObjectJsonTask*)getTask:(NSString*)api;
-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy;
-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy waitingMessage:(NSString*)waitingMessage;


-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy factory:(NSInteger)factory;
-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy waitingMessage:(NSString*)waitingMessage factory:(NSInteger)factory;


@end
