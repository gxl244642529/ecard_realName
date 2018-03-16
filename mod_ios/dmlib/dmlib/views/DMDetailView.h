//
//  DetailView.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMJobDelegate.h"

IB_DESIGNABLE
@interface DMDetailView : UIView<DMJobDelegate>



//api
@property (nonatomic,copy) IBInspectable NSString* api;
//
@property (nonatomic,copy) IBInspectable NSString* cachePolicy;
//加密方式  0:不加密 1:上行加密  2:下载加密 3:都加密
@property (nonatomic,assign) IBInspectable NSInteger crypt;
//服务器
@property (nonatomic,assign) IBInspectable NSInteger server;


//数据主键,用于传入的data的索引
@property (nonatomic,copy) IBInspectable NSString* dataKey;

//数据主键,参数名称,用于向服务器传输数据的索引,数据为上述数据
@property (nonatomic,copy) IBInspectable NSString* paramKey;

//自动
@property (nonatomic,assign) IBInspectable BOOL autoExecute;



@property (nonatomic,copy) IBInspectable NSString* entityName;


//是否在初始化的时候设置值
@property (nonatomic,assign) IBInspectable BOOL setValueOnInit;



@property (nonatomic,retain) IBInspectable NSDictionary* extraData;


-(void)execute;

@end
