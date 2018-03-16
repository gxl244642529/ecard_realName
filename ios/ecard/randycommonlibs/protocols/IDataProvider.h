//
//  IDataProvider.h
//  ecard
//
//  Created by randy ren on 15/5/30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#ifndef ecard_IDataProvider_h
#define ecard_IDataProvider_h


@protocol IDataProviderListener <NSObject>

-(void)onReceiveData:(id)data;
-(void)onError:(NSString*)error;

@end

@protocol IDataProvider <NSObject>

-(void)load;
-(void)setDataProviderListener:(id<IDataProviderListener>)listener;

@end




#endif
