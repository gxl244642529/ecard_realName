//
//  TreeMenuView.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "TreeMenuView.h"
#import "MainMenuItem.h"
#import "SubMenuItem.h"





@implementation TreeMenuData

-(void)dealloc{
    
    self.children = NULL;
}

@end


@interface TreeMenuView()
{
    UITableView* _right;
    
    __weak TreeMenuData* _mainData;
    __weak MenuData* _subData;
    
    TableDataAdapter* _rightAdapter;
}
@end

@implementation TreeMenuView

-(void)dealloc{
    _right = NULL;
    _rightAdapter = NULL;
}

-(void)onSelectItem:(TreeMenuData *)data{
    [super onSelectItem:data];
    if(data.children){
        [_rightAdapter setData:data.children];
    }
}

-(void)initImpl:(CGRect)frame{
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width/2, frame.size.height)];
    [self addSubview:_tableView];
    _tableView.rowHeight = MENU_ITEM_HEIGHT;
    
    _right =[[UITableView alloc]initWithFrame:CGRectMake(frame.size.width/2, 0, frame.size.width/2, frame.size.height)];
    [self addSubview:_right];
    _right.rowHeight = MENU_ITEM_HEIGHT;
    
    
    _adapter = [[TableDataAdapter alloc]init];
    [_adapter setScrollView:_tableView];
    [_adapter setOnItemClickListener:self];
    [_adapter registerCell:@"MainMenuItem" height:_tableView.rowHeight bundle:nil];
    [_adapter setListener:self];
    
    _rightAdapter = [[TableDataAdapter alloc]init];
    [_rightAdapter setScrollView:_right];
    [_rightAdapter setOnItemClickListener:self];
    [_rightAdapter registerCell:@"SubMenuItem" height:_tableView.rowHeight bundle:nil];
    [_rightAdapter setListener:self];
    
    
    
    
    [_right setBackgroundColor:[UIColor colorWithWhite:(float)0xee/255 alpha:1]];
    
    
    [_tableView setBackgroundColor:[UIColor colorWithWhite:(float)0xf7/255 alpha:1]];
    UIImage *backgroundImage = [UIImage imageNamed:@"left_bg.png"];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(1, 1,1, 1)];
    UIImageView* svRect = [[UIImageView alloc] initWithImage:backgroundImage];
    [_tableView setBackgroundView:svRect];

}

-(void)onInitializeView:(UIView*)parent cell:(UIView*)cell data:(id)data index:(NSInteger)index{
    if(parent==_tableView){
        TreeMenuData* menuData = (TreeMenuData*)data;
        MainMenuItem* item = (MainMenuItem*)cell;
        [item setData:menuData];
        item.title.text = menuData.title;
    }else{
        
        SubMenuItem* item = (SubMenuItem*)cell;
        [item setData:data];
        
    }
}
-(void)onItemClick:(UIView*)parent data:(MenuData*)data index:(NSInteger)index{
    if(parent==_tableView){
        TreeMenuData* menuData = (TreeMenuData*)data;
        if(menuData!=_mainData){
            _mainData = menuData;
            if(menuData.children){
                [_rightAdapter setData:menuData.children];
                return;
            }else{
                [_rightAdapter setData:@[]];
                menuData.selected = YES;
                [_delegate onClickMenu:menuData index:0];
                return;
            }
        }else{
            return;
        }
    }else{
        
        if(_subData){
            _subData.selected = FALSE;
        }
        data.selected = FALSE;
        _subData = data;
    }
    
    
    [_delegate onClickMenu:data index:0];
    NSLog(@"%@",data.title);
}

-(void)onSetData:(NSArray*)array{
    
    
    _tableView.frame = CGRectMake(0, 0, self.frame.size.width/2, MIN(_maxHeight, array.count * MENU_ITEM_HEIGHT));
    _right.frame = CGRectMake(self.frame.size.width/2, 0, self.frame.size.width/2, MIN(_maxHeight, array.count * MENU_ITEM_HEIGHT));

}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
