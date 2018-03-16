//
//  DMPayTableView.h
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IOnItemClickListener.h"
#import "IDataAdapterListener.h"


IB_DESIGNABLE
@interface DMPayTableView : UITableView<IOnItemClickListener>

@property (nonatomic,copy) IBInspectable NSString* support;
@property (nonatomic,copy) IBInspectable NSString* businessId;
@property (nonatomic,copy) IBInspectable NSString* cellName;
@property (nonatomic,copy) IBInspectable NSString* bundleName;

-(void)setListener:(id<IDataAdapterListener>)listener;

@end
