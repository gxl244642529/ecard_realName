//
//  SafeInsuredDetailController.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "SafeUtil.h"
#import "SafeCounterView.h"

@interface SafeInsuredDetailController :DMViewController<SafeCounterViewDelegate>

@property (nonatomic,retain) SafeVo* data;



@end
