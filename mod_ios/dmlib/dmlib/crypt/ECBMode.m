//
//  ECBMode.m
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ECBMode.h"


@interface ECBMode()
{
    __weak id <ISymmetricKey> _key;
    __weak id <IPad> _padding;
}

@end

@implementation ECBMode
-(id)initWithKey:(id<ISymmetricKey>)key padding:(id<IPad>)padding{
    if(self = [super init]){
        _key = key;
        _padding = padding;
    }
    return self;
}
-(NSInteger)getBlockSize{
    return [_key getBlockSize];
}
-(NSData*)encrypt:(NSData*)src{
    src = [_padding pad:src];
    NSUInteger blockSize = [_key getBlockSize];
    Byte tmp[blockSize];
    NSMutableData* result = [[NSMutableData alloc]init];
    
    for (NSUInteger i=0,s = src.length;i<s;i+=blockSize) {
        //从源拷贝blockSize大小的字节
        [src getBytes:tmp range:NSMakeRange(i, blockSize)];
        //加密
        [_key encrypt:tmp index:0];
        //拷贝进dst
        [result appendBytes:tmp length:blockSize];
    }
    return result;
    
}
-(NSData*)decrypt:(NSData*)src{
    NSUInteger blockSize = [_key getBlockSize];
    Byte tmp[blockSize];
    NSMutableData* result = [[NSMutableData alloc]init];
    
    for (NSUInteger i=0,s = src.length;i<s;i+=blockSize) {
        //从源拷贝blockSize大小的字节
        [src getBytes:tmp range:NSMakeRange(i, blockSize)];
        //解密
        [_key decrypt:tmp index:0];
        //拷贝进dst
        [result appendBytes:tmp length:blockSize];
    }
    
    
 
    
    return  [_padding unpad:result];
    
}
-(NSString*)toString{
   return [NSString stringWithFormat:@"%@-ecb",[_key toString]];
}



@end
