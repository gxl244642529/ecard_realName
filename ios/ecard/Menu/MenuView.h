//
//  MenuView.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/dmlib.h>
#import "MenuData.h"



/**
 菜单项的大小,所以最大大小为这个大小的整数倍
 */
extern const CGFloat MENU_ITEM_HEIGHT;




@protocol MenuViewDelegate <NSObject>
-(void)onClickMenu:(MenuData*)menuData index:(NSInteger)index;
@end

@interface MenuView : UIView<IOnItemClickListener,IDataAdapterListener>
{
    UITableView* _tableView;
    TableDataAdapter* _adapter;
    __weak NSObject<MenuViewDelegate>* _delegate;
    CGFloat _maxHeight;
}

@property (nonatomic) NSInteger index;


-(void)setData:(NSArray*)array;

-(void)setDelegate:(NSObject<MenuViewDelegate>*)delegate;
-(void)initImpl:(CGRect)frame;

-(void)onSelectItem:(MenuData*)data;

@end
