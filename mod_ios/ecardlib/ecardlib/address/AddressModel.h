//
//  AddressModel.h
//  ecard
//
//  Created by randy ren on 16/2/25.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>


#import <DMLib/DMLib.h>
#import "AddressVo.h"
@interface AddressModel : NSObject


+(void)deleteAddress:(AddressVo*)address;
//查询运费
+(void)queryTransFee:(AddressVo*)address;

@end
