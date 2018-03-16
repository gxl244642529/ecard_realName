//
//  SAddrCell.h
//  ecard
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <ecardlib/ecardlib.h>

#import "BaseTableCell.h"
@interface SAddrCell : BaseTableCell
@property (weak, nonatomic) IBOutlet UILabel *strName;
@property (weak, nonatomic) IBOutlet UILabel *addr;
@property (weak, nonatomic) IBOutlet UILabel *phone;
@property (weak, nonatomic) IBOutlet ItemView *btnDelete;
@property (weak, nonatomic) IBOutlet ItemView *btnEdit;

@end
