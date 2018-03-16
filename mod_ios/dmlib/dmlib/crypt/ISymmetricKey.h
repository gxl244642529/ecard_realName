//
//  ISymmetricKey.h
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ISymmetricKey <NSObject>

-(NSInteger)getBlockSize;

-(void)encrypt:(Byte*)block index:(NSInteger)index;

-(void)decrypt:(Byte*)block index:(NSInteger)index;


-(NSString*)toString;


@end
