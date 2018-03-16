//
//  Aes.m
//  aestest
//
//  Created by randy ren on 15/8/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "Aes.h"
#import "IPad.h"
#import "IMode.h"
#import "PKCS5.h"
#import "AESCrypt.h"
#import "ECBMode.h"
#import "DMBase64.h"

@interface Aes(){
    id<IPad> _pad;
    id<IMode> _mode;
    AESCrypt* _key;
}

@end

@implementation Aes

-(void)dealloc{
    _pad = nil;
    _mode = nil;
    _key = nil;
}

-(id)initWithKey:(NSString*)key{
    if(self = [super init]){
        _pad = [[PKCS5 alloc]init];
        _key = [[AESCrypt alloc]initWithKey:[key dataUsingEncoding:NSUTF8StringEncoding]];
        _mode = [[ECBMode alloc]initWithKey:_key padding:_pad];
        [_pad setBlockSize:[_mode getBlockSize]];
        
    }
    return self;
}



-(void)setKey:(NSString*)key{
    [_key setKey:[key dataUsingEncoding:NSUTF8StringEncoding]];
}

/**
 *  加密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSString*)encrpyt:(NSString*)text{
    
    NSData* data = [_mode encrypt:[text dataUsingEncoding:NSUTF8StringEncoding]];
    return [data base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
}

/**
 *  解密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSString*)decrpyt:(NSString*)text{
    NSData* data = [_mode decrypt:[[NSData alloc]initWithBase64EncodedString:text options:NSDataBase64DecodingIgnoreUnknownCharacters]];
    
    return [[NSString alloc] initWithData:data  encoding:NSUTF8StringEncoding];
}
@end
