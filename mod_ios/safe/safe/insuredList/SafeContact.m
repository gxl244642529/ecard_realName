//
//  SafeContact.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeContact.h"

@implementation SafeContact

-(void)fromJson:(NSDictionary*)json{
    _id = [json[@"id"]integerValue];
    _name = json[@"name"];
    _idCard = json[@"idCard"];
    _relation = [json[@"relation"]integerValue];
}
-(NSMutableDictionary*)toJson{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    dic[@"id"] = WRAP_INTEGER( self.id );
    dic[@"name"] = _name;
    dic[@"idCard"] = _idCard;
    dic[@"relation"] = WRAP_INTEGER(_relation);
    return dic;
}

@end
