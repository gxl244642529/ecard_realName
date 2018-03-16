//
//  ReflectUtil.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ReflectUtil.h"
#import "IJsonValueObject.h"


NSDictionary* _dataMap;

@implementation ReflectUtil

+(void)initialize{
    _dataMap = @{
                 @"hash":@"1",
                 @"description":@"1",
                 @"superclass":@"1",
                 @"debugDescription":@"1"
                 };
}
+ (void)class:(Class)aClass getPropertyKeyList:(NSMutableArray *)proName
{
    unsigned int count;
    objc_property_t *properties = class_copyPropertyList(aClass, &count);
    for (int i = 0; i < count; i++) {
        objc_property_t property = properties[i];
        NSString * key = [[NSString alloc]initWithCString:property_getName(property)  encoding:NSUTF8StringEncoding];
        if([_dataMap objectForKey:key]){
            continue;
        }
        [proName addObject:key];
    }
    aClass = [aClass superclass];
    if (aClass == [NSObject class]) {
        return;
    }
    [ReflectUtil class:aClass getPropertyKeyList:proName];
}


+(NSArray*)propertyKeys:(Class)cls
{
    NSMutableArray* arr = [[NSMutableArray alloc]initWithCapacity:0];
    
    [ReflectUtil class:cls getPropertyKeyList:arr];
    
    return arr;
}
+ (void)jsonToObject:(NSDictionary*)dataSource src:(NSObject*)src
{
    for (NSString *key in [dataSource allKeys]) {
        id propertyValue = [dataSource valueForKey:key];
        if (propertyValue && propertyValue!=[NSNull null]) {
            [src setValue:propertyValue forKey:key];
        }
    }
}



+(NSDictionary*)objectToJson:(NSObject*)src {
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    Class cls = [src class];
    NSArray* arr = [ReflectUtil propertyKeys:cls];
    for (NSString *key in arr) {
        id propertyValue = [src valueForKey:key];
        if (propertyValue && propertyValue != [NSNull null]){
            [dic setValue:propertyValue forKey:key];
        }
    }
    
    return dic;
    
}


+(id)copyObject:(id)object{
    NSObject* newObject = [[[object class]alloc]init];
    for (NSString *key in [ReflectUtil propertyKeys:[object class]]) {
        id propertyValue = [object valueForKey:key];
        if(propertyValue && propertyValue != [NSNull null]){
            [newObject setValue:propertyValue forKey:key];
        }
    }
    return newObject;

}



@end
