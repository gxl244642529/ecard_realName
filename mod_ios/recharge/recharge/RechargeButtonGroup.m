//
//  RechargeButtonGroup.m
//  ecard
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeButtonGroup.h"


@interface RechargeButtonGroup()
{
    BOOL _enabled;
}

@end

@implementation RechargeButtonGroup

-(void)awakeFromNib{
    [super awakeFromNib];
    _enabled = YES;
}

-(void)setEnabled:(BOOL)enabled{
    if(enabled != _enabled){
        _enabled = enabled;
        for(UIButton* view in self.subviews){
            view.enabled = enabled;
        }
    }
    
}

-(BOOL)enabled{
    return _enabled;
}



@end
