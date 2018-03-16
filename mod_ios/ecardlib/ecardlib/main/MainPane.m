//
//  MainPane.m
//  ecard
//
//  Created by randy ren on 16/1/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MainPane.h"
#import "ImageUtil.h"
#import <DMLib/dmlib.h>

@interface MainPane()
{
    UIImageView* _imageView;
    UIImageView* _iconImageView;
    NSMutableArray* _buttons;
    CGFloat _panScale;
    CGFloat _iconScale;
    int _panWidth;
}

@end


@implementation MainPane

-(void)dealloc{
    _buttons = nil;
    _imageView = nil;
}
-(void)showButtons{
    //展示按钮
    _imageView.hidden = YES;
    _iconImageView.hidden = YES;
    _icon.hidden = NO;
    for(UIView* view in _buttons){
        view.hidden = NO;
    }

    
}
-(void)awakeFromNib{
  [super awakeFromNib];
    _panWidth = 255;//SCREEN_WIDTH / 320 * 255;
    
    [self findWidth].constant = _panWidth;
    [self findHeight].constant = _panWidth;
    
    self.bg = [self viewWithTag:1];
    self.icon = [self viewWithTag:2];
    _buttons = [[NSMutableArray alloc]init];
    for(UIView* view in self.subviews){
        if(view.tag == 0){
            [_buttons addObject:view];
        }
    }
    
    
    _panScale =(float)62 / _panWidth;
    _iconScale = (float)55 / (100 * SCREEN_WIDTH / 320);
    [_bg findHeight].constant = _panWidth;
    [_bg findWidth].constant = _panWidth;
}


-(void)hideButtons{
    //隐藏按钮
    _imageView.hidden = NO;
    _iconImageView.hidden = NO;
    _icon.hidden = YES;
    for(UIView* view in _buttons){
        view.hidden = YES;
    }
}

-(void)makeSmall{
    _bg.transform = CGAffineTransformMakeScale(_panScale, _panScale);
    _imageView.transform =CGAffineTransformMakeScale(_panScale,_panScale);
    _iconImageView.transform =CGAffineTransformMakeScale(_iconScale, _iconScale);
}
-(void)makeBig{
    _iconImageView.transform =CGAffineTransformMakeScale(1, 1);
    _bg.transform = CGAffineTransformMakeScale(1, 1);
    _imageView.transform =CGAffineTransformMakeScale(1, 1);
}
-(void)takeSnap{
    NSLog(@"%d",(int)self.bounds.size.width);
    if((int)self.bounds.size.width != _panWidth){
        return;
    }
    
    if(_imageView)return;
    
    UIImage* iconImage =  [UIImage imageNamed:@"ecardlibbundle.bundle/ic_main_pane_btn.png"];
    
    _bg.hidden = YES;
    _icon.hidden = YES;
    //拍照
    UIImage* image = [ImageUtil imageFromView:self];
    _bg.hidden = NO;
    _icon.hidden = NO;
    //拍照
    _imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, _panWidth, _panWidth)];
    [self insertSubview:_imageView aboveSubview:_bg];
    _imageView.image = image;
    
    _iconImageView = [[UIImageView alloc]initWithFrame:_icon.frame];
    [self addSubview:_iconImageView];
    _iconImageView.image = iconImage;
    
}

-(void)layoutSubviews{
    [super layoutSubviews];
    [self takeSnap];
    
}

@end
