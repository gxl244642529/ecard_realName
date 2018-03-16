//
//  SafeInsuredListView.h
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>
#import "SafeInsuredWithRelationCell.h"

IB_DESIGNABLE
@interface SafeInsuredListView : UIView<DMCheckDelegate,DMFormElement,IDataAdapterListener,SafeInsuredWithRelationCellDelegate>

//是否多人
@property (nonatomic,assign) BOOL isMutil;

-(void)onSelect:(NSArray*)arr;


-(NSArray*)getInsured;

@end
