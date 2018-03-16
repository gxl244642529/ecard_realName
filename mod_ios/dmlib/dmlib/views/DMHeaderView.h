//
//  DMHeaderView.h
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DMHeaderView : UIView

@property (nonatomic,weak) UITableView* tableView;
-(id)initWithFrame:(CGRect)frame contentView:(UIView*)contentView;

@end
