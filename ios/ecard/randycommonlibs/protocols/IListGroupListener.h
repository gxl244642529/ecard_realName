//
//  IListGroupListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>




@protocol IListGroupListener <IDataAdapterListener>
-(void)onLoadData:(NSInteger)position;
@end
