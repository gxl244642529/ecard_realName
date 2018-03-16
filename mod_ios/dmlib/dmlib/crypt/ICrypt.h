//
//  ICrypt.h
//  des
//
//  Created by randy ren on 15/8/29.
//
//

#import <Foundation/Foundation.h>


@protocol ICrypt <NSObject>

/**
 *  加密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSString*)encrpyt:(NSString*)text;

/**
 *  解密
 *
 *  @param text <#text description#>
 *
 *  @return <#return value description#>
 */
-(NSString*)decrpyt:(NSString*)text;

@end

