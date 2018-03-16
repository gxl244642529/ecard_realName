//
//  SafeDetailInfoView.m
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeDetailInfoView.h"

@implementation SafeDetailInfoView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(NSString*)validate{
    
    NSString* ret = [super validate];
    if(ret)return ret;
    
    //看有没有选择
    if(![_relatonView val]){
        
        return @"请选择被保险人与投保人的关系";
    }
    
    return nil;
}
-(void)getInsured:(SafeContact*)dic{
    [super getInsured:dic];
    dic.relation = [[_relatonView val]integerValue];
}
@end
