//
//  DiyFilterView.h
//  ecard
//
//  Created by randy ren on 15-1-31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TouchableView.h"
#import "ISelectable.h"
@interface DiyFilterView : TouchableView<ISelectable>


-(void)setImage:(UIImage*)image;
-(void)setLabel:(NSString*)label;
@end
