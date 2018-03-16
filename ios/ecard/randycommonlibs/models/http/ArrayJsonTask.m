
//
//  ArrayJsonTask.m
//  randycommonlibs
//
//  Created by randy ren on 14-7-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ArrayJsonTask.h"


int Start_Position = 1;


@interface PageData : NSObject
@property (nonatomic) NSInteger position;
@property (nonatomic) NSInteger page;
@property (nonatomic) NSInteger total;
@property (nonatomic,retain) NSObject* listData;
@end


@implementation PageData

-(void)dealloc{
    self.listData = NULL;
}

@end


@interface ArrayJsonTask()
{

}

@end

@implementation ArrayJsonTask
-(void)setCondition:(NSArray *)conditions{
   
}

-(void)jobSuccess:(DMApiJob*)request{
    NSInteger page = [request.param[@"position"]integerValue];
    [_delegate task:self result:request.data position:page isLastPage: YES];
    
    if(_successListener){
        _successListener(request.data,page,YES);
    }
}

-(id)initWithConditions:(NSArray*)conditions{
    if(self=[super init]){
      
    }
    return self;
}
-(NSMutableArray*)array{
    return _job.data;
}




-(void)setPosition:(NSInteger)position{
    [_job put:@"position" value:WRAP_INTEGER(position)];
}



-(void)loadMore:(NSInteger)currentPosition{
    [self setPosition:currentPosition];
    [self execute];
}
-(void)setListener:(NSObject<IArrayRequestResult> *)delegate{
    _delegate = delegate;
    [self setErrorDelegate:delegate];
}




-(void)setClass:(Class)cls{
    _job.clazz= cls;
}



@end
