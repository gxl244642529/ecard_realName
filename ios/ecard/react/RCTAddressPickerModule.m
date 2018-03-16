//
//  RCTAddressPickerModule.m
//  MyProject
//
//  Created by 任雪亮 on 16/7/17.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTAddressPickerModule.h"




@interface RCTAddressPickerModule (){
  
}

@property (nonatomic, copy,readwrite) RCTResponseSenderBlock callback;

@end

@implementation RCTAddressPickerModule
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(selectAddress:(RCTResponseSenderBlock)callback){
  self.callback = callback;
  dispatch_async(dispatch_get_main_queue(), ^{
     [AddressController selectAddress:[[DMJobManager sharedInstance]topController] delegate:self];
  });
 
}

-(void)onSelectAddress:(AddressVo*)address{
  if(self.callback!=nil){
    self.callback(@[[ReflectUtil objectToJson:address]]);
  }
}
@end
