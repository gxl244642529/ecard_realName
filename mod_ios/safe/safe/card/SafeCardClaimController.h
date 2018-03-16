//
//  SafeCardClaimController.h
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "MySafeVo.h"
@interface SafeCardClaimController : DMViewController<DMSubmitDelegate>


@property (nonatomic,retain) MySafeVo* data;

@end
