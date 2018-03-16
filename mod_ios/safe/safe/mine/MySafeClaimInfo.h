//
//  MySafeClaimInfo.h
//  ecard
//
//  Created by 任雪亮 on 16/3/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/dmlib.h>

//"delivery_addr":"福建省厦门市思明区fgg","delivery_tel":"15259298519","express_company":null,"delivery_no":null,"name":""
@interface MySafeClaimInfo : NSObject<IJsonValueObject>


@property (nonatomic,copy) NSString* name;
@property (nonatomic,copy) NSString* addr;
@property (nonatomic,copy) NSString* phone;
@property (nonatomic,copy) NSString* devCom;
@property (nonatomic,copy) NSString* devCode;


@end
