//
//  AddressController.h
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "AddressVo.h"

@protocol AddressSelectDelegate <NSObject>

-(void)onSelectAddress:(AddressVo*)address;

@end

@interface AddressController : DMViewController<IOnItemClickListener,IDataAdapterListener>


+(void)selectAddress:(UIViewController*)parent delegate:(NSObject<AddressSelectDelegate>*)delegate;


@property (nonatomic,weak) NSObject<AddressSelectDelegate>* delegate;

@end
