//
//  Job.m
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import "DMJob.h"




@implementation DMException



-(id)initWithReason:(NSString*)reason code:(NSInteger)code{
    if(self = [super initWithName:@"DMException" reason:reason userInfo:nil]){
        _code = code;
    }
    return self;
}
-(BOOL)isNetworkError{
    return _code == DMErrorType_Network;
}
@end


@interface DMJob()
{
    
}
@end

@implementation DMJob

-(void)dealloc{
   
}

//取消任务
-(void)cancel{
    _isCanceled = YES;
    if(_ctrl){
        [_ctrl cancelJob:self];
    }
}
-(void)setIsCanceled:(BOOL)canceled{
    _isCanceled = canceled;
    if(canceled){
        _ctrl = nil;
    }
}
//是否被取消
-(BOOL)isCanceled{
    return _isCanceled;
}



@end
