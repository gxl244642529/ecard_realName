//
//  SDiyView.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TabPaneView.h"
#import <ecardlib/ecardlib.h>
#import "ArrayJsonTask.h"
#import "JsonTaskManager.h"

#import "CollectionGroup.h"
@interface SDiyView : UIView<UICollectionViewDataSource,UICollectionViewDelegate,UIAlertViewDelegate,UIScrollViewDelegate,IDataAdapterListener,IOnItemClickListener>



-(void)reloadData;

@property (nonatomic,weak) UIViewController* parent;

@end
