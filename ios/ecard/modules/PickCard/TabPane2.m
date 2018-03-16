//
//  TabPane2.m
//  ecard
//
//  Created by randy ren on 15/4/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TabPane2.h"
#import "CommonMacro.h"

@interface TabPane2 ()
{
    NSInteger _last;
    NSInteger _itemCount;
    ViewPager* _scrollView;
    NSMutableSet* _set;
}

@end

@implementation TabPane2

-(void)dealloc{
    _scrollView = nil;
    _set = nil;
}
-(void)setCurrentIndex:(NSInteger)index{
    [self onSel:index];
}
-(void)setTitles:(NSArray*)arr{
    NSArray* children = [self viewWithTag:999].subviews;
    for(NSInteger i=0 , count = children.count; i < count; ++i){
        
        UIButton* btn = children[i];
        NSString* title = arr[i];
        
        [btn setTitle:title forState:UIControlStateNormal];
        [btn setTitle:title forState:UIControlStateHighlighted];
        [btn setTitle:title forState:UIControlStateSelected];
        
    }
}
-(void)awakeFromNib{
  [super awakeFromNib];
  
    _scrollView.pagingEnabled = YES;
    _scrollView.bounces = NO;
    
    _last = -1;
    _set = [[NSMutableSet alloc]init];
    UIView* container = [self viewWithTag:999];
    _scrollView = (ViewPager*)[self viewWithTag:1000];
    NSArray* children = container.subviews;
    int index = 0;
    for (UIButton* btn in children) {
        btn.tag = index ++;
        Control_AddTarget(btn, onButton);
    }
   
    _itemCount = index;
}





/**
 视图数量
 */
-(NSInteger)getPageCount{
    return _itemCount;
}
/**
 创建视图
 */
-(UIView*)instantiateItem:(NSInteger)index parent:(UIView*)parent frame:(CGRect)frame{
    return [self.delegate tabInitItem:index frame:frame];
}
-(void)setFrame:(CGRect)frame{
    [super setFrame:frame];
 
}

-(void)layoutSubviews{
    [super layoutSubviews];
    CGRect frame = self.bounds;
    _scrollView.frame = CGRectMake(0, _scrollView.frame.origin.y, [UIScreen mainScreen].bounds.size.width, self.frame.size.height - _scrollView.frame.origin.y);
    
    _scrollView.contentSize = CGSizeMake(frame.size.width*_itemCount, 0);
    
}

-(void)setDelegate:(id<TabPaneViewDelegate2>)delegate{
    _delegate = delegate;
    [_scrollView setDataSource:self];
    [self onSel:0];
}

-(void)onSel:(NSInteger)index{
    if(index != _last){
        NSArray* arr = [self viewWithTag:999].subviews;
        UIButton* btn;
        if(_last>=0){
            btn=arr[_last];
            btn.selected = NO;
        }
        btn = arr[index];
        btn.selected = YES;
        if(![_set containsObject:btn]){
            [_set addObject:btn];
            [self.delegate tabDidSelected:index first:YES];
        }else{
            [self.delegate tabDidSelected:index first:NO];
        }
        [_scrollView setCurrentItem:index animate:YES];
        _last = index;
    }

}


/**
 *  拖动完成后调用
 *
 *  @param view  <#view description#>
 *  @param index <#index description#>
 */
-(void)viewPager:(UIView*)view didSelectedIndex:(NSInteger)index{
    [self onSel:index];
}

ON_VIEW_EVENT(onButton){
    NSInteger index = sender.tag;
    [self onSel:index];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
