//
//  ImageErrors.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ImageErrors : NSObject

-(void)add:(NSString*)url;
-(BOOL)contains:(NSString*)url;


@end
