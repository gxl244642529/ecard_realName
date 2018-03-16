//
//  IObjectJsonTask.h
//  randycommonlib
//
//  Created by randy ren on 14-9-28.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IJsonTask.h"
#import "ISystemJsonTask.h"
@protocol IObjectJsonTask <ISystemJsonTask>

/**
 *  设置数据id，非必要
 *
 *  @param dataID <#dataID description#>
 */
-(void)setDataID:(id)dataID;
-(void)setDataID:(NSString*)name value:(id)dataID;


/**
 *  设置监听器
 *
 *  @param delegate <#delegate description#>
 */
-(void)setListener:(NSObject<IRequestResult>*)delegate;



@end
