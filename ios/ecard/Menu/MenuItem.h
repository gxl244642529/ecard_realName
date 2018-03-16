//
//  MenuItem.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MenuData.h"

@interface MenuItem : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *title;

-(void)setData:(MenuData*)data;

@end
