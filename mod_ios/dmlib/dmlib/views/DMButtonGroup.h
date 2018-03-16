//
//  DMButtonGroup.h
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMTabDelegate.h"
#import "DMValue.h"
//只要是一组按钮都可以使用这个

IB_DESIGNABLE
@interface DMButtonGroup : UIView<DMTab,DMTabDelegate,DMValue>

//如果对方可以切换,则可以使用这个
@property (nonatomic,assign) IBInspectable NSInteger targetTabTag;
//如果内部有tab,如表示进度条的,则可以使用这个
@property (nonatomic,assign) IBInspectable NSInteger innerTabTag;
//是否可以选择的
@property (nonatomic,assign) IBInspectable BOOL selectable;

//初始化选择
@property (nonatomic,assign) IBInspectable NSInteger initSelected;

//所有值
@property (nonatomic,assign) IBInspectable NSString* values;

@property (nonatomic,weak) id<DMTabDelegate> tabDelegate;



@end
