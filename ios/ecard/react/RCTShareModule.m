//
//  RCTShareModule.m
//  ecard
//
//  Created by 任雪亮 on 17/6/19.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTShareModule.h"
#import <ecardlib/ecardlib.h>

@implementation RCTShareModule

RCT_EXPORT_MODULE()



RCT_EXPORT_METHOD(wxShare:(NSDictionary*)map callback:(RCTResponseSenderBlock)callback){
  
  [DMShareUtil share:map onSuccess:^(BOOL isSuccess) {
    if(isSuccess){
      callback(@[@0]);
    }else{
      callback(@[@-1]);
    }
  }];
  
}


@end
