//
//  AbsDataProvider.h
//  ecard
//
//  Created by randy ren on 15/5/30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IDataProvider.h"
@interface AbsDataProvider : NSObject<IDataProvider>
{
    __weak id<IDataProviderListener> _listener;
}
@end
