//
//  Aes.h
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ICrypt.h"
@interface Aes : NSObject<ICrypt>
-(id)initWithKey:(NSString*)key;
-(void)setKey:(NSString*)key;
@end
