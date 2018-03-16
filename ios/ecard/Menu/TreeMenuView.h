//
//  TreeMenuView.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "MenuData.h"

#import "MenuView.h"


@interface TreeMenuData : MenuData



@property (nonatomic,retain) NSMutableArray* children;

@end

/**
 *  二级菜单
 */
@interface TreeMenuView : MenuView<IOnItemClickListener,IDataAdapterListener>


@end
