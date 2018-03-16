//
//  DMShareUil.h
//  ecardlib
//
//  Created by 任雪亮 on 17/1/21.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <dmlib/dmlib.h>


typedef enum _DMShareType{
    ShareType_WXSingle=1,
    ShareType_WXFrend,
    
    
}DMShareType;

@interface DMShareUtil : NSObject

+(void)share:(NSString*)content title:(NSString*)title  parent:(UIView*)parent type:(DMShareType) type;
+(BOOL)handleOpenUrl:(NSURL *)url;

+(void)share:(NSDictionary*)map onSuccess:BLOCK_PARAM(onSuccess,BOOL);

+(void)initSDK;
@end
