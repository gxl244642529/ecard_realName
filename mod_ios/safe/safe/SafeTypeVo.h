//
//  SafeTypeVo.h
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface SafeTypeVo : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* title;
@property (nonatomic,copy) NSString* typeId;
@property (nonatomic,assign) NSInteger type;

@end
