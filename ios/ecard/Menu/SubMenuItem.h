//
//  SubMenuItem.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MenuData.h"
@interface SubMenuItem : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *title;
-(void)setData:(MenuData*)data;
@end
