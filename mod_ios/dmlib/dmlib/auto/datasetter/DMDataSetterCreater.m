//
//  DMDataSetterCreater.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMDataSetterCreater.h"
#import "DMCellDataSetter.h"
@implementation DMDataSetterCreater
+(NSObject<IDataAdapterListener>*)create:(id)controller cellName:(NSString*)cellName{
    return [DMCellDataSetter create:controller cellName:cellName];
}
@end
