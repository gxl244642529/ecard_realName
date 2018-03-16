//
//  ArrayContainer.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ArrayContainer.h"


@interface ArrayContainer()
{
 
}

@end

@implementation ArrayContainer

-(id)objectAt:(NSInteger)index{
    return [_array objectAtIndex:index];
}
-(id)init{
    if(self = [super init]){
        self.array = [[NSMutableArray alloc]init];
    }
    return self;
}

-(void)registerObject:(NSInteger)index object:(id)object{
    if(index == _array.count){
        [_array addObject:object];
    }else{
        if(index > _array.count){
            for(NSInteger i=index-_array.count; i >0 ; --i){
                [_array addObject:[NSNull null]];
            }
            [_array addObject:object];
        }else{
            _array[index] = object;
        }
    }

}

-(void)dealloc{
    self.array = nil;
}

@end
