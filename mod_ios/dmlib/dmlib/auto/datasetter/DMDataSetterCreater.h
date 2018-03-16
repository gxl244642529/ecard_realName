//
//  DMDataSetterCreater.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>


#import "IDataAdapterListener.h"

@interface DMDataSetterCreater : NSObject
+(NSObject<IDataAdapterListener>*)create:(id)controller cellName:(NSString*)cellName;
@end
