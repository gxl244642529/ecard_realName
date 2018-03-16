//
//  ScrollCtrl.m
//  ecard
//
//  Created by randy ren on 15-1-21.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ScrollCtrl.h"


#define WIDTH 20
#define HEIGHT 2
#define SPACING 7

@interface ScrollCtrl()
{
    __weak UIView* _currentView;
    UIColor* _highColor;
}

@end

@implementation ScrollCtrl
-(void)dealloc{
    _highColor = NULL;
}


-(void)setItems:(NSInteger)items{
    if(items == self.subviews.count)return;
    NSInteger count = self.subviews.count;
    if(count < items){
        for(NSInteger i=count; i < items; ++i){
            UIView* view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
            view.backgroundColor =[UIColor darkGrayColor];
            [self addSubview:view];
        }

    }else{
        
        for(NSInteger i=count-1; i >= items; --i){
            UIView* view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT)];
            [view removeFromSuperview];
        }

    }
    
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger items = self.subviews.count;
    NSInteger len = items * WIDTH + (items-1)* SPACING;
    CGRect rect = self.frame;
    NSInteger pos = (rect.size.width - len)/2;
    for(int i=0; i < items; ++i){
        UIView* view = self.subviews[i];
        view.frame = CGRectMake(pos, 0, WIDTH, HEIGHT);
        pos +=SPACING+WIDTH;
    }
}

-(void)awakeFromNib{
  [super awakeFromNib];
    _highColor = [UIColor colorWithRed:(float)0xff/255 green:(float)0x4b/255 blue:(float)0x5a/255 alpha:1];
    NSInteger count = self.subviews.count;
    
    for(int i=0; i < count; ++i){
        UIView* view = self.subviews[i];
        view.backgroundColor =[UIColor darkGrayColor];
    }

}


-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        _highColor = [UIColor colorWithRed:(float)0xff/255 green:(float)0x4b/255 blue:(float)0x5a/255 alpha:1];
        
    }
    return self;
}

-(id)initWithItems:(NSInteger)items{
    
    if(self=[super init]){
        _highColor = [UIColor colorWithRed:(float)0xff/255 green:(float)0x4b/255 blue:(float)0x5a/255 alpha:1];
        int pos = 0;
        for(int i=0; i < items; ++i){
            UIView* view = [[UIView alloc]initWithFrame:CGRectMake(pos, 0, WIDTH, HEIGHT)];
            view.backgroundColor =[UIColor darkGrayColor];
            pos += SPACING+WIDTH;
            [self addSubview:view];
        }
        pos -= SPACING;
        self.frame = CGRectMake(0, 0, pos, 4);
    }
    return self;
}

-(void)setCurrentItem:(NSInteger)index{
    if(_currentView){
        _currentView.backgroundColor = [UIColor darkGrayColor];
    }
    
    UIView* view = self.subviews[index];
    view.backgroundColor = _highColor;
    _currentView = view;
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
