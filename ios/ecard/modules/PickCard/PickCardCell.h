//
//  PickCardCell.h
//  ecard
//
//  Created by randy ren on 14-5-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "BaseTableCell.h"

@interface PickCardCell : BaseTableCell
@property (weak, nonatomic) IBOutlet UILabel *cardNumber;
@property (weak, nonatomic) IBOutlet UILabel *time;

@end
