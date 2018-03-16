//
//  UserImageModel.h
//  ecard
//
//  Created by 任雪亮 on 17/1/21.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <dmlib/dmlib.h>

/**
 用户图片
 */
@interface UserImageModel : DMModel





-(void)setUserImage:(UIImage*)image type:(int)type;

-(void)setHeadImage:(UIImage*)image;

-(void)setBg:(UIImage*)image;

@end
