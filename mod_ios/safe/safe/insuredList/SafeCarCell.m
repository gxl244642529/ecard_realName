//
//  SafeCarCell.m
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeCarCell.h"

@implementation SafeCarCell



-(NSString*)validate:(NSMutableDictionary*)data{
    if([_carNo.text isEqualToString:@""]){
        return @"请输入车牌号";
    }
    
    if([_carFrame.text isEqualToString:@""]){
        
        return @"请输入车架号";
    }
    
    return nil;
    
    
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
