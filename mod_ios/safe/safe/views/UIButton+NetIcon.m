//
//  UIButton+NetIcon.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "UIButton+NetIcon.h"
#import <DMLib/DMLib.h>


static char* UIButtonNetIcon = "UIButtonNetIcon";

@interface NetIconJob : NSObject<DMJobDelegate>

@property (nonatomic,weak) UIButton* button;

@property (nonatomic,assign) NSInteger size;

@end

@implementation UIButton(NetIcon)

-(void)setIconSize:(NSInteger)size{
    NetIconJob* job = [[NetIconJob alloc]init];
    job.button = self;
    job.size = size;
    objc_setAssociatedObject(self, UIButtonNetIcon, job, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

-(void)load:(NSString*)url{
    NetIconJob* job = objc_getAssociatedObject(self, UIButtonNetIcon);
    [[DMJobManager sharedInstance]loadImage:url delegate:job];
}

@end


@implementation NetIconJob


-(void)jobSuccess:(DMHttpJob*)request{
    UIImage* image = request.data;
    image = [image aspectFill:CGSizeMake(_size, _size)];
    [_button setImage:image forState:UIControlStateNormal];
}


@end
