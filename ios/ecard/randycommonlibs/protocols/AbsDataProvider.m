//
//  AbsDataProvider.m
//  ecard
//
//  Created by randy ren on 15/5/30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AbsDataProvider.h"

@interface AbsDataProvider()


@end

@implementation AbsDataProvider

-(void)load{
    
}

-(void)setDataProviderListener:(id<IDataProviderListener>)listener{
    _listener = listener;
}

@end
