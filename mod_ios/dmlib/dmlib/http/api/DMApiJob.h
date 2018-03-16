//
//  JsonTask.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DMHttpJob.h"
#import "DMMacro.h"

//加密类型
typedef enum _CryptType{
    CryptType_None=0,
    CryptType_Upload,
    CryptType_Download,
    CryptType_Both
}CryptType;

typedef enum _MessageType{
    MessageType_Toast=0,
    MessageType_Alert=1
}MessageType;


@protocol DMApiCtrl <NSObject>

-(void)put:(NSString*)key value:(id)data;
-(void)execute;
-(void)reload;
-(void)cancel;

@end




@interface DMApiJob : DMHttpJob<DMApiCtrl>


-(NSMutableDictionary*)param;

-(void)putAll:(NSMutableDictionary*)param;

//
@property (nonatomic,retain,setter=setApi:) NSString* api;

@property (nonatomic) Class clazz;

BLOCK_PROPERTY(successCallback,id)

-(NSString*)successNotification;
-(NSString*)errorNotification;
-(NSString*)messageNotification;


//服务器的消息,用于通知
@property (nonatomic,retain) NSString* serverMessage;

//-1表示弱弹出框 -2表示弹出框
@property (nonatomic,assign) MessageType serverMessageType;

//通知是否是以弹出框的形式
@property (nonatomic,assign) BOOL alert;


//是否加密
@property (nonatomic,assign) CryptType crypt;


//等待
@property (nonatomic,retain) NSString* waitingMessage;


//点击的button
@property (nonatomic,weak) UIView* button;

-(NSMutableString*)makeDataKey;
@end
