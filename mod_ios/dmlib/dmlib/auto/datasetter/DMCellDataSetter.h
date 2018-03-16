//
//  DMCellDataSetter.h
//  libs
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "IDataAdapterListener.h"

/**
 * cell的数据设置器
 */
@interface DMCellDataSetter : NSObject<IDataAdapterListener>
-(id)initWithCellName:(NSString*)cellName;
-(void)setListener:(id<IDataAdapterListener>)listener;


+(DMCellDataSetter*)createWithView:(UIView*)view cellName:(NSString*)cellName;
+(DMCellDataSetter*)create:(id)controller cellName:(NSString*)cellName;
@end
