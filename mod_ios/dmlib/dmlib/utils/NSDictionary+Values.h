//
//  NSDictionary+Values.h
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDictionary(Values)

-(NSString*)getString:(NSString*)key;
-(NSInteger)getInteger:(NSString*)key;
-(NSMutableArray*)parseArray:(NSString*)key class:(Class)clazz;


@end
