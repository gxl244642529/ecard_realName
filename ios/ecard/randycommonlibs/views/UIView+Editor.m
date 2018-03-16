//
//  UIView+Editor.m
//  ecard
//
//  Created by randy ren on 15/12/29.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "UIView+Editor.h"

@implementation UIView(Editor)


-(void)setCornerRadius:(NSInteger)cornerRadius{
    self.layer.cornerRadius = cornerRadius;
    self.layer.masksToBounds = cornerRadius > 0;
}

-(NSInteger)cornerRadius{
    return self.layer.cornerRadius;
}

@end
