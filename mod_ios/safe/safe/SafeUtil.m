//
//  SafeUtil.m
//  ecard
//
//  Created by randy ren on 15/12/2.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "SafeUtil.h"

@implementation SafeUtil
+(BOOL)isQuanjiabao:(SafeVo*)data{
    return [data.inId isEqualToString:@"2003"];
}

+(void)setSafeGuard:(NSString*)guard detailView:(UIView*)detailView detailHeight:(NSLayoutConstraint*)detailHeight{
    NSArray* arr = [guard componentsSeparatedByString:@"^"];
    NSInteger y = 0;
    for(NSString* s in arr){
        UIView* view = [[UIView alloc]initWithFrame:CGRectMake(0, y, 300, 20)];
        [detailView addSubview:view];
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"safebundle.bundle/ic_safeguard_left.png"]];
        imageView.frame = CGRectMake(0, 4, imageView.frame.size.width, imageView.frame.size.height);
        [view addSubview:imageView];
        UILabel* label = [DMViewUtil createLabel:s parent:view];
        label.frame = CGRectMake(14, 0, 280, 20);
        
        y+=20;
    }
    detailHeight.constant = arr.count * 20;
}
+(NSString*)getUrl:(NSString*)url{
    if((NSNull*)url == [NSNull null]){
        return nil;
    }
    if(!url){
        return nil;
    }
    return [NSString stringWithFormat:@"%@%@",[DMConfigReader getString:@"servers" subkey:@"picc"],url];
}


+(void)setTermForResult:(TermLabel*)_term noticeUrl:(NSString*)_noticeUrl protocalUrl:(NSString*)_protocolUrl{
    NSString* text = @"查看《保险条款》和《重要告知》";
    
    [_term text:text noticeUrl:_noticeUrl protocalUrl:_protocolUrl];
    
    
}
+(void)setTerm:(TermLabel*)_term noticeUrl:(NSString*)_noticeUrl protocalUrl:(NSString*)_protocolUrl {
   
    NSString* text = @"本人已认真阅读了本保单所适用的所有《保险条款》和《重要告知》，了解并接受其中保险责任、责任免除、投保人、被保险人义务、赔偿处理、其他事项等相关约定，同意以此作为订立保险合同的依据。";
     [_term text:text noticeUrl:_noticeUrl protocalUrl:_protocolUrl];
    
    
}
+(BOOL)isInCailm:(int)status{
    return status==1 || status==2;
}
+(BOOL)isCompleteCailm:(int)status{
    return status==3;
}

+(BOOL)isExpire:(int)status{
    return status==4;
}
+(BOOL)isBackcard:(int)status{
    return status==5;
}

+(NSInteger)getAge:(NSString*)value{
    value = [value stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    NSInteger length =0;
    if (!value) {
        return -1;
    }else {
        length = value.length;
        if(length != 18){
            return -1;
        }
    }
    // 省份代码
    NSArray *areasArray =@[@"11",@"12", @"13",@"14", @"15",@"21", @"22",@"23", @"31",@"32", @"33",@"34", @"35",@"36", @"37",@"41", @"42",@"43", @"44",@"45", @"46",@"50", @"51",@"52", @"53",@"54", @"61",@"62", @"63",@"64", @"65",@"71", @"81",@"82", @"91"];
    
    NSString *valueStart2 = [value substringToIndex:2];
    BOOL areaFlag =NO;
    for (NSString *areaCode in areasArray) {
        if ([areaCode isEqualToString:valueStart2]) {
            areaFlag =YES;
            break;
        }
    }
    
    if (!areaFlag) {
        return false;
    }
    
    
    NSRegularExpression *regularExpression;
    NSUInteger numberofMatch;
    
    int year =0;
  
    
    year = [value substringWithRange:NSMakeRange(6,4)].intValue;
    if (year %4 ==0 || (year %100 ==0 && year %4 ==0)) {
        
        regularExpression = [[NSRegularExpression alloc]initWithPattern:@"^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$"
                                                                options:NSRegularExpressionCaseInsensitive
                                                                  error:nil];//测试出生日期的合法性
    }else {
        regularExpression = [[NSRegularExpression alloc]initWithPattern:@"^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$"
                                                                options:NSRegularExpressionCaseInsensitive
                                                                  error:nil];//测试出生日期的合法性
    }
    
  
    
    numberofMatch = [regularExpression numberOfMatchesInString:value
                                                       options:NSMatchingReportProgress
                                                         range:NSMakeRange(0, value.length)];
    if(numberofMatch >0) {
        int S = ([value substringWithRange:NSMakeRange(0,1)].intValue +
                 [value substringWithRange:NSMakeRange(10,1)].intValue) *7 +
        ([value substringWithRange:NSMakeRange(1,1)].intValue +
         [value substringWithRange:NSMakeRange(11,1)].intValue) *9 + ([value substringWithRange:NSMakeRange(2,1)].intValue + [value substringWithRange:NSMakeRange(12,1)].intValue) *10 + ([value substringWithRange:NSMakeRange(3,1)].intValue + [value substringWithRange:NSMakeRange(13,1)].intValue) *5 + ([value substringWithRange:NSMakeRange(4,1)].intValue + [value substringWithRange:NSMakeRange(14,1)].intValue) *8 + ([value substringWithRange:NSMakeRange(5,1)].intValue + [value substringWithRange:NSMakeRange(15,1)].intValue) *4 + ([value substringWithRange:NSMakeRange(6,1)].intValue + [value substringWithRange:NSMakeRange(16,1)].intValue) *2 + [value substringWithRange:NSMakeRange(7,1)].intValue *1 + [value substringWithRange:NSMakeRange(8,1)].intValue *6 + [value substringWithRange:NSMakeRange(9,1)].intValue *3;
        int Y = S %11;
        NSString *M =@"F";
        NSString *JYM =@"10X98765432";
        M = [JYM substringWithRange:NSMakeRange(Y,1)];// 判断校验位
        if ([M isEqualToString:[value substringWithRange:NSMakeRange(17,1)]]) {
            return YES;// 检测ID的校验位
        }
        
    }
return -1;

}


@end
