//
//  BusinessVo.h
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DMLib/dmlib.h>
@interface BusinessVo : NSObject<IJsonValueObject>



@property (nonatomic,copy) NSString* name;
@property (nonatomic,copy) NSString* img;
@property (nonatomic,assign) NSInteger id;

@end
