//
//  DataSetterUtil.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DataSetterUtil.h"
#import "UIView+ViewNamed.h"
#import "DMFormElement.h"
#import "DMMacro.h"
#import "DataUtil.h"
#import "DMJobManager.h"
#import "DMServers.h"
#import "ReflectUtil.h"
#import <objc/runtime.h>
#import "DMTableView.h"

#import "DMViewPropFinder.h"

#import "DMValue.h"
#import "DMMutilValue.h"

//字段类型表

__weak NSMutableDictionary* _dic;




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@implementation DataSetterUtil


+(NSMutableArray<id<DMDataSetter>>*)createDataSetters:(UIView *)view infos:(NSArray<DMViewInfo*>*)infos{
    
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    for (DMViewInfo* info in infos) {
        id<DMDataSetter> setter = [info createSetter:view];
        [arr addObject:setter];
    }
    return arr;
    
}

+(NSMutableDictionary*)initData{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    _dic = dic;
    return dic;
}

+(NSMutableArray*)findAllViewsByInfos:(UIView*)view info:(NSMutableArray<DMViewInfo*>*)infos{
    NSMutableArray* result = [[NSMutableArray alloc]init];
    for (DMViewInfo* info in infos) {
        UIView* item = [view viewWithTag:info.tag];
        [result addObject:item];
    }
    
    return result;
    
}
+(void)addCacheInfos:(NSMutableArray<DMViewInfo*>*)infos key:(NSString*)key{
    [_dic setValue:infos forKey:key];
}

+(NSMutableArray<DMViewInfo*>*)cachedInfos:(NSString*)key{
    return [_dic objectForKey:key];
}

+(NSMutableArray<__kindof UIView*>*)findAllViews:(UIView*)view arr:(NSMutableArray<DMViewInfo*>*)arr{
    NSMutableArray* result = [[NSMutableArray alloc]init];
    for (DMViewInfo* info in arr) {
        [result addObject:[view viewWithTag:info.tag]];
    }
    return result;
}
+(void)appendCache:(NSString*)key arr:(NSArray<DMViewInfo*>*)arr{
    [_dic setValue:arr forKey:key];
}

+(NSMutableArray<DMViewInfo*>*)cacheInfos:(UIView *)view key:(NSString *)key{
    NSMutableArray<DMViewInfo*>* result =[DataSetterUtil findAllViewInfo:view];
    [_dic setValue:result forKey:key ];
    return result;
}

static char* detailViewInfos = "detailViewInfos";

//将view的加入到缓存
+(NSMutableArray<DMViewInfo*>*)cacheInfosForDetailView:(UIView*)view key:(NSString*)key data:(id)data{
    NSMutableArray<DMViewInfo*>* resultInfos ;
    
    if([data isKindOfClass:[NSDictionary class]]){
        //将所有带名字的视图全部找出来,然后比对数据中是否有这些字段
        //首先判断有没有
        NSMutableArray<DMViewInfo*>* allInfos = objc_getAssociatedObject(view, detailViewInfos);
        if(!allInfos){
            allInfos = [DataSetterUtil findAllViewInfo:view];
            objc_setAssociatedObject(view, detailViewInfos, allInfos, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
        }
        resultInfos = [[NSMutableArray alloc]init];
        //如果是字典
        for(DMViewInfo* info in allInfos){
            if([info shouldHandle:data]){
                [resultInfos addObject:info];
            }
        }
    }else{
        //实体类
        resultInfos = [DMViewPropFinder findAllViewInfo:view class:[data class]];
        
    }
    
    [_dic setValue:resultInfos forKey:key];
    
    return resultInfos;
}

+(NSMutableArray<DMViewInfo*>*)findAllViewInfo:(UIView*)view{
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    [self visitSubviews:view arr:arr creater:[[DMPropManager sharedInstance]defaultCreater]];
    return arr;
}



+(void)visitSubviews:(UIView*)view arr:(NSMutableArray*)arr creater:(DMViewInfoCreater*)creater{
    for(UIView* subView in view.subviews){
        NSString* viewName = subView.viewName;
        if(viewName){
            DMViewInfo* info = [creater createViewInfo:subView];
            if(info){
                [arr addObject:info];
                if([subView isKindOfClass:[DMTableView class]]){
                    DMTableView* tableView = (DMTableView*)subView;
                    [self visitSubviews:tableView.realHeaderView arr:arr creater:creater];
                }
                 continue;
            }
        }
        
        if(subView.subviews.count>0){
            //下一层
            [self visitSubviews:subView arr:arr creater:creater];
        }
    }
}


@end



