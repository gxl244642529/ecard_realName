//
//  SafeVo.h
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>

/**
 "title": "e通卡贴心保（6个月）",
 "insurance_id": "5433",
 "compensate": "多一道余额保障  |  随心充值  |  在线理赔",
 "info": "2.50",
 "type_id": "1",
 "icon_url": "/uploads/prod_bg/2.jpg",
 "promotion_type": "2",
 "ori_price": "5.00",
 "on_sale": "1"
 */

@interface SafeVo : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* inId;
@property (nonatomic,copy) NSString* title;
@property (nonatomic,copy) NSString* detail;
@property (nonatomic,copy) NSString* image;
@property (nonatomic,copy) NSString* price;
@property (nonatomic,copy) NSString* orgPrice;
@property (nonatomic,assign) BOOL isSale;
@property (nonatomic,copy) NSString* typeId;

@property (nonatomic,copy) NSString* ticket;
//购买数量
@property (nonatomic,assign) NSInteger count;
//促销类型
@property (nonatomic,assign) NSInteger type;

//促销图片
-(UIImage*)promotion;


DECLARE_HIDDEN_PROPERTY(price);

@end
