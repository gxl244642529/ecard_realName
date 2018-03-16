//
//  Page.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMPage : NSObject

@property (nonatomic,retain) NSArray* list;
@property (nonatomic,assign) NSInteger page;
@property (nonatomic,assign) NSInteger total;


@property (nonatomic,assign) NSDictionary* extra;

-(BOOL)isLast;
-(BOOL)isFirst;
-(NSDictionary*)toJson;
@end
