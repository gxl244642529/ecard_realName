//
//  TouchableImageView.m
//  ecard
//
//  Created by randy ren on 14-7-17.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "TouchableImageView.h"

@interface TouchableImageView(){
}

@end

@implementation TouchableImageView
@synthesize imageView;
@synthesize image;
-(void)dealloc{
    self.imageView = NULL;
}
-(void)awakeFromNib{
  [super awakeFromNib];
    self.imageView = [[UIImageView alloc]initWithFrame:self.bounds];
    [self addSubview:self.imageView];
}
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.imageView = [[UIImageView alloc]initWithFrame:self.bounds];
        [self addSubview:self.imageView];
    }
    return self;
}
-(void)setImage:(UIImage*)img{
    self.imageView.image = img;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
