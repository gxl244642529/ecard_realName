//
//  ICrypt.h
//  des
//
//  Created by randy ren on 15/8/29.
//
//

#import <Foundation/Foundation.h>


@protocol ICryptData <NSObject>

/**
 *  加密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSData*)encrpyt:(NSData*)text key:(NSData*)key;

/**
 *  解密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSData*)decrpyt:(NSData*)text key:(NSData*)key;

@end

