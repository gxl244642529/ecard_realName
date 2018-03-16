//
//  CollectionGroup.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "PullToRefreshBase.h"


@interface CollectionGroup : PullToRefreshBase<LoadingStateDelegate>
{
   
}


-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent listener:(NSObject<IDataAdapterListener>*)listener task:(ArrayJsonTask*)task layout:(UICollectionViewLayout*)layout;

-(DMCollectionDataAdapter*)getAdapter;
-(UICollectionView*)getCollectionView;

@end
