//
//  DMScrollTabContainer.h
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "DMTabDelegate.h"

@interface DMScrollTabContainer : UIScrollView<DMTabDelegate,UIScrollViewDelegate>
//视图的名字,根据视图名字创建视图
@property (nonatomic,copy) IBInspectable NSString* viewNames;

@property (nonatomic,weak) id<DMTabDelegate> tabDelegate;

@end
