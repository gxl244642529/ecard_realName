//
//  ICipher.h
//  des
//
//  Created by randy ren on 15/8/29.
//
//
#import <Foundation/Foundation.h>


@protocol ICipher <NSObject>

-(NSInteger)getBlockSize;
-(NSData*)encrypt:(NSData*)src;
-(NSData*)decrypt:(NSData*)src;
-(NSString*)toString;

@end

