//
//  ApiDelegate.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>


#import "DMJobDelegate.h"

@protocol DMApiDelegate <DMJobDelegate>


//服务器信息
@optional
-(BOOL)jobMessage:(id)request;


@end
