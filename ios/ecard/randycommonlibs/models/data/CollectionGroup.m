//
//  CollectionGroup.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "CollectionGroup.h"

@implementation CollectionGroup

-(DMCollectionDataAdapter*)getAdapter{
    return (DMCollectionDataAdapter*)_adapter;
}
-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent listener:(NSObject<IDataAdapterListener>*)listener task:(ArrayJsonTask*)task layout:(UICollectionViewLayout*)layout{
    if(self = [super initWithTask:task]){
        _scrollView = [[UICollectionView alloc]initWithFrame:frame collectionViewLayout:layout];
        [parent addSubview:_scrollView];
        _wrap = [[PullToRefreshViewWrap alloc]initWithScrollView:_scrollView];
        [_wrap setRefreshDelegate:self];
        [_wrap setPullRefreshEnable];
        _adapter = [[DMCollectionDataAdapter alloc]init];
        [_adapter setListener:listener];
        [_adapter setScrollView:_scrollView];
        _state = [[LoadingState alloc]initWithScrollView:_scrollView];
        [_state setDelegate:self];
        if(task){
            [self onLoadData:Start_Position];
        }
    }
    return self;
}
-(UICollectionView*)getCollectionView{
    return (UICollectionView*)_scrollView;
}
-(void)onLoadingRefresh:(id)sender{
    [self onLoadData:Start_Position];
}
@end
