//
//  SCardFavCell.h
//  ecard
//
//  Created by randy ren on 15/4/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GridTableCell.h"
@interface SCardFavCell : GridTableCell
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UIButton *close;

@end
