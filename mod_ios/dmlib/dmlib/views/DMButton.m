//
//  DMButton.m
//  libs
//
//  Created by randy ren on 16/1/12.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMButton.h"

@implementation DMButton

-(void)layoutSubviews{
    [super layoutSubviews];
    
    [self.titleLabel sizeToFit];
    CGRect frame = self.bounds;
    CGPoint point = CGPointMake(frame.size.width/2, frame.size.height/2);
    //计算文字大小
    CGSize tsize = self.titleLabel.frame.size;
    CGSize isize = self.imageView.frame.size;
    
    //图片在文字的下面
   NSInteger total = tsize.height + _padding + isize.height;
    NSInteger start = (frame.size.height - total)/2;
    
    self.imageView.center = CGPointMake(point.x , start + isize.height/2);
    self.titleLabel.center = CGPointMake(point.x, start+total - tsize.height/2);
   
}

@end
