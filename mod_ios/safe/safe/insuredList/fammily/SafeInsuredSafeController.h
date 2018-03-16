//
//  SafeInsuredSafeController.h
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "SafeTypeVo.h"
@interface SafeInsuredSafeController : DMViewController<IOnItemClickListener>
@property (nonatomic,retain) SafeTypeVo* data;
@end
