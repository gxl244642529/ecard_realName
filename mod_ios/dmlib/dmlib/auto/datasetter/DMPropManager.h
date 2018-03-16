//
//  DMPropManager.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMMacro.h"
#import "DMViewInfoCreater.h"
/**
 属性管理
 */
@interface DMPropManager : NSObject

DECLARE_SINGLETON(DMPropManager);

-(void)registerType:(NSString*)type creater:(DMViewInfoCreater*)creater;
-(DMViewInfoCreater*)get:(NSString*)type;
-(NSDictionary<NSString*,NSArray<NSObject<DMViewInfoCreater>*>*>*)parseData:(Class)clazz;

-(DMViewInfoCreater*)defaultCreater;

@end
