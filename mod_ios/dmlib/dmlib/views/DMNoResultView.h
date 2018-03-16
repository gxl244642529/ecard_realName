//
//  DMNoResultView.h
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMNetState.h"
@interface DMNoResultView : UIView<LoadingState>
@property (nonatomic,weak) id<LoadingStateDelegate> loadingDelegate;
@end
