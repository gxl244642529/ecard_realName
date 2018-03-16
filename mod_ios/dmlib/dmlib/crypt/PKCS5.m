//
//  PKCS5.m
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PKCS5.h"

@interface PKCS5()
{
    NSUInteger _blockSize;
}

@end

@implementation PKCS5
/**
 *  增加填充
 *
 *  @param a <#a description#>
 */
-(NSData*)pad:(NSData*)a{
    
    NSUInteger c = _blockSize-a.length%_blockSize;
    if(c>0){
        
        //填充c
        NSMutableData* result = [[NSMutableData alloc]initWithData:a];
        Byte tmp[c];
        for (NSUInteger i=0;i<c;i++){
            tmp[i] = c;
        }
        [result appendBytes:tmp length:c];
        return result;
    }
    return a;
}
/**
 * Remove padding from the array.
 * @throws Error if the padding is invalid.
 */
-(NSData*)unpad:(NSData*)a{
    NSUInteger c = a.length % _blockSize;
    if (c!=0) {
        return a;
    }
    Byte last;
    [a getBytes:&last range:NSMakeRange(a.length-1, 1)];
    for (Byte i=1;i<=last;++i) {
        Byte v;
        [a getBytes:&v range:NSMakeRange(a.length-i, 1)];
        if (last!=v) {
            NSLog(@"PKCS#5:unpad: Invalid padding value. expected [%d], found [%d]",last,v);
            return nil;
        }
    }
    if([a isKindOfClass:[NSMutableData class]]){
        NSMutableData* data = (NSMutableData*)a;
        data.length = data.length - last;
        return data;
    }
    
    Byte tmp[a.length-last];
    [a getBytes:tmp range:NSMakeRange(a.length-last, last)];
    
    NSMutableData* data = [[NSMutableData alloc]initWithBytes:tmp length:a.length-last];
    
    return data;
    
    
}
/**
 * Set the blockSize to work on
 */
-(void)setBlockSize:(NSInteger)bs{
    _blockSize = bs;
}
@end
