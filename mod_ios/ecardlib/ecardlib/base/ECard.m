//
//  ECard.m
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECard.h"
#import "ECardUtil.h"

@implementation ECard

-(void)fromJson:(NSDictionary*)json{
    
    _cardId = json[@"cardid"];
    _cardName = json[@"cardName"];
    _expireTime =[ECardUtil parseDate: json[@"expireTime"] ];
    _cardType = json[@"cardType"];
    
}
@end
