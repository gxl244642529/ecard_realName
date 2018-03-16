//
//  AddressVo.h
//  ecard
//
//  Created by randy ren on 16/2/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DMLib/DMLib.h>
@interface AddressVo : NSObject<IJsonValueObject>

/**
 * 省
 */
@property (nonatomic,retain) NSString* sheng;
/**
 * 市
 */
@property (nonatomic,retain) NSString* shi;
/**
 * 区
 */
@property (nonatomic,retain) NSString* qu;
/**
 * 详细地址
 */
@property (nonatomic,retain) NSString* jie;
/**
 * 邮编
 */
@property (nonatomic,retain) NSString* postcode;
/**
 * 是否是默认地址
 */
@property (nonatomic) BOOL def;
/**
 * 姓名
 */
@property (nonatomic,retain) NSString* name;
/**
 * null
 */
@property (nonatomic,retain) NSString* phone;
/**
 * null
 */
@property (nonatomic,assign) NSInteger id;

@property (nonatomic,assign) NSInteger tyId;



@property (nonatomic,assign) NSInteger shengId;
@property (nonatomic,assign) NSInteger shiId;
@property (nonatomic,assign) NSInteger quId;


//详细地址
@property (nonatomic,readonly,copy) NSString* address;
@property (nonatomic,readonly,copy) NSString* area;
@end
