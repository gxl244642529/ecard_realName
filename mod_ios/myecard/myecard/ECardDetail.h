//
//  ECardDetail.h
//  ecard
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface ECardDetail : NSObject<IJsonValueObject>


@property (nonatomic,copy) NSString* left;
@property (nonatomic,copy) NSString* time;
@property (nonatomic,retain) NSArray* hisList;

@end
