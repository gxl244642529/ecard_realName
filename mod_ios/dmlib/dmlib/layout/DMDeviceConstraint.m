//
//  DMDeviceConstraint.m
//  ecard
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMDeviceConstraint.h"



@implementation DMDeviceConstraint
+(BOOL)isLongScreen{
    CGSize size = [UIScreen mainScreen].bounds.size;
    return (size.height / size.width) > 1.5;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    if([DMDeviceConstraint isLongScreen]){
        self.constant = [UIScreen mainScreen].bounds.size.width * _longRate;
    }else{
         self.constant = [UIScreen mainScreen].bounds.size.width * _shortRate;
    }
    
}

@end
