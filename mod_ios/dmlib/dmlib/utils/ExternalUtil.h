//
//  ExternalUtil.h
//  ecard
//
//  Created by randy ren on 15/3/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ExternalUtil : NSObject



+(void)showActionSheet:(UIViewController*)parent  completion:(void (^)(UIImage*))completion;

+(void)selectFromAlbum:(UIViewController*)parent completion:(void (^)(UIImage*))completion;

+(void)captureImage:(UIViewController*)parent completion:(void (^)(UIImage*))completion;


+(void)destroy;

@end
