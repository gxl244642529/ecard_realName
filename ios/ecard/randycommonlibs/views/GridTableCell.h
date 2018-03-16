//
//  GridTableCell.h
//  randycommonlib
//
//  Created by randy ren on 14-10-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import <ecardlib/ecardlib.h>

@interface GridTableCell : UIView

@property (nonatomic) NSInteger index;

@property (nonatomic,retain) TouchableView* contentView;

@end
