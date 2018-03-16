//
//  RCTAddressPickerModule.h
//  MyProject
//
//  Created by 任雪亮 on 16/7/17.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <ecardlib/ecardlib.h>


@interface RCTAddressPickerModule : NSObject<RCTBridgeModule ,AddressSelectDelegate>

@end
