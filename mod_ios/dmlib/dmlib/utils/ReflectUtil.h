//
//  ReflectUtil.h
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <objc/runtime.h>


@interface ReflectUtil : NSObject

+(void)initialize;

/**
 *  获取所有属性
 *
 *  @param cls obj description
 *
 *  @return <#return value description#>
 */
+(NSArray*)propertyKeys:(Class)cls;



/**
 *  <#Description#>
 *
 *  @param dataSource <#dataSource description#>
 *  @param src        <#src description#>
 *
 *  @return <#return value description#>
 */
+(void)jsonToObject:(NSDictionary*)dataSource src:(NSObject*)src;

+(NSDictionary*)objectToJson:(NSObject*)src;

/**
 *  克隆
 *
 *  @param object <#object description#>
 *
 *  @return <#return value description#>
 */
+(id)copyObject:(id)object;



@end
