//
//  MenuView.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "MenuView.h"
#import "MenuItem.h"

const CGFloat MENU_ITEM_HEIGHT = 42;


@interface MenuView()
{
    
}

@end

@implementation MenuView
-(void)dealloc{
    _tableView = NULL;
    _adapter = NULL;
}

-(void)setDelegate:(NSObject<MenuViewDelegate>*)delegate{
    _delegate = delegate;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self initImpl:frame];
        _maxHeight = floor([UIScreen mainScreen].bounds.size.height * 0.8 / MENU_ITEM_HEIGHT) * MENU_ITEM_HEIGHT;
        
        [self setBackgroundColor:[UIColor redColor]];
    }
    return self;
}
-(void)initImpl:(CGRect)frame{
    
    _tableView = [[UITableView alloc]initWithFrame:self.bounds];
    _tableView.rowHeight = MENU_ITEM_HEIGHT;
    [self addSubview:_tableView];
    
    
    
    _adapter = [[TableDataAdapter alloc]init];
    [_adapter setScrollView:_tableView];
    [_adapter setOnItemClickListener:self];
    [_adapter registerCell:@"MenuItem" height:_tableView.rowHeight bundle:nil];
    [_adapter setListener:self];
    
    [_tableView setBackgroundColor:[UIColor colorWithWhite:(float)0xf7/255 alpha:1]];
    
    //maxheigh
    
   
    
    
}

-(void)onInitializeView:(UIView*)parent cell:(MenuItem*)cell data:(MenuData*)data index:(NSInteger)index{
    cell.title.text =data.title;
    [cell setData:data];
}
-(void)onItemClick:(UIView*)parent data:(id)data index:(NSInteger)index{
    [_delegate onClickMenu:data index:self.index];
}
-(void)setData:(NSArray*)array{
    [_adapter setData:array];
    NSInteger count = array.count;
    for (NSInteger i=0 ; i < count; ++i) {
        MenuData* data = array[i];
        if(data.selected){
            
            [_tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:0] animated:NO scrollPosition:UITableViewScrollPositionMiddle];
            [self onSelectItem:data];
            //
            break;
        }
    }
    
    [self onSetData:array];
     self.frame = CGRectMake(0, self.frame.origin.y, self.frame.size.width, MIN(_maxHeight, array.count * MENU_ITEM_HEIGHT));
}

-(void)onSetData:(NSArray*)array{
    _tableView.frame = CGRectMake(0, 0, self.frame.size.width, MIN(_maxHeight, array.count * MENU_ITEM_HEIGHT));
   

}
-(void)onSelectItem:(MenuData*)data{
    [_delegate onClickMenu:data index:self.index];
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
