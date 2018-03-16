//
//  FormButton.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMSubmit.h"
#import "DMFormElement.h"
#import "DMApiDelegate.h"
#import "DMApiJob.h"
#import "DMJobManager.h"



@protocol DMSubmitDelegate <NSObject>

/**
 在提交的时候需要验证
 */
@optional
-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary*)data;

/**
 在提交之前,需要补充一些数据
 */
@optional
-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary*)data;

/**
 最后确认，是否由delegate实现
 */
@optional
-(BOOL)submit:(id<DMSubmit>)submit onSubmit:(NSMutableDictionary*)data;

@end

IB_DESIGNABLE
@interface DMSubmitButton : UIButton<DMSubmit>

//api
@property (nonatomic,copy) IBInspectable NSString* api;

@property (nonatomic,assign) IBInspectable NSInteger crypt;

//服务器
@property (nonatomic,assign) IBInspectable NSInteger server;

//等待信息
@property (nonatomic,copy) IBInspectable NSString* waitingMessage;
//表单元素的名字,如有多个,则用逗号隔开;
//如为空,则表示传输所有表单元素
@property (nonatomic,copy) IBInspectable NSString* submitItems;


//确认对话框
@property (nonatomic,copy) IBInspectable NSString* confirmMessage;



-(void)onSubmit:(id)sender;


-(void)doSubmit:(NSMutableDictionary*)dic;

-(BOOL)doValidate:(NSMutableDictionary*)dic;
//提交表单
-(void)submit;

-(void)releaseKeyboard;

-(DMApiJob*)apiJob;



@property (nonatomic,weak) id<DMSubmitDelegate> delegate;

@property (nonatomic,weak) NSObject<DMForm>* form;

@end
