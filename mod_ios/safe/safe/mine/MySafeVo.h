//
//  MySafeVo.h
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface MySafeVo : NSObject<IJsonValueObject>

//名字
@property (nonatomic,copy) NSString* name;
@property (nonatomic,copy) NSString* phone;
@property (nonatomic,copy) NSString* idCard;

@property (nonatomic,assign) NSInteger count;

@property (nonatomic,copy) NSString* picc_policy_no;

//金额
@property (nonatomic,copy) NSString* payPrice;


@property (nonatomic,copy) NSString* carNo;

@property (nonatomic,copy) NSString* address;


@property (nonatomic,copy) NSString* carFrame;

//被保险人列表
@property (nonatomic,retain) NSArray* insurant;

//标题
@property (nonatomic,copy) NSString* title;
//公司
@property (nonatomic,copy) NSString* company;
//优惠价
@property (nonatomic,copy) NSString* price;



@property (nonatomic,copy) NSString* serviceTel;

@property (nonatomic,copy) NSString* guardUrl;

@property (nonatomic,copy) NSString* cardId;
//协议
@property (nonatomic,copy) NSString* protocolUrl;
//通知
@property (nonatomic,copy) NSString* noticeUrl;
//样品
@property (nonatomic,copy) NSString* sampleUrl;

//理赔金额
@property (nonatomic,copy) NSString* claimsValue;

//原价
@property (nonatomic,copy) NSString* orgPrice;
//缩略图
@property (nonatomic,copy) NSString* image;
//typeid
@property (nonatomic,assign) NSInteger typeId;
//
@property (nonatomic,copy) NSString* orderId;
//摘要
@property (nonatomic,copy) NSString* summary;
@property (nonatomic,copy) NSString* startTime;
@property (nonatomic,copy) NSString* endTime;


@property (nonatomic,assign) NSInteger status;
@property (nonatomic,readonly) NSString* statusStr;
@property (nonatomic,readonly) NSString* duration;
@property (nonatomic,readonly) NSString* statusTag;


@property (nonatomic,copy) NSString* detailUrl;


-(NSInteger)fee;
-(BOOL)shouldHideLostButton;
@end
