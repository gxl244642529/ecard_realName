//
//  ArrayContainer.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ArrayContainer : NSObject


@property (nonatomic,retain) NSMutableArray* array;

-(void)registerObject:(NSInteger)index object:(id)object;

-(id)objectAt:(NSInteger)index;

@end
