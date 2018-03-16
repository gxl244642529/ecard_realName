//
//  Job.h
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJobDelegate.h"
#import "Cancelable.h"

//错误号
typedef enum{
    DMErrorType_Network,
    DMErrorType_InvalidData, //不是json数据、数据解密失败等等
    DMErrorType_BusinessProcess,//流程错误
}DMErrorType;


@interface DMException : NSException

-(id)initWithReason:(NSString*)reason code:(NSInteger)code;

//错误号
@property (nonatomic,assign) NSInteger code;
//原来错误
@property (nonatomic,retain) NSError* error;


-(BOOL)isNetworkError;

@end


@protocol DMJobCtrl <NSObject>

-(void)cancelJob:(id)job;

@end

@protocol DMJobHandler <NSObject>

-(void)handleTask:(id)task index:(NSInteger)index;
-(void)stop;

@end


@interface DMJob : NSObject
{
    BOOL _isCanceled;
}
/**
 * 指定类型
 */
@property (nonatomic,assign) NSInteger type;
@property (nonatomic,weak) id<DMJobCtrl> ctrl;
//是否被取消
-(BOOL)isCanceled;
-(void)setIsCanceled:(BOOL)canceled;
-(void)cancel;


@end
