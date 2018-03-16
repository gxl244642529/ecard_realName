//
//  AddressEditController.h
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "AddressVo.h"
@interface AddressEditController : DMViewController<DMSubmitDelegate>



@property (nonatomic,retain) AddressVo* data;

@end
