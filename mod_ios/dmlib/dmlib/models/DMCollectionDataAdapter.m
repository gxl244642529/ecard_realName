//
//  CollectionDataAdapter.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DMCollectionDataAdapter.h"

@implementation DMCollectionDataAdapter
-(void)notifyDataChanged{
    UICollectionView* tableView = (UICollectionView*)_scrollView;
    if(!tableView.dataSource){
        tableView.delegate = self;
        tableView.dataSource = self;
    }
    [tableView reloadData];
}
-(void)registerCell:(NSString*)nibName  bundle:(NSBundle*)bundle{
    UICollectionView* collectionView = (UICollectionView*)_scrollView;
    [collectionView setBackgroundColor:[UIColor clearColor]];
    [collectionView registerNib:[UINib nibWithNibName:nibName bundle:bundle] forCellWithReuseIdentifier:@"Cell"];

}
//////
//集合代理-每一部分数据项
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return _data.count;
}


//Cell
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"Cell";
    UICollectionViewCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:cellIdentifier forIndexPath:indexPath];
    NSInteger index = indexPath.row;
    [_listener onInitializeView:_scrollView cell:cell data:_data[index] index:index];
    return cell;
    
}

//代理－选择行的触发事件
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"cell selected at index path %ld", (long)indexPath.row);
    NSInteger index = indexPath.row;
    [self didSelectItem:index];
    
}

-(void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath{
   
}


@end
