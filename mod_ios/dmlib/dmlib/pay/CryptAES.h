//
//  CryptAES.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface CryptAES : NSObject

/**
 *  加密
 *
 *  @param clearText <#clearText description#>
 *  @param key       <#key description#>
 *
 *  @return <#return value description#>
 */
+(NSString *) encrypt:(NSString *)clearText key:(NSString *)key;


/**
 *  解密
 *
 *  @param cipherText <#cipherText description#>
 *  @param key        <#key description#>
 *
 *  @return <#return value description#>
 */
+(NSString*) decrypt:(NSString*)cipherText key:(NSString*)key;

@end
