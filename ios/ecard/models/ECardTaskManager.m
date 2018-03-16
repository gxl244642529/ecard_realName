//
//  ECardTaskManager.m
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ECardTaskManager.h"
#import "BaseViewController.h"
#import <ecardlib/ecardlib.h>
#import "JsonUtil.h"
#define USER_PHONE @"userPhone"
//我的收藏的ids版本号
#define MY_COUPON_IDS_VERSION @"fcoupon_ids_v"
#define MY_BUSINESS_IDS_VERSION @"fbusiness_ids_v"

@interface ECardTaskManager()
{
    ObjectJsonTask* _uploadDeviceToken;
}

@end

@implementation ECardTaskManager

static ECardTaskManager* g_instance;

+(id<IJsonTaskManager>)sharedInstance{
    if(g_instance == nil){
        g_instance = [[ECardTaskManager alloc]init];
        [ECardTaskManager setInstance:g_instance];
    }
    return g_instance;
}


-(void)readImpl:(ECardUserInfo *)userInfo def:(NSUserDefaults *)userDefaults{
   userInfo.bg= [userDefaults valueForKey:@"bg"];
     userInfo.userPhone= [userDefaults valueForKey:USER_PHONE];
     userInfo.headImage= [userDefaults valueForKey:@"headImage"];
}

-(void)saveUserInfoImpl:(ECardUserInfo *)userInfo def:(NSUserDefaults *)userDefaults{
    [userDefaults setValue:userInfo.userPhone forKey:USER_PHONE];
    [userDefaults setValue:userInfo.bg forKey:@"bg"];
    [userDefaults setValue:userInfo.headImage forKey:@"headImage"];
}

+(void)setImageSrc:(UIImageView*)imageView src:(NSString*)url{
    [g_instance setImageSrc:imageView src:url];
}


-(void)onLoginSuccess:(NSDictionary*)result userInfo:(ECardUserInfo *)userInfo{
    userInfo.userID = [JsonUtil getString:result key:@"userid"];
    userInfo.userPhone = [JsonUtil getString:result key:@"phone"];//result[@"phone"];
    
}

-(void)uploadDeviceToken:(NSString *)deviceToken{
    if(!_uploadDeviceToken){
        _uploadDeviceToken = [self createObjectJsonTask:@"update_push_id" cachePolicy:DMCachePolicy_NoCache];
        __weak ECardTaskManager* __self = self;
        _uploadDeviceToken.successListener = ^(id result){
           
        };
    }
    [_uploadDeviceToken put:@"pushID" value:[self getPushID]];
    [_uploadDeviceToken execute];
}



-(NSString*)getLoginApi{
/*#ifdef MY_DEBUG
    return @"pass_api/login";
#else
    return @"login";
#endif
 */
    
    return @"pass_api/login";
}

-(ECardUserInfo*)createUserInfo{
    return [[ECardUserInfo alloc]init];
}


-(void)beforeLogin:(JsonTask*)task account:(NSString*)userName password:(NSString*)password{
//#ifdef MY_DEBUG
    [task put:@"username" value:userName];
    [task put:@"pwd" value:password];
    [task put:@"platform" value:PLATFORM];
    [task put:@"version" value:APP_VERSION];
    [task put:@"pushID" value:[self getPushID]];
   /* #else
    [task put:@"username" value:userName];
    [task put:@"password" value:password];
    [task put:@"platform" value:PLATFORM];
    [task put:@"version" value:@ECARD_VERSION_CODE];
    [task put:@"pushID" value:[self getPushID]];
    #endif*/
}

@end
