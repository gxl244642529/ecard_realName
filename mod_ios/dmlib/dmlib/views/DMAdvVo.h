//
//  DMAdvVo.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IJsonValueObject.h"

@interface DMAdvVo : NSObject<IJsonValueObject>


@property (nonatomic,copy) NSString* title;
@property (nonatomic,copy) NSString* url;
@property (nonatomic,copy) NSString* imgBig;
@property (nonatomic,copy) NSString* img;
@property (nonatomic,assign) BOOL enable;

@end
