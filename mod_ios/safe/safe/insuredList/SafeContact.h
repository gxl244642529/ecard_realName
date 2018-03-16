//
//  SafeContact.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface SafeContact : NSObject<IJsonValueObject>

@property (nonatomic,assign) NSInteger index;
@property (nonatomic,assign) NSInteger id;
@property (nonatomic,copy) NSString* name;
@property (nonatomic,copy) NSString* idCard;
@property (nonatomic,assign) NSInteger relation;

-(NSMutableDictionary*)toJson;


@end
