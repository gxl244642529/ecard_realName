//
//  CashierController.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"

#import "RPay.h"
@interface CashierController : MyViewController<IDataAdapterListener,IOnItemClickListener>


-(id)initWithModel:(ECardRPayModel*)model;

@end
