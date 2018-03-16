//
//  AESCrypt.h
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ISymmetricKey.h"
@interface AESCrypt : NSObject<ISymmetricKey>

-(id)initWithKey:(NSData*)key;
-(void)setKey:(NSData*)key;

@end
