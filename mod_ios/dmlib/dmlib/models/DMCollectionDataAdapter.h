//
//  CollectionDataAdapter.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DMDataAdapter.h"

@interface DMCollectionDataAdapter : DMDataAdapter<UICollectionViewDataSource,UICollectionViewDelegate>


-(void)registerCell:(NSString*)nibName bundle:(NSBundle*)bundle;

@end
