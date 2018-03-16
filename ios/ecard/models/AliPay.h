//
//  AliPay.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AbsPay.h"
#import "BasePayAction.h"

@interface AliPay : AbsPay
-(id)initWithModel:(ECardRPayModel *)model controller:(UIViewController *)parent action:(id<IPayAction>)action;
@end
