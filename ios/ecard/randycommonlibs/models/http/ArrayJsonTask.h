//
//  ArrayJsonTask.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "JsonTask.h"
#import "IArrayRequestResult.h"
#import "IArrayJsonTask.h"


#define DATA_KEY_FORMAT @"%@_%@_%d"

extern int Start_Position;//  1


@interface ArrayJsonTask : JsonTask<IArrayJsonTask>
{

    __weak NSObject<IArrayRequestResult>* _delegate;
}
-(void)setClass:(Class)cls;
-(void)setCondition:(NSArray*)conditions;
-(void)setListener:(NSObject<IArrayRequestResult>*)delegate;
-(void)setPosition:(NSInteger)position;
//
-(void)loadMore:(NSInteger)currentPosition;

-(NSMutableArray*)array;

@property (copy,readwrite,nonatomic) void(^successListener)(NSArray* result,NSInteger position,BOOL isLastPage);

@end
