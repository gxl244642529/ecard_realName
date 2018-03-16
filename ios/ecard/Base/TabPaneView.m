//
//  TabPaneView.m
//  eCard
//
//  Created by randy ren on 13-11-17.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import "TabPaneView.h"

@interface TabPaneView()
{
    TabView* _tabView;
    UIScrollView* _scrollView;
    __weak NSObject<TabPaneViewDelegate>* _delegate;
    int _panCount;
    BOOL _first[3];
    
    BOOL _firstSet;
}
@end

@implementation TabPaneView
-(void)dealloc{
    _scrollView=NULL;
    _tabView = NULL;
    _delegate = NULL;
}

- (id)initWithParam:(TabView*)tabView scrollView:(UIScrollView*)scrollView
{
    if(self=[super init])
    {
        _tabView = tabView;
        [tabView setDelegate:self];
        scrollView.delegate=self;
        _scrollView=scrollView;
        _scrollView.pagingEnabled=YES;
        _first[0]=_first[1]=_first[2]=YES;
        _firstSet = TRUE;
    }
    return self;
}


-(void)addSubPane:(UIView*)pane
{
    _panCount++;
    [_scrollView addSubview:pane];
}
-(void)increaseSubPane{
     _panCount++;
}
//下一个frame
-(CGRect)getNextFrame
{
    int index=_panCount;
    CGRect frame = _scrollView.bounds;
    frame.origin.x = index * frame.size.width;
    return frame;
}
-(void)setDelegate:(NSObject<TabPaneViewDelegate>*)delegate
{
    _delegate = delegate;
    if(delegate)
    {
        if(_firstSet)
        {
            [delegate tabDidSelected:0 first:YES];
            _first[0]=FALSE;
            _firstSet= FALSE;
        }
     }
}
#pragma mark - UIScrollViewDelegate


- (void)scrollViewDidEndDecelerating:(UIScrollView *)aScrollView {
    int x = aScrollView.contentOffset.x;
    int index = x / (int)aScrollView.bounds.size.width;
    [_tabView setCurrentSelectIndex:index notify:FALSE];
    [_delegate tabDidSelected:index first:_first[index]];
    _first[index]=FALSE;
}

#pragma mark - TabViewDelegate
-(void)tabDidSelected:(id)tab index:(NSInteger)index
{
    [_scrollView setContentOffset:CGPointMake(_scrollView.bounds.size.width * index, 0) animated:YES];
}
-(void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView
{
    [self scrollViewDidEndDecelerating:scrollView];
}

-(void)setCurrentSelected:(NSInteger)index notify:(BOOL)nofity
{
    [_tabView setCurrentSelectIndex:index notify:nofity];
    if(nofity){
        [_delegate tabDidSelected:index first:_first[index]];
    }
    
    _first[index]=FALSE;
}

@end
