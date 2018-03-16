//
//  SafeListController.h
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>

#import "SafeTypeVo.h"
@interface SafeListController : DMViewController<IOnItemClickListener>


@property (nonatomic,retain) SafeTypeVo* data;


@end
