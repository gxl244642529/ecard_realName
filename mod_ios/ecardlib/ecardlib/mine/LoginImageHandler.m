//
//  LoginImageHandler.m
//  ecard
//
//  Created by randy ren on 15/9/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "LoginImageHandler.h"
#import <objc/runtime.h>
#import "UserImageModel.h"


#import <ecardlib/ecardlib.h>

@interface LoginImageHandler()
{
    NSString* _key;
    UserImageModel* _model;
  
  __weak UIButton* _button;
}

@end

@implementation LoginImageHandler

-(void)dealloc{
    _key = nil;
    _model = nil;
}

-(id)initWithDefaultImage:(NSString *)defaultImage localImage:localImage key:(NSString*)key  type:(int)type  cornor:(BOOL)cornor{
    if(self = [super initWithDefaultImage:defaultImage localImage:localImage type:type cornor:cornor]){
        _key = key;
        _model = [[UserImageModel alloc]init];
    }
    return self;
}


-(void)jobSuccess:(DMHttpJob*)request{
  UIImage* image = request.data;
  
    
  UIImageView* imageView =   [[UIImageView alloc]initWithFrame:CGRectMake(0,0, 100, 100)];
    
    [_button.superview addSubview:imageView];
    [imageView setImage:image];
    
    
  [_button setImage:image forState:UIControlStateNormal];
    _button.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 0);
    
    [[_button imageView] setContentMode:UIViewContentModeScaleAspectFill];
    
    
    _button.contentHorizontalAlignment= UIControlContentHorizontalAlignmentFill;
    
    _button.contentVerticalAlignment = UIControlContentVerticalAlignmentFill;
}


-(void)handle:(id)view{
  ECardUserInfo* userInfo = [DMAccount current];
    NSString* imageUrl = [userInfo valueForKey:_key];
    if(imageUrl){
        if([imageUrl hasPrefix:@"/uploads"]){
            imageUrl = [DMServers formatUrl:0 url:imageUrl];
        }
      if([view isKindOfClass:[UIImageView class]]){
       
        [[DMJobManager sharedInstance]loadImage:imageUrl image:view];
      }else{
        _button = view;
         [[DMJobManager sharedInstance]loadImage:imageUrl delegate:self];
      }
    }else{
        //设置本地图片
        [super handle:view];
    }
}


-(void)onSaveImage:(UIImage *)image{
    //保存图片
    [_model setUserImage:image type:_type];
}

@end
