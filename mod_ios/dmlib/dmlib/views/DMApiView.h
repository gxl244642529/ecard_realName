//
//  ApiItem.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

//表示一个可以做api调用的视图
@protocol DMApiView <NSObject>

//api
@property (nonatomic,copy) NSString* api;

//服务器
@property (nonatomic,assign) NSInteger server;

@end

