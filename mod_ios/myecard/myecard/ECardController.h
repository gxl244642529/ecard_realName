//
//  ECardController.h
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>

#import <ecardlib/ecardlib.h>

#import "ScanViewController.h"

///我的e通卡
@interface ECardController : DMViewController<IOnItemClickListener,UIActionSheetDelegate,SCanDelegate>

+(void)selectECard:(UIViewController*)parent delegate:(id<ECardSelectDelegate>)delegate;



@property (nonatomic,weak) id<ECardSelectDelegate> delegate;

@end
