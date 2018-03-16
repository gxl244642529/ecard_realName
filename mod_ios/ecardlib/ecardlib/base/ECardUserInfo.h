//
//  ECardUserInfo.h
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>

@interface ECardUserInfo : DMAccount

/**
 用户头像
 */
@property (nonatomic,copy) NSString* headImage;

/**
 绑定手机号码
 */
@property (nonatomic,copy) NSString* userPhone;



@property (nonatomic,copy) NSString* bg;


@property (nonatomic,copy) NSString* userHash;

//数据
@property (nonatomic,retain) NSMutableDictionary* data;


+(void)loginSuccess:(DMApiJob*)request account:(NSString*)account pwd:(NSString*)pwd;

+(void)logout;

@end
