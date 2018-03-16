//
//  ECBMode.h
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ICipher.h"
#import "ISymmetricKey.h"
#import "IPad.h"
#import "IMode.h"
@interface ECBMode : NSObject<ICipher,IMode>
-(id)initWithKey:(id<ISymmetricKey>)key padding:(id<IPad>)padding;
@end
