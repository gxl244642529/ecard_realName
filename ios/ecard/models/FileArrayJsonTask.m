//
//  FileArrayJsonTask.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "FileArrayJsonTask.h"
#import <ecardlib/ecardlib.h>
#import <DMlib/dmlib.h>


@implementation FileObject

-(void)dealloc{
    self.filePath = NULL;
}

@end


@interface FileArrayJsonTask()
{
    NSString* _path;
    NSMutableArray* _list;
    Class _cls;

    /**
     *  当前数据
     */
    id _currentData;
    /**
     *  当前数据的备份
     */
    id _copyData;
    
    NSString* _clsName;
}

@end

@implementation FileArrayJsonTask
-(void)dealloc{
    _copyData =NULL;
    _path = NULL;
    _list = NULL;
    _cls = NULL;
    _currentData = NULL;
    _clsName = NULL;
}
-(void)clear{
    [[NSFileManager defaultManager]removeItemAtPath:_path error:NULL];
    [[NSFileManager defaultManager] createDirectoryAtPath:_path withIntermediateDirectories:NO attributes:NULL error:NULL];
}


-(NSInteger)getCount{
    return _list.count;
}
/**
 *  实际设置路径
 *
 *  @param api 路径名称
 */
-(void)setClass:(Class)cls{
    _cls = cls;
    _clsName = NSStringFromClass(cls);
    if(!_path){
        _path = [CacheUtil getPath: NSStringFromClass(cls)];
    }
}

-(void)setPath:(NSString*)path{
    _path = [CacheUtil getPath: path];
}


/**
 *  执行任务
 */
-(NSMutableArray*)getList{
    if(_list)return _list;
    NSMutableArray* result = [[NSMutableArray alloc]init];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSDirectoryEnumerator *dirEnum = [fileManager enumeratorAtPath:_path];
    NSString *path = nil;
    NSUInteger len = [_clsName length];
    while ((path = [dirEnum nextObject] )!= nil)
    {
        //判断
        if([path compare:_clsName options:0 range:NSMakeRange(0, len)]==NSOrderedSame){
            NSError* error = nil;
            NSString* absPath = [_path stringByAppendingPathComponent:path];
            NSData* data= [fileManager contentsAtPath:absPath];
            if(!data)continue;
            NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
            if(error){
                continue;
            }
            FileObject* object = [[_cls alloc]init];
            [ReflectUtil jsonToObject:resultMap src:object];
            object.filePath = absPath;
            NSDictionary* dic = [fileManager attributesOfItemAtPath:path error:NULL];
            NSDate* date = [dic valueForKey:NSFileModificationDate];
            object.modified = [date timeIntervalSince1970];
            [result addObject:object];

        }
    }
    NSArray *array = [result sortedArrayUsingComparator:^(FileObject *obj1,FileObject *obj2) {
        NSTimeInterval val1 = [obj1 modified];
        NSTimeInterval val2 = [obj2 modified];
        if (val1 > val2) {
            return NSOrderedAscending;
        } else {
            return NSOrderedDescending;
        }
    }];
    _list = [[NSMutableArray alloc]initWithArray:array];
    return _list;
}

/**
 *  将最后更新的设置为当前的
 */
-(void)setLastModifiedAsCurrent{
    if(_list.count>0){
        _currentData = _list[0];
    }else{
        [self createNewAsCurrent];
    }
}


/**
 *  创建新的设置为当前的
 */
-(void)createNewAsCurrent{
    _currentData = [[_cls alloc]init];
}
-(id)getItem:(NSInteger)index{
    return _list[index];
}
-(BOOL)containsObject:(FileObject*)object{
    return [_list containsObject:object];
}



-(void)addObject:(FileObject*)object{
    //获取现在的时间
    NSDate* date  = [NSDate date];
    NSTimeInterval timestamp = [date timeIntervalSince1970];
    
    //带className
    NSString* path =  [_path stringByAppendingPathComponent:[NSString stringWithFormat:@"%@_%lf.json",_clsName,timestamp]];
    [self writeToPath:object path:path];
    object.filePath = path;
    object.modified = timestamp;
    
    [_list insertObject:object atIndex:0];
}


+(NSString*)dicToJson:(NSDictionary*)dic{
    NSMutableString* str = [[NSMutableString alloc]init];
    
    [str appendString:@"{"];
    BOOL first = true;
    for(NSString* key in dic.allKeys){
        
        if(first){
            first = false;
        }else{
            [str appendString:@","];
        }
        
        [str appendString:@"\""];
        [str appendString:key];
        [str appendString:@"\""];
        [str appendString:@":"];
        
        id value = [dic valueForKey:key];
        if([value isKindOfClass:[NSString class]]){
            [str appendString:@"\""];
            [str appendString:value];
            [str appendString:@"\""];
        }else{
            [str appendFormat:@"%@",value];
        }
        
        
    }
    
    
    [str appendString:@"}"];
    
    return str;
}



-(void)writeToPath:(FileObject*)object path:(NSString*)path{
    NSDictionary* dic ;
    if([object conformsToProtocol:@protocol(IJsonToObject)]){
        id<IJsonToObject> obj = (id<IJsonToObject>)object;
        dic = [obj toJsonValue];
    }else{
        
        dic = [ReflectUtil objectToJson:object];
    }
    NSString *returnString=[FileArrayJsonTask dicToJson:dic];
    //写入数据
    NSData* data = [returnString dataUsingEncoding:NSUTF8StringEncoding];
    [data writeToFile:path atomically:YES];
    
}

-(void)updateObject:(FileObject*)object{
    [self writeToPath:object path:object.filePath];
    NSDate* date  = [NSDate date];
    NSTimeInterval timestamp = [date timeIntervalSince1970];
    object.modified =timestamp;
}

-(void)removeObject:(FileObject*)object{
    [_list removeObject:object];
    [[NSFileManager defaultManager] removeItemAtPath:object.filePath error:nil];
    
}


/**
 *  获取当前数据
 *
 *  @return <#return value description#>
 */
-(id)getCurrent{
    //如果没有就创建一个
    if(!_currentData){
        _currentData = [[_cls alloc]init];
        assert([_currentData isKindOfClass:[FileObject class]]);
    }
    _copyData =[ReflectUtil copyObject:_currentData];
    return _currentData;
}

/**
 *  设置当前数据，当前数据必须是list中包含的数据，不能是外部数据
 *
 *  @param current <#current description#>
 */
-(void)setCurrent:(id)current{
    //也可以是空的
    _currentData = current;
}

-(void)saveCurrent{
    //比较是否一样
    if([_list containsObject:_currentData]){
        [self updateObject:_currentData];
    }else{
        [self addObject:_currentData];
    }
    _copyData = NULL;
}
@end
