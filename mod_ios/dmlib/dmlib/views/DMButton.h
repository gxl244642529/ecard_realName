//
//  DMButton.h
//  libs
//
//  Created by randy ren on 16/1/12.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

//逻辑按钮,可以是任意视图

IB_DESIGNABLE
@interface DMButton : UIButton


//图标和文字的距离
@property (nonatomic,assign) IBInspectable NSInteger padding;


@end
