//
//  ItemView.h
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "TouchableView.h"

@interface ItemView :TouchableView
{
    UIColor* _itemNormalColor;
    UIColor* _highColor;
}

-(void)setHighColor:(UIColor*)highColor;
-(void)setNormalColor:(UIColor*)normalColor;

@end
