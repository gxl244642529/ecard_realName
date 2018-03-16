//
//  LostCardDetailInfo.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "LostCardDetailInfo.h"

@implementation LostCardDetailInfo

-(void)dealloc{
    self.info = NULL;
    self.phone = NULL;
    
    self.vry_error = NULL;
    self.vry_result = NULL;
    
    self.img = NULL;
    self.cardID = NULL;
    self.time = NULL;
}

@end
