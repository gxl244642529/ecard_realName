//
//  BaseImageHandler.m
//  ecard
//
//  Created by randy ren on 15/9/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <ecardlib/ecardlib.h>

#import "BaseImageHandler.h"
#import "LoginImageHandler.h"

@implementation BaseImageHandler

-(void)dealloc{
    _localImage = nil;
    _defaultImage = nil;
}


-(void)handle:(id)view{
  
  if([view isKindOfClass:[UIImageView class]]){
    UIImageView* imageView = view;
    //背景
    NSString* string = [self imagePath];
    if([[NSFileManager defaultManager]fileExistsAtPath:string]){
      UIImage* image = [UIImage imageWithContentsOfFile:string];
      if(image){
        [imageView setImage:image];
      }else{
       // NSString* str = [NSString stringWithFormat:@"ecardlibbundle.bundle/%@",_defaultImage];
        //imageView.image = [UIImage imageNamed:str];
      }
    }else{
    //  NSString* str = [NSString stringWithFormat:@"ecardlibbundle.bundle/%@",_defaultImage];
     // imageView.image = [UIImage imageNamed:str];
    }

  }else{
    UIButton* imageView = view;
    
    
    //背景
    NSString* string = [self imagePath];
    if([[NSFileManager defaultManager]fileExistsAtPath:string]){
      UIImage* image = [UIImage imageWithContentsOfFile:string];
      if(image){
        [imageView setImage:image forState:UIControlStateNormal];
      }else{
        NSString* str = [NSString stringWithFormat:@"ecardlibbundle.bundle/%@",_defaultImage];
        UIImage* image  = [UIImage imageNamed:str];
        
          [imageView setImage:image forState:UIControlStateNormal];
        
      }
    }else{
      NSString* str = [NSString stringWithFormat:@"ecardlibbundle.bundle/%@",_defaultImage];
      UIImage* image  = [UIImage imageNamed:str];
      [imageView setImage:image forState:UIControlStateNormal];

    }
    

  }
  
  
}

-(id)initWithDefaultImage:(NSString*)defaultImage localImage:(NSString*)localImage type:(int)type  cornor:(BOOL)cornor{
    if(self = [super init]){
        _defaultImage = defaultImage;
        _localImage = localImage;
        _type = type;
        _cornor = cornor;
    }
    return self;
}

/**
 本地图片地址
 */
-(NSString*)imagePath{
    return [NSString stringWithFormat:@"%@/%@.jpg",[CacheUtil getPath:@"image"],_localImage];
}

+(BaseImageHandler*)create:(NSString*)defaultImage localImage:(NSString*)localImage key:(NSString*)key  type:(int)type  cornor:(BOOL)cornor{
    if([DMAccount isLogin]){
        return [[LoginImageHandler alloc]initWithDefaultImage:defaultImage localImage:localImage key:key type:type cornor:cornor];
    }
    return [[BaseImageHandler alloc]initWithDefaultImage:defaultImage localImage:localImage type:type cornor:cornor];
}

//保存图片
-(void)onSaveImage:(UIImage*)image{
    //写入缓存系统
    dispatch_async(GLOBAL_QUEUE, ^{
        //保存至文件
        NSString* aPath = [self imagePath];
        NSData *imgData = UIImageJPEGRepresentation(image,0);
        [imgData writeToFile:aPath atomically:YES];
        
    });
}

@end
