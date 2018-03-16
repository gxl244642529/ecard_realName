//
//  SafeCardBuyController.h
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "DMOrderConfirmController.h"
#import "SafeVo.h"

@interface SafeCardBuyController : DMOrderConfirmController<DMSubmitDelegate,UIAlertViewDelegate>


@property (nonatomic,retain) SafeVo* data;

@end
