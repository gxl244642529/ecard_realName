//
//  IPullTorefreshListener.h
//  ecard
//
//  Created by randy ren on 15/4/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#ifndef ecard_IPullTorefreshListener_h
#define ecard_IPullTorefreshListener_h

#import <DMLib/dmlib.h>


@protocol IPullTorefreshListener <IDataAdapterListener>

-(void)onLoadData:(NSInteger)position;

@end

#endif
