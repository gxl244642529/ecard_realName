//
//  CommonUtil.m
//  randycommonlibs
//
//  Created by randy ren on 14-7-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "CommonUtil.h"
#import <CommonCrypto/CommonDigest.h>
#include <ifaddrs.h>
#include <arpa/inet.h>
#include <net/if.h>



#define IOS_CELLULAR    @"pdp_ip0"
#define IOS_WIFI        @"en0"
#define IP_ADDR_IPv4    @"ipv4"
#define IP_ADDR_IPv6    @"ipv6"
@implementation CommonUtil

+(NSString*)encodeURL:(NSString *)string
{
    NSString *newString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"),kCFStringEncodingUTF8));
    if (newString) {
        return newString;
    }
    return @"";
}


+(__kindof UIViewController*)findPrevController:(UIViewController*)controller{
    NSArray* arr = controller.navigationController.viewControllers;
    for(NSInteger i=arr.count-1; i >=0 ; --i){
        if(arr[i]==controller){
            if(i>0){
                return arr[i-1];
            }
            break;
        }
    }
    return nil;
    
}

+(__kindof UIViewController*)findPrevController:(UINavigationController*)nav clazz:(Class)clazz{
    NSArray* arr = nav.viewControllers;
    for(NSInteger i=arr.count-1; i >=0 ; --i){
        if([arr[i] isKindOfClass:clazz]){
            if(i>0){
                return arr[i-1];
            }
            break;
        }
    }
    return nil;

}
+(__kindof UIViewController*)findController:(UINavigationController*)nav clazz:(Class)clazz{
    NSArray* arr = nav.viewControllers;
    for(NSInteger i=arr.count-1; i >=0 ; --i){
        if([arr[i] isKindOfClass:clazz]){
            return arr[i];
        }
    }
    return nil;
}


+ (NSString *)md5:(NSString *)str
{
    const char *cStr = [str UTF8String];
    unsigned char result[16];
    CC_MD5(cStr, (CC_LONG)strlen(cStr), result); // This is the md5 call
    return [NSString stringWithFormat:
            @"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}
+(BOOL)isLongScreen{
    CGSize size = [UIScreen mainScreen].bounds.size;
    return (size.height / size.width) > 1.5;
}

+(void)downloadApp:(NSString*)appID;
{
    NSString *string = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/cn/app/id%@?mt=8",appID];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:string]];
}

+(void)checkAndOpenApp:(NSString*)strurl appID:(NSString*)appID{
    NSURL *url = [NSURL URLWithString:strurl];
    if([[UIApplication sharedApplication] canOpenURL:url])
    {
        [[UIApplication sharedApplication]openURL:url];
    }else{
        [CommonUtil downloadApp:appID];
    }
}


+(BOOL)checkAndOpenApp:(NSString*)appUrl url:(NSString*)url parent:(UIViewController*)parent{
    NSURL *appUrlUrl = [NSURL URLWithString:appUrl];
    if([[UIApplication sharedApplication] canOpenURL:appUrlUrl])
    {
        [[UIApplication sharedApplication]openURL:appUrlUrl];
        return YES;
    }
    return FALSE;
}

+(void)makePhoneCall:(NSString*)phoneNumber parent:(UIView*)parent
{
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",phoneNumber]];
    UIWebView* phoneCallWebView = (UIWebView*)[parent viewWithTag:9875];
    if(!phoneCallWebView)
    {
        phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
        phoneCallWebView.tag = 9875;
        [parent addSubview:phoneCallWebView];
    }
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
}

+ (NSString *)sha1:(NSString *)input
{
    const char *ptr = [input UTF8String];
    
    int i =0;
    int len = (int)strlen(ptr);
    Byte byteArray[len];
    while (i!=len)
    {
        unsigned eachChar = *(ptr + i);
        unsigned low8Bits = eachChar & 0xFF;
        
        byteArray[i] = low8Bits;
        i++;
    }
    
    unsigned char digest[CC_SHA1_DIGEST_LENGTH];
    
    CC_SHA1(byteArray, len, digest);
    
    NSMutableString *hex = [NSMutableString string];
    for (int i=0; i<20; i++)
        [hex appendFormat:@"%02x", digest[i]];
    
    NSString *immutableHex = [NSString stringWithString:hex];
    
    return immutableHex;
}

+ (NSString *)getIPAddress:(BOOL)preferIPv4
{
    NSArray *searchArray = preferIPv4 ?
    @[ IOS_WIFI @"/" IP_ADDR_IPv4, IOS_WIFI @"/" IP_ADDR_IPv6, IOS_CELLULAR @"/" IP_ADDR_IPv4, IOS_CELLULAR @"/" IP_ADDR_IPv6 ] :
    @[ IOS_WIFI @"/" IP_ADDR_IPv6, IOS_WIFI @"/" IP_ADDR_IPv4, IOS_CELLULAR @"/" IP_ADDR_IPv6, IOS_CELLULAR @"/" IP_ADDR_IPv4 ] ;
    
    NSDictionary *addresses = [self getIPAddresses];
    //NSLog(@"addresses: %@", addresses);
    
    __block NSString *address;
    [searchArray enumerateObjectsUsingBlock:^(NSString *key, NSUInteger idx, BOOL *stop)
     {
         address = addresses[key];
         if(address) *stop = YES;
     } ];
    return address ? address : @"0.0.0.0";
}

+ (NSDictionary *)getIPAddresses
{
    NSMutableDictionary *addresses = [NSMutableDictionary dictionaryWithCapacity:8];
    
    // retrieve the current interfaces - returns 0 on success
    struct ifaddrs *interfaces;
    if(!getifaddrs(&interfaces)) {
        // Loop through linked list of interfaces
        struct ifaddrs *interface;
        for(interface=interfaces; interface; interface=interface->ifa_next) {
            if(!(interface->ifa_flags & IFF_UP) || (interface->ifa_flags & IFF_LOOPBACK)) {
                continue; // deeply nested code harder to read
            }
            const struct sockaddr_in *addr = (const struct sockaddr_in*)interface->ifa_addr;
            if(addr && (addr->sin_family==AF_INET || addr->sin_family==AF_INET6)) {
                NSString *name = [NSString stringWithUTF8String:interface->ifa_name];
                char addrBuf[INET6_ADDRSTRLEN];
                if(inet_ntop(addr->sin_family, &addr->sin_addr, addrBuf, sizeof(addrBuf))) {
                    NSString *key = [NSString stringWithFormat:@"%@/%@", name, addr->sin_family == AF_INET ? IP_ADDR_IPv4 : IP_ADDR_IPv6];
                    addresses[key] = [NSString stringWithUTF8String:addrBuf];
                }
            }
        }
        // Free memory
        freeifaddrs(interfaces);
    }
    
    // The dictionary keys have the form "interface" "/" "ipv4 or ipv6"
    return [addresses count] ? addresses : nil;
}

@end