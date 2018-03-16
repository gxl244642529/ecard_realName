//
//  Base64.m
//  ecard
//
//  Created by randy ren on 15/9/4.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DMBase64.h"

@implementation DMBase64
+(NSString*)encodeURL:(NSString *)string
{
    NSString *newString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"),kCFStringEncodingUTF8));
    if (newString) {
        return newString;
    }
    return @"";
}


+(NSString*)encodeString:(NSString*)src{
    return [DMBase64 encodeURL: [[src dataUsingEncoding:NSUTF8StringEncoding]base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed] ] ;
}

+(NSString*)encodeImage:(UIImage*)image{
    NSData* imgData = UIImageJPEGRepresentation(image,1);
    NSString* base64string = [imgData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    return [DMBase64 encodeURL:base64string];
}

@end
