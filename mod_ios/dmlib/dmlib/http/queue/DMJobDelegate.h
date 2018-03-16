//
//  HttpDelegate.h
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//


#import <Foundation/Foundation.h>

@protocol DMJobError< NSObject>

/**
 * 网络问题\服务器报错505等
 */
@optional
-(BOOL)jobError:(id)request;
@end




@protocol DMJobSuccess <NSObject>
/**
 * 设计数据解析
 */
@optional
-(void)jobSuccess:(id)request;


@end

@protocol DMJobFinish <DMJobSuccess,DMJobError>


-(void)releaseJob:(id)job;

@end



@protocol DMJobDelegate <DMJobSuccess,DMJobError>



/**
 * 进度
 */
@optional
-(void)jobProgress:(id)request total:(NSInteger)total progress:(NSInteger)progress;

@end