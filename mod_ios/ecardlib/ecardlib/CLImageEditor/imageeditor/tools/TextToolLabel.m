//
//  TextToolLabel.m
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TextToolLabel.h"

@interface TextToolLabel()
{
    NSString* _realText;
}

@end

@implementation TextToolLabel

-(void)dealloc{
    _realText  =NULL;
}
-(NSString*)realText{
    return _realText;
}
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
    }
    return self;
}
-(void)setRealText:(NSString*)realText{
    [super setText:realText];
    _realText = realText;
    CGRect rect = self.frame;
    self.frame = CGRectMake(rect.origin.x, rect.origin.y, 0, 0);
    [self sizeToFit];
}
-(void)setFontSize:(float)size{
    self.font = [UIFont fontWithName:@"HelveticaNeue" size:size];
    CGRect rect = self.frame;
    self.frame = CGRectMake(rect.origin.x, rect.origin.y, 0, 0);
    [self sizeToFit];
}
-(id)init{
    if(self = [super init]){
        self.text = @"单击输入文字";
        self.font = [UIFont fontWithName:@"HelveticaNeue" size:12];
        self.textColor = [UIColor whiteColor];
        self.numberOfLines = 0;
        self.backgroundColor = [UIColor clearColor];
        self.userInteractionEnabled = YES;
        self.layer.borderColor = [UIColor redColor].CGColor;
    }
    return self;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.delegate onStartDrag:self touch:[touches anyObject]];
}

- (void)drawRect:(CGRect)rect
{
    [super drawRect:rect];
    //外面家边框
    //画线
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetLineCap(context, kCGLineCapRound);
    CGContextSetLineWidth(context, 2.0);  //线宽
    CGContextSetAllowsAntialiasing(context, YES);
    CGContextSetRGBStrokeColor(context, 1.0, 0.0, 0.0, 1.0);  //颜色
    CGContextBeginPath(context);
    CGContextMoveToPoint(context, 0, 0);  //起点坐标
    CGContextAddLineToPoint(context, rect.size.width, 0);   //终点坐标
    CGContextAddLineToPoint(context, rect.size.width, rect.size.height);   //终点坐标
    CGContextAddLineToPoint(context, 0, rect.size.height);   //终点坐标
    CGContextAddLineToPoint(context, 0, 0);   //终点坐标
    CGContextStrokePath(context);
    UIGraphicsEndImageContext();
}


@end
