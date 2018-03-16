//
//  WeakSet.h
//  ecard
//
//  Created by randy ren on 15/4/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>




////////////////////////////////////////////////////////////////////////

@interface WeakSet : NSObject

-(void)addObjectsFromArray:(NSArray *)array;
-(BOOL)containsObject:(id)anObject;
-(void)addObject:(id)object;
-(void)removeObject:(id)object;
-(NSArray*)allObjects;
-(NSInteger)count;
@end
