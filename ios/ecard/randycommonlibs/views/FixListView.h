//
//  FixListView.h
//  ecard
//
//  Created by randy ren on 15-1-24.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "VLayout.h"
#import <DMLib/dmlib.h>
@interface FixListView : VLayout


-(void)setListener:(NSObject<IDataAdapterListener>*)listener;

-(void)registerNib:(NSString*)nibName;

-(void)setData:(NSArray*)data;




@end
