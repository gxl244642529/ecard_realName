//
//  SafeUtil.h
//  ecard
//
//  Created by randy ren on 15/12/2.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "TTTAttributedLabel.h"
#import "SafeVo.h"
#import "SafeDetailVo.h"
#import "TermLabel.h"

#define IMAGE_HEIGHT 135



typedef enum _SafeType{
    
    SafeType_Card=1,
    SafeType_Family,
    SafeType_Accdent,
    SafeType_Cashier,
    SafeType_Age,
    SafeType_Car,
    
}SafeType;

@interface SafeUtil : NSObject

+(NSString*)getUrl:(NSString*)url;

+(BOOL)isQuanjiabao:(SafeVo*)data;

+(void)setSafeGuard:(NSString*)guard detailView:(UIView*)detailView  detailHeight:(NSLayoutConstraint*)detailHeight;


+(void)setTerm:(TermLabel*)_term noticeUrl:(NSString*)_noticeUrl protocalUrl:(NSString*)_protocolUrl;
+(void)setTermForResult:(TermLabel*)_term noticeUrl:(NSString*)_noticeUrl protocalUrl:(NSString*)_protocolUrl;


+(BOOL)isInCailm:(int)status;
+(BOOL)isCompleteCailm:(int)status;
+(BOOL)isExpire:(int)status;
+(BOOL)isBackcard:(int)status;

//从身份证获取age
+(NSInteger)getAge:(NSString*)idCard;

@end
