//
//  AddressVo.m
//  ecard
//
//  Created by randy ren on 16/2/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "AddressVo.h"

@implementation AddressVo
-(void)fromJson:(NSDictionary *)json{
    self.shengId=[[json valueForKey:@"shengId"]integerValue];
    self.shiId=[[json valueForKey:@"shiId"]integerValue];
    self.quId=[[json valueForKey:@"quId"]integerValue];
    self.sheng=[json valueForKey:@"sheng"];
    self.shi=[json valueForKey:@"shi"];
    self.qu=[json valueForKey:@"qu"];
    self.jie=[json valueForKey:@"jie"];
    self.postcode=[json valueForKey:@"postcode"];
    self.def=[[json valueForKey:@"def"]boolValue];
    self.name=[json valueForKey:@"name"];
    self.phone=[json valueForKey:@"phone"];
    self.id=[[json valueForKey:@"id"]integerValue];
    self.tyId = [[json valueForKey:@"tyId"]integerValue];
}
-(NSString*)address{
    if(self.qu){
        return [NSString stringWithFormat:@"%@%@%@%@",self.sheng,self.shi,self.qu,self.jie];
    }
    return [NSString stringWithFormat:@"%@%@%@",self.sheng,self.shi,self.jie];
}
-(NSString*)area{
    if(self.qu){
        return [NSString stringWithFormat:@"%@ %@ %@",self.sheng,self.shi,self.qu];
    }
    return [NSString stringWithFormat:@"%@ %@",self.sheng,self.shi];
    
}
@end
