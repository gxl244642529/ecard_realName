//
//  ColorSelectView.m
//  ecard
//
//  Created by randy ren on 15-2-10.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ColorSelectView.h"
#define WIDTH 10
@interface ColorSelectView ()
{
    NSMutableArray* _arr;
    BOOL _drag;
    int _lastIndex;
}

@end

@implementation ColorSelectView

-(void)dealloc{
    _arr = NULL;
}


-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    UITouch* touch = [touches anyObject];
    _drag = YES;
    CGPoint pt = [touch locationInView:self];
    int index = pt.x / self.frame.size.width * 27;
    if(index < _arr.count){
        [self.delegate onColorSelected:_arr[index]];
         _lastIndex = index;
    }
    
   
}

-(void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event{
    
    if(_drag){
        //改变颜色
        //下标
        UITouch* touch = [touches anyObject];
        CGPoint pt = [touch locationInView:self];
        int index = pt.x / self.frame.size.width * 27;
        if(index < _arr.count && index!=_lastIndex){
            [self.delegate onColorSelected:_arr[index]];
            _lastIndex = index;
        }
    }
}
-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
    _drag = NO;
}
-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    _drag = NO;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    _arr = [[NSMutableArray alloc]init];
    float eve = 255/3;
    for(int r=0; r < 3; ++r){
        for(int g = 0; g < 3; ++g){
            for(int b = 0; b < 3; ++b){
                float fr = eve * b / 255;
                float fg = eve * r / 255;
                float fb = eve * g / 255;
                [_arr addObject:[UIColor colorWithRed:fr green:fg blue:fb alpha:1]];
            }
        }
    }

}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        
    }
    return self;
}

//r g  b
// r b g
// b r g
// b g r
// g r b
// g b r

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
    // Initialization code
    CGContextRef context = UIGraphicsGetCurrentContext();
    for(int r=0; r < 3; ++r){
        for(int g = 0; g < 3; ++g){
            for(int b = 0; b < 3; ++b){
                int index =  (r*3+g)*3 + b;
                UIColor* color = _arr[index];
                CGContextSetFillColorWithColor(context, color.CGColor);
                CGContextFillRect(context, CGRectMake(10*index, 8, WIDTH, 15));
            }
        }
    }
}


@end
