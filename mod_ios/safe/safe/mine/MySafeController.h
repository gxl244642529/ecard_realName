//
//  MySafeController.h
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import <DMLib/DMLib.h>
#import "MySafeVo.h"


@interface MySafeController : DMViewController<IOnItemClickListener,DMStateDataSource>

-(void)updateListView;

@end
