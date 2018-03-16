//
//  UserImageModel.m
//  ecard
//
//  Created by 任雪亮 on 17/1/21.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "UserImageModel.h"



@interface UserImageModel()
{
  NSData* _imageDataHead;
  NSData* _imageDataBg;
}

@end


@implementation UserImageModel


-(void)dealloc{
  _imageDataBg = nil;
  _imageDataHead = nil;
}


-(void)setHeadImage:(UIImage*)image{
  dispatch_async(GLOBAL_QUEUE, ^{
    //头像大小为640
    UIImage* result = image;
    if(image.size.width > 640 ){
      double dx =640/ image.size.width;
      double dy = 640/image.size.height;
      dx = MIN(dx, dy);
      
      result=[image resize:CGSizeMake(image.size.width * dx, image.size.height * dx)];
      
    }
    NSData* imgData = UIImageJPEGRepresentation(result,1);
    _imageDataHead = imgData;
    dispatch_async(dispatch_get_main_queue(), ^{
      DMApiJob* job = [self createApi: @"user/head"];
      job.cachePolicy = DMCachePolicy_NoCache;
      [job put:@"head" value:imgData];
      [job execute];
    });
  });
  
  
}
-(void)setBg:(UIImage*)image{
  dispatch_async(GLOBAL_QUEUE, ^{
    //头像大小为640
    UIImage* result = image;
    if(image.size.width > 640 ){
      double dx =640/ image.size.width;
      double dy = 640/image.size.height;
      dx = MIN(dx, dy);
      
      result=[image resize:CGSizeMake(image.size.width * dx, image.size.height * dx)];
      
    }
    NSData* imgData = UIImageJPEGRepresentation(result,1);
    _imageDataBg = imgData;
    NSString* base64string = [imgData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    NSString* base64= [DMBase64 encodeURL:base64string];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      
      
      DMApiJob* job = [self createApi: @"user_api/set_bg"];
      job.cachePolicy = DMCachePolicy_NoCache;
        job.server = 0;
      [job put:@"img" value:base64];
      [job execute];

    });
  });
  
  
}

-(void)setUserImage:(UIImage*)image type:(int)type{
  if(type==0){
    [self setBg:image];
  }else{
    [self setHeadImage:image];
  }
}



@end
