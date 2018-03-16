//
//  Toolbar.m
//  YingYingLiCai
//
//  Created by JianYe on 13-6-6.
//  Copyright (c) 2013年 YingYing. All rights reserved.
//

#import "Navbar.h"
#import "NavBarItem.h"
//static NSString* BackgroundImage = @"nav_bg_image.png";

static UIColor* _titleColor;
@interface Navbar()
{
    float bottomLineColor;
}
//@property (nonatomic,strong)NSNumber *stateBarStyle;
@end

@implementation Navbar
//@synthesize stateBarColor = _stateBarColor;
//@synthesize stateBarStyle = _stateBarStyle;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        bottomLineColor = (float)0xe5 / 255;
    }
    return self;
}



- (UIColor *)adjustColourBrightness:(UIColor *)color withFactor:(double)factor
{
    CGColorRef cgColour = [color CGColor];
	CGFloat *oldComponents = (CGFloat *)CGColorGetComponents(cgColour);
	CGFloat newComponents[4];
    
	NSInteger numComponents = CGColorGetNumberOfComponents(cgColour);
    
	switch (numComponents)
	{
		case 2:
		{
			//grayscale
			newComponents[0] = oldComponents[0]*factor;
			newComponents[1] = oldComponents[0]*factor;
			newComponents[2] = oldComponents[0]*factor;
			newComponents[3] = oldComponents[1];
			break;
		}
		case 4:
		{
			//RGBA
			newComponents[0] = oldComponents[0]*factor;
			newComponents[1] = oldComponents[1]*factor;
			newComponents[2] = oldComponents[2]*factor;
			newComponents[3] = oldComponents[3];
			break;
		}
	}
    
	CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
	CGColorRef newColor = CGColorCreate(colorSpace, newComponents);
	CGColorSpaceRelease(colorSpace);
    
	UIColor *retColor = [UIColor colorWithCGColor:newColor];
	CGColorRelease(newColor);
    
	return retColor;
}
+(void)titleColor:(UIColor*)titleColor titleTextColor:(UIColor*)titleTextColor{
    [NavBarButtonItem setTitleTextColor:titleTextColor];
    _titleColor = titleColor;
}
- (void)drawRect:(CGRect)rect
{
    // [[UIImage imageNamed:BackgroundImage] drawInRect:rect];
    
     [super drawRect:rect];
    
    
    UIColor *bottomColor = _titleColor;
    // UIColor *topColor = self.tintColor;
    
    
    // emulate the normal gradient tint
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGFloat locations[2] = { 0.0, 1.0 };
    CGColorSpaceRef myColorspace = CGColorSpaceCreateDeviceRGB();
    
    NSArray *colours = [NSArray arrayWithObjects:(__bridge id)bottomColor.CGColor, (__bridge id) bottomColor.CGColor, nil];
    CGGradientRef gradient = CGGradientCreateWithColors(myColorspace, (__bridge CFArrayRef)colours, locations);
    CGContextDrawLinearGradient(context, gradient, CGPointMake(0, 0), CGPointMake(0,self.frame.size.height), 0);
    CGGradientRelease(gradient);
    
    CGColorSpaceRelease(myColorspace);
    
    // top Line 1 px high across the very top of the bar slightly lighter colour than the top gradient color
    CGContextSetStrokeColorWithColor(context, [[self adjustColourBrightness:bottomColor withFactor:1.5] CGColor]);
    CGContextMoveToPoint(context, 0, 0);
    CGContextAddLineToPoint(context, self.frame.size.width, 0);
    CGContextStrokePath(context);
    
    // bottom line 1px across the bottom of the bar in black
    //CGContextSetRGBStrokeColor(context, 0, 0, 0, 1.0);
    CGContextSetRGBStrokeColor(context, bottomLineColor, bottomLineColor, bottomLineColor, 1.0);
    CGContextMoveToPoint(context, 0, self.frame.size.height);
    CGContextAddLineToPoint(context, self.frame.size.width, self.frame.size.height);
    CGContextStrokePath(context);
    
}


- (void)setNeedsLayout
{
    [super setNeedsLayout];
    
    self.barStyle = UIBarStyleBlackOpaque;
    UIView *view = [self viewWithTag:99900];
    if (!view) {
        view = [[UIView alloc] initWithFrame:CGRectMake(0, -20, self.bounds.size.width, 21)];
        view.backgroundColor = _titleColor;
        [self addSubview:view];
    }
    
    /**< 起到在IOS 7中navbar 和state bar 不 悬浮的作用*/
    self.translucent = NO;
    
   self.tintColor = [UIColor clearColor];
}

@end


