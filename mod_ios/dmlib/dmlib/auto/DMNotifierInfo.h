//
//  NotifierInfo.h
//  DMLib
//
//  Created by randy ren on 16/2/25.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMNotifierInfo :NSObject
@property (nonatomic,weak) id observer;
@property (nonatomic) SEL selector;
//消息名称
@property (nonatomic,retain) NSString* notificaion;
@end