//
//  TestViewController.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>

#import "SafeUtil.h"
#import "InsuredContactView.h"
#import "DMOrderConfirmController.h"

@interface SafeBuyInsuredController : DMOrderConfirmController<UITableViewDataSource,UITableViewDelegate,DMCheckDelegate,DMJobDelegate,ContactSelectDelegate,IDataAdapterListener>


@property (nonatomic,retain) SafeVo* data;
//@property (nonatomic,weak) SafeDetailVo* detailVo;

@end
