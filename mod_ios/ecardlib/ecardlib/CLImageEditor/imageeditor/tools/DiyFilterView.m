//
//  DiyFilterView.m
//  ecard
//
//  Created by randy ren on 15-1-31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DiyFilterView.h"
#import "ViewUtil.h"
#import "EditorUtil.h"
@interface DiyFilterView()
{
    UIImageView* _imageView;
    UILabel* _label;
}

@end

@implementation DiyFilterView

-(void)dealloc{
    _imageView = NULL;
    _label = NULL;
}

-(void)setSelected:(BOOL)selected{
    if(selected){
        self.backgroundColor = [[UIColor lightGrayColor]colorWithAlphaComponent:0.5];
    }else{
        self.backgroundColor = [UIColor clearColor];
    }
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        float h = 60 * [EditorUtil rate];
        _imageView = [[UIImageView alloc]initWithFrame:CGRectMake(5,5, 60, h)];
        [self addSubview:_imageView];
        _imageView.clipsToBounds = YES;
        _imageView.layer.cornerRadius = 5;
        _imageView.layer.masksToBounds = YES;
        _imageView.contentMode = UIViewContentModeScaleAspectFill;
        
        _label = [[UILabel alloc] initWithFrame:CGRectMake(0, h + 5,frame.size.width,15)];
        _label.backgroundColor = [UIColor clearColor];
        _label.font = [UIFont systemFontOfSize:10];
        _label.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_label];

    }
    return self;
}
-(void)setImage:(UIImage*)image{
    _imageView.image = image;
}
-(void)setLabel:(NSString*)label{
    _label.text = label;
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
