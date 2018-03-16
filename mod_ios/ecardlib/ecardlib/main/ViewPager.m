//
//  ViewPager.m
//  randycommonlib
//
//  Created by randy ren on 14-10-9.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ViewPager.h"

@interface ViewPager()
{
    NSInteger _totalPages;
    NSInteger _curPage;
    NSInteger _willLoadPage;
    //所有加载的view
    NSMutableDictionary* _views;
    
    __weak NSObject<ViewPagerDelegate>* _dataSource;
    
    CGFloat _beginScroll;
}

@end

@implementation ViewPager

-(void)setCurrentItem:(NSInteger)index animate:(BOOL)animate{
    _curPage = index;
    [self loadData:_curPage];
    [self setContentOffset:CGPointMake(index * self.bounds.size.width, 0) animated:animate];
}

-(void)dealloc{
    _views = NULL;
}
-(UIView*)getItem:(NSInteger)index{
    NSString* key = [NSString stringWithFormat:@"%d",(int)index];
    UIView* view = _views[key];
    return view;
}
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _views = [[NSMutableDictionary alloc]init];
        self.showsHorizontalScrollIndicator = NO;
        self.pagingEnabled = YES;
        self.delegate = self;
        _curPage = 0;
    }
    return self;
}


-(void)awakeFromNib{
    _views = [[NSMutableDictionary alloc]init];
    self.showsHorizontalScrollIndicator = NO;
    self.pagingEnabled = YES;
    self.delegate = self;
    _curPage = 0;
}

-(void)setDataSource:(NSObject<ViewPagerDelegate>*)dataSource{
    _dataSource = dataSource;
    [self removeLeft];
    [self removeRight];
    [self reloadData];
}


- (void)reloadData
{
    [self removeLeft];
    [self removeRight];
    
    _totalPages = [_dataSource getPageCount];
    _curPage = 0;
    self.contentSize = CGSizeMake(self.bounds.size.width * _totalPages, 0);
    self.contentOffset = CGPointMake(0, 0);
    [self loadData:0];
}



- (void)loadData:(NSInteger)index
{
    //计算要创建的
    NSInteger pre = index - 1;
    if(pre<0)pre=0;
    NSInteger last = index+2;
    if(last > _totalPages-1){
        last = _totalPages - 1;
    }
    for(NSInteger i=pre; i <= last; ++i){
        NSString* key = [NSString stringWithFormat:@"%d",(int)i];
        UIView* view = _views[key];
        if(!view){
            CGRect rect = CGRectMake(i*self.bounds.size.width, 0, self.bounds.size.width, self.bounds.size.height);
            view = [_dataSource instantiateItem:i parent:self frame:rect];
            [_views setValue:view forKey:key];
            [self addSubview:view];
        }
    }

}
#pragma mark - UIScrollViewDelegate


-(BOOL)removeItem:(NSInteger)i{
    NSString* key = [NSString stringWithFormat:@"%ld",(long)i];
    UIView* view = _views[key];
    if(!view)return FALSE;
    [_views removeObjectForKey:key];
    [view removeFromSuperview];
    if([_dataSource respondsToSelector:@selector(destroyItem:index:)]){
        [_dataSource destroyItem:view index:i];
    }
    return TRUE;
}

-(void)removeLeft{
    //右边移动
    //移除左边的
    NSInteger max = _curPage-2;
    if(max>0){
        for(NSInteger i=max; i >=0; --i){
            if(![self removeItem:i]){
                break;
            }
        }
    }

}

-(void)removeRight{
    //左边移动
    //移除右边的
    NSInteger min = _curPage + 3;
    NSInteger max = _totalPages - 1;
    if(max > min){
        for(NSInteger i=min; i <= max; ++i){
            if(![self removeItem:i]){
                break;
            }
        }
    }

}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    CGFloat current =scrollView.contentOffset.x;
    _curPage =(int)scrollView.contentOffset.x / self.bounds.size.width;
    if(current > _beginScroll){
        //往右边移动
        [self removeLeft];
    }else{
        //往左边移动
        [self removeRight];
    }
    [self loadData:_curPage];
    if([_dataSource respondsToSelector:@selector(viewPager:didSelectedIndex:)]){
        [_dataSource viewPager:self didSelectedIndex:_curPage];
    }
}

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
     _beginScroll  =scrollView.contentOffset.x ;
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
    _curPage =(int)scrollView.contentOffset.x / self.bounds.size.width;
    [self loadData:_curPage];

}
/*
-(void)layoutSubviews{
    [super layoutSubviews];
    int index=0;
    CGRect frame =self.frame;
    for (UIView* view in self.subviews) {
        view.frame = CGRectMake(frame.size.width*index++, 0, frame.size.width, frame.size.height);
    }

}
*/
@end
