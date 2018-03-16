//
//  FileArrayJsonTask.h
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "IArrayJsonTask.h"

@protocol IJsonToObject <NSObject>

-(NSDictionary*)toJsonValue;

@end


/**
 *  文件排序类型
 */
typedef enum _FileOrderType{
    
    LastModified_Desc,
    LastModified_Asc
    
}FileOrderType;



@interface FileObject : NSObject

/**
 *  修改时间
 */
@property (nonatomic) NSTimeInterval modified;

/**
 *  文件路径
 */
@property (nonatomic,retain) NSString* filePath;

@end


//////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 *  文件列表
 */
@interface FileArrayJsonTask : NSObject






/**
 *  实际设置路径
 *
 *  @param api 路径名称
 */
-(void)setClass:(Class)cls;

/**
 *  设置路径名称
 *
 *  @param path <#path description#>
 */
-(void)setPath:(NSString*)path;


/**
 *  执行任务
 */
-(NSMutableArray*)getList;

/**
 *  增加并写入文件
 *
 *  @param object <#object description#>
 */
-(void)addObject:(FileObject*)object;

-(void)removeObject:(FileObject*)object;

-(void)updateObject:(FileObject*)object;



-(BOOL)containsObject:(FileObject*)object;
/**
 *  获取当前数据
 *
 *  @return <#return value description#>
 */
-(id)getCurrent;

/**
 *  设置当前数据，当前数据必须是list中包含的数据，不能是外部数据
 *
 *  @param current <#current description#>
 */
-(void)setCurrent:(id)current;

/**
 *  将最后更新的设置为当前的
 */
-(void)setLastModifiedAsCurrent;


/**
 *  获取
 *
 *  @param index <#index description#>
 *
 *  @return <#return value description#>
 */
-(id)getItem:(NSInteger)index;
/**
 *  创建新的设置为当前的
 */
-(void)createNewAsCurrent;

/**
 *  保存当前的
 */
-(void)saveCurrent;

/**
 *  清空
 */
-(void)clear;


-(NSInteger)getCount;

@end
