//
//  DataParser.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>




@protocol DataParser<NSObject>

/**
 * 数据解析
 */
-(id)parseData:(id)request data:(id)data error:(NSError**)error;

@end
