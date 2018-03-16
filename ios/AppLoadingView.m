//
//  AppLoadingView.m
//  ecard
//
//  Created by 任雪亮 on 17/3/28.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "AppLoadingView.h"

@interface AppLoadingView ()
{
  UIImageView* _imageView ;
}

@end


@implementation AppLoadingView

-(id)initWithFrame:(CGRect)frame{
  
  if(self = [super initWithFrame:frame]){
    self.backgroundColor = [UIColor whiteColor];
    _imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
    _imageView.image = [UIImage imageNamed:@"splash.png"];
    [self addSubview:_imageView];
  }
  return self;
  
}


-(void)layoutSubviews{
  [super layoutSubviews];
  CGFloat height = [UIScreen mainScreen].bounds.size.width * 91 / 320;
  _imageView.frame = CGRectMake(0, ([UIScreen mainScreen].bounds.size.height - height  )/2  , [UIScreen mainScreen].bounds.size.width ,
                                
                                height
                                );
}

@end
