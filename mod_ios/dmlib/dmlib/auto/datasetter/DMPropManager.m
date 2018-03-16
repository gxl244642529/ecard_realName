//
//  DMPropManager.m
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPropManager.h"
#import "ReflectUtil.h"
#import "DMViewInfoCreater.h"

@interface DMPropManager()
{
    NSMutableDictionary* _dic;
    DMViewInfoCreater* _creater;
}

@end

@implementation DMPropManager

IMPLEMENT_SINGLETON(DMPropManager)


-(void)dealloc{
    _creater = nil;
    _dic = nil;
}


-(DMViewInfoCreater*)defaultCreater{
    return _creater;
}

-(NSDictionary<NSString*,NSArray<NSObject<DMViewInfoCreater>*>*>*)parseData:(Class)clazz{
    //如果是实体类
    NSArray* properties = [ReflectUtil propertyKeys:clazz];
    NSDictionary<NSString*,NSMutableArray<NSObject<DMViewInfoCreater>*>*>* dic = [[NSMutableDictionary alloc]initWithCapacity:properties.count];
    //分析
    DMViewInfoCreater* creator;
    NSString* name;
    for (NSString* key in properties) {
        
        if([key rangeOfString:@"_"].location != NSNotFound){
            NSArray* arr = [key componentsSeparatedByString:@"_"];
            if(arr.count > 2){
                NSLog(@"%@ column %@ name error,to many _!",NSStringFromClass(clazz), key);
                continue;
            }
            //取出后面
            name = arr[1];
            NSString* t = arr[0];
            //这个t比较
            creator = [self get:t];
        }else{
            name = key;
            creator = _creater;
        }
        if( [dic objectForKey:name] ){
            NSMutableArray<NSObject<DMViewInfoCreater>*>* arr = dic[name];
            [arr addObject:creator];
        }else{
            NSMutableArray<NSObject<DMViewInfoCreater>*>* arr = [[NSMutableArray alloc]initWithObjects:creator, nil];
            [dic setValue:arr forKey:name];
        }
    }
    return dic;
}



-(id)init{
    if(self = [super init]){
        _dic = [[NSMutableDictionary alloc]init];
        _creater = [[DMViewInfoCreater alloc]init];
        //背景
        [_dic setValue:[[DMBgViewInfoCreater alloc]init] forKey:@"bg"];
        //图片
        [_dic setValue:[[DMImageInfoInfoCreater alloc]init] forKey:@"img"];
        //隐藏
        [_dic setValue:[[DMHiddenInfoInfoCreater alloc]init] forKey:@"hd"];
        //本地图片
        [_dic setValue:[[DMLocalImgInfoCreater alloc]init] forKey:@"lc"];
    }
    return self;
}

-(void)registerType:(NSString*)type creater:(DMViewInfoCreater*)creater{
    _dic[type] = creater;
}
-(DMViewInfoCreater*)get:(NSString*)type{
    return [_dic objectForKey:type];
}
@end
