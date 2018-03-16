//
//  ECard.h
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DMLib/DMLib.h>
@interface ECard : NSObject<IJsonValueObject>


@property (nonatomic,retain) NSString* cardId;
@property (nonatomic,retain) NSString* cardidExt;
@property (nonatomic,retain) NSString* cardName;
@property (nonatomic) double left;
@property (nonatomic,retain) NSString* expireTime;
@property (nonatomic,retain) NSString* cardType;

@end
