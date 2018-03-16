//
//  DMApiNetwork.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMApiNetwork : NSObject


-(NSData*)execute:(NSInteger)timeoutSeconds url:(NSString*)url extraHeaders:(NSDictionary*)extraHeaders data:(NSData*)data;

-(void)cancel;

-(NSError*)error;

@end
