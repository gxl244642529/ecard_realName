//
//  ECardModel.h
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ECardModel : NSObject

//绑定二维码
+(void)bindBarcode:(NSString*)barcode;
//解绑
+(void)unbind:(NSString*)cardId;
//修改
+(void)edit:(NSString*)cardId name:(NSString*)name;
@end
