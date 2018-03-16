//
//  DMTabContainer.h
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMTabDelegate.h"

//可切换的容器
IB_DESIGNABLE
@interface DMTabContainer : UIView<DMTabDelegate>

//视图的名字,根据视图名字创建视图
@property (nonatomic,copy) IBInspectable NSString* viewNames;

@end
