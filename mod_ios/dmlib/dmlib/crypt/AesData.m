//
//  AesData.m
//  ecard
//
//  Created by randy ren on 15/10/29.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "AesData.h"
#import "IPad.h"
#import "IMode.h"
#import "PKCS5.h"
#import "AESCrypt.h"
#import "ECBMode.h"
#import "DMBase64.h"

@interface AesData(){
    id<IPad> _pad;
    id<IMode> _mode;
    AESCrypt* _key;
}

@end

@implementation AesData

-(id)init{
    if(self = [super init]){
        _pad = [[PKCS5 alloc]init];
        _key = [[AESCrypt alloc]initWithKey:[@"1234567890abcdef" dataUsingEncoding:NSUTF8StringEncoding]];
        _mode = [[ECBMode alloc]initWithKey:_key padding:_pad];
        [_pad setBlockSize:[_mode getBlockSize]];
    }
    return self;
    
}
-(void)dealloc{
    _pad = nil;
    _mode = nil;
    _key = nil;
}
/**
 *  加密
 *
 *  @param text <#text description#>
 *  @return <#return value description#>
 */
-(NSData*)encrpyt:(NSData*)text key:(NSData*)key{
    [_key setKey:key];
    return [_mode encrypt:text];
}

/**
 *  解密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSData*)decrpyt:(NSData*)text key:(NSData*)key{
    [_key setKey:key];
    return [_mode decrypt:text];
}
@end
