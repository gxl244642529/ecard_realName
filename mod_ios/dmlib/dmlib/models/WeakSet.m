//
//  WeakSet.m
//  ecard
//
//  Created by randy ren on 15/4/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "WeakSet.h"


@interface WeakWrapper : NSObject


-(id)initWithObject:(id)object;
-(id)object;
@end


@interface WeakSet()
{
    NSMutableSet* _set;
}

@end

@implementation WeakSet
-(NSInteger)count{
    return _set.count;
}
-(id)init{
    if(self = [super init]){
        _set = [[NSMutableSet alloc]init];
    }
    return self;
}

-(void)dealloc{
    _set = nil;
}

-(void)addObjectsFromArray:(NSArray *)array{
    for (id data in array) {
        [self addObject:data];
    }
}

-(BOOL)containsObject:(id)anObject{
    NSArray* arr = [_set allObjects];
    for (WeakWrapper* data in arr) {
        id obj = data.object;
        if(obj == anObject){
            return YES;
        }
    }
    return NO;
}

-(void)addObject:(id)object{
    [_set addObject:[[WeakWrapper alloc]initWithObject:object]];
}

-(void)removeObject:(id)object{
    assert(object!=nil);
    NSArray* arr = [_set allObjects];
    for (WeakWrapper* data in arr) {
        id obj = data.object;
        if(obj == object){
            [_set removeObject:data];
            break;
        }
    }
}

-(NSArray*)allObjects{
    NSArray* arr = [_set allObjects];
    NSMutableArray* result = [[NSMutableArray alloc]init];
    for (WeakWrapper* data in arr) {
        id object = data.object;
        if(object){
            [result addObject:object];
        }else{
            [_set removeObject:data];
        }
    }
    return result;
}


@end

@interface WeakWrapper()
{
    __weak id _object;
}

@end

@implementation WeakWrapper
-(id)object{
    return _object;
}
-(id)initWithObject:(id)object{
    if(self = [super init]){
        _object = object;
    }
    return self;
}

@end



