//
//  TabPaneDelegate.h
//  ecard
//
//  Created by randy ren on 15/10/23.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#ifndef TabPaneDelegate_h
#define TabPaneDelegate_h


@protocol TabPaneViewDelegate <NSObject>

-(void)tabDidSelected:(NSInteger)index first:(BOOL)first;

@end

@protocol TabPaneViewDelegate2 <TabPaneViewDelegate>

-(UIView*)tabInitItem:(NSInteger)index frame:(CGRect)frame;

@end


#endif /* TabPaneDelegate_h */
