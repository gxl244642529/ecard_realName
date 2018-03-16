//
//  DataSetterUtil.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IDataAdapterListener.h"
#import "DMFormElement.h"
#import "DMDataSetter.h"
#import "DMViewInfo.h"
#import "DMPropManager.h"

typedef enum : NSUInteger{
    //表单元素
    ViewType_FormItem,
    //提交按钮
    ViewType_FormSubmit,
    //标签
    ViewType_Label,
    //输入框
    ViewType_TextField,
    //输入框
    ViewType_TextView,
    //图片,网络图片
    ViewType_ImageView,
    //可选择图片
    ViewType_SelectImageView
    
}ViewType;










@interface DataSetterUtil : NSObject

+(NSMutableDictionary*)initData;


+(NSMutableArray<id<DMDataSetter>>*)createDataSetters:(UIView*)view infos:(NSArray<DMViewInfo*>*)infos;

+(void)appendCache:(NSString*)key arr:(NSArray<DMViewInfo*>*)arr;
//将view的加入到缓存
+(NSMutableArray<DMViewInfo*>*)cacheInfos:(UIView*)view key:(NSString*)key;

//将view的加入到缓存
+(NSMutableArray<DMViewInfo*>*)cacheInfosForDetailView:(UIView*)view key:(NSString*)key data:(id)data;

+(NSMutableArray<DMViewInfo*>*)cachedInfos:(NSString*)key;
+(void)addCacheInfos:(NSMutableArray<DMViewInfo*>*)infos key:(NSString*)key;


//找到所有info
+(NSMutableArray<DMViewInfo*>*)findAllViewInfo:(UIView*)view;

//通过info查找所有表单元素
+(NSMutableArray*)findAllViewsByInfos:(UIView*)view info:(NSMutableArray<DMViewInfo*>*)infos;

+(NSMutableArray<__kindof UIView*>*)findAllViews:(UIView*)view arr:(NSMutableArray<DMViewInfo*>*)arr;

@end
