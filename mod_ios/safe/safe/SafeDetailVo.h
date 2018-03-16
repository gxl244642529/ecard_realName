//
//  SafeDetailVo.h
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DMLib/DMLib.h>
/*
 title": "e通卡随心保（6个月）",
 "company": "PICC",
 "summary": "小小1元，放心用卡^手续简便，轻松补卡",
 "background_url": "/uploads/prod_bg/1.jpg",
 "detail_url": "/uploads/prod_bg/5.jpg",
 "safeguard_url": "/article/show/4",
 "safeguard": "产品详情",
 "service_tel": "0592-95518",
 "duration": "4",
 "year_limit": "无",
 "sales_limit": "0",
 "pid_sales_limit": "2",
 "ori_price": "2.00",
 "price": "1.00",
 "start_time": "2016-01-26 00:00:00",
 "end_time": "2016-07-25 23:59:59",
 "protocol_title": "",
 "protocol_url": "/article/show/8",
 "notice_url": "/article/show/6"
 */
@interface SafeDetailVo : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* title;
//优惠价
@property (nonatomic,copy) NSString* price;
@property (nonatomic,assign) float priceVal;

@property (nonatomic,copy) NSString* headImage;

//原价
@property (nonatomic,copy) NSString* orgPrice;

@property (nonatomic,copy) NSString* startTime;
@property (nonatomic,copy) NSString* endTime;

@property (nonatomic,copy) NSString* company;

//协议
@property (nonatomic,copy) NSString* protocolUrl;
//通知
@property (nonatomic,copy) NSString* noticeUrl;
@property (nonatomic,copy) NSString* summary;


@property (nonatomic,copy) NSString* guardUrl;

@property (nonatomic,assign) NSInteger limit;

//服务电话
@property (nonatomic,copy) NSString* serviceTel;

//保障期限
@property (nonatomic,copy) NSString* duration;





@end
