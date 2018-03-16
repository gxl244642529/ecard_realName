//
//  ToggleButton.h
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <ecardlib/ecardlib.h>

@interface ToggleButton : TouchableView<ISelectable>



/**
 *  设置文字颜色
 *
 *  @param color       <#color description#>
 *  @param highlighted <#highlighted description#>
 */
-(void)setTextColor:(UIColor*)color highlighted:(UIColor*)highlighted;

-(NSString*)label;

@end
