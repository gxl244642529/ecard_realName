//
//  RCTECardSelectorModule.m
//  MyProject
//
//  Created by 任雪亮 on 16/7/14.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTECardSelectorModule.h"


@interface RCTECardSelectorModule()
{

}

@property (nonatomic,copy,readwrite) RCTResponseSenderBlock callback;

@end

@implementation RCTECardSelectorModule
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(selectECard:(RCTResponseSenderBlock)callback)
{
  
  self.callback = callback;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    [ECardController selectECard: [DMJobManager sharedInstance].topController delegate:self];
    //[MyECardController selectCardDelegate:self parent:[DMJobManager sharedInstance].topController];
  });

  
}


-(void)didSelectECard:(ECard*)ecard{
  
  self.callback(@[ecard.cardId]);
  self.callback = nil;
  
}

@end
