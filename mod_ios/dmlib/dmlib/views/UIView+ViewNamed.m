//
//  UIView+ViewNamed.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "UIView+ViewNamed.h"
#import <objc/runtime.h>


char* UIViewViewNamed = "UIViewViewNamed";



@implementation UIView(ViewNamed)

-(void)setViewName:(NSString *)viewName{
    objc_setAssociatedObject(self, UIViewViewNamed, viewName, OBJC_ASSOCIATION_RETAIN);
}
-(void)removeAllSubviews{
     [self.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
}
-(NSString*)viewName{
    return objc_getAssociatedObject(self, UIViewViewNamed);
}

-(__kindof UIViewController*)findViewController{
    id target=self;
    while (target) {
        target = ((UIResponder *)target).nextResponder;
        if ([target isKindOfClass:[UIViewController class]]) {
            break;
        }
    }
    return target;
}

-(UIImage*)takeSnapshot{
    UIGraphicsBeginImageContext(self.frame.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [self.layer renderInContext:context];
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return theImage;
}

-(NSLayoutConstraint*)findWidth{
    for (NSLayoutConstraint* c in self.constraints) {
        if(c.firstItem == self && c.firstAttribute == NSLayoutAttributeWidth){
            return c;
            break;
        }
    }
    return nil;
}
-(NSLayoutConstraint*)findHeight{
    for (NSLayoutConstraint* c in self.constraints) {
        if(c.firstItem == self && c.firstAttribute == NSLayoutAttributeHeight){
            return c;
            break;
        }
    }
    return nil;
}

-(__kindof UIView*)findFirstResponsder{
    if (self.isFirstResponder) {
        return self;
    }
    for (UIView *subView in self.subviews) {
        UIView *firstResponder = [subView findFirstResponsder];
        if (firstResponder != nil) {
            return firstResponder;
        }
    }
    return nil;
}

- (CGFloat)top
{
    return self.frame.origin.y;
}

- (void)setTop:(CGFloat)y
{
    CGRect frame = self.frame;
    frame.origin.y = y;
    self.frame = frame;
}

- (CGFloat)right
{
    return self.frame.origin.x + self.frame.size.width;
}

- (void)setRight:(CGFloat)right
{
    CGRect frame = self.frame;
    frame.origin.x = right - self.frame.size.width;
    self.frame = frame;
}

- (CGFloat)bottom
{
    return self.frame.origin.y + self.frame.size.height;
}

- (void)setBottom:(CGFloat)bottom
{
    CGRect frame = self.frame;
    frame.origin.y = bottom - self.frame.size.height;
    self.frame = frame;
}

- (CGFloat)left
{
    return self.frame.origin.x;
}

- (void)setLeft:(CGFloat)x
{
    CGRect frame = self.frame;
    frame.origin.x = x;
    self.frame = frame;
}

- (CGFloat)width
{
    return self.frame.size.width;
}

- (void)setWidth:(CGFloat)width
{
    CGRect frame = self.frame;
    frame.size.width = width;
    self.frame = frame;
}

- (CGFloat)height
{
    return self.frame.size.height;
}

- (void)setHeight:(CGFloat)height
{
    CGRect frame = self.frame;
    frame.size.height = height;
    self.frame = frame;
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
