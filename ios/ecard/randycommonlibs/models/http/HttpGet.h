//
//  HttpGet.h
//  ecard
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "IRequestResult.h"
#import <DMLib/dmlib.h>
/**
 获取json数据
 */
@interface HttpGet : NSObject<DMJobDelegate>

-(void)get:(NSString*)url listener:(NSObject<IRequestResult>*)listener;
-(void)get:(NSString*)url listener:(NSObject<IRequestResult>*)listener parser:(NSObject<NSXMLParserDelegate>*)parser;

@end
