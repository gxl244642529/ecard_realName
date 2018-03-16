/**
 * IPad
 *
 * An interface for padding mechanisms to implement.
 * Copyright (c) 2007 Henri Torgemane
 *
 * See LICENSE.txt for full license information.
 */

#import <Foundation/Foundation.h>
/**
 * Tiny interface that represents a padding mechanism.
 */
@protocol IPad <NSObject>

/**
 *  增加填充
 *
 *  @param a <#a description#>
 */
-(NSData*)pad:(NSData*)a;
/**
 * Remove padding from the array.
 * @throws Error if the padding is invalid.
 */
-(NSData*)unpad:(NSData*)a;
/**
 * Set the blockSize to work on
 */
-(void)setBlockSize:(NSInteger)bs;

@end

