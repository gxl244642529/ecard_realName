//
//  MyECardVo.h
//  myecard
//
//  Created by 任雪亮 on 17/3/29.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <ecardlib/ecardlib.h>

@interface MyECardVo : ECard

@property (nonatomic,copy) NSString* createDate;
@property (nonatomic,assign) int cardFlag;

@end
