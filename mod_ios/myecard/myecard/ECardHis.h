//
//  ECardHis.h
//  ecard
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface ECardHis : NSObject<IJsonValueObject>


@property (nonatomic,copy) NSString* shpName;
@property (nonatomic,copy) NSString* type;
@property (nonatomic,copy) NSString* time;
@property (nonatomic,copy) NSString* value;
@property (nonatomic,readonly) UIColor* bg_type;

@end
