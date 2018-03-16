//
//  SafeRelationView.h
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <dmlib/dmlib.h>

#import "SafeContact.h"

@interface SafeRelationView : DMItem<DMValue>


@property (nonatomic,weak) SafeContact* data;

@end
