//
//  DirUtil.h
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DirUtil : NSObject


/**
 *  获取存放图片路径
 *
 *  @return <#return value description#>
 */
+(NSString*)getImageDir;


/**
 获取我的头像位置
 */
+(NSString*)getHeadImagePath;


@end
