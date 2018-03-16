
//
//  ECardUserInfo.m
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardUserInfo.h"

#import "EBusinessServerHandler.h"



#import <dmlib/dmlib.h>


@implementation ECardUserInfo
+(void)loginSuccess:(DMApiJob*)request account:(NSString*)account pwd:(NSString*)pwd{
    NSDictionary* result = request.data;
    
    int code = [result[@"code"]intValue];
    if(code==0){
        ECardUserInfo* userInfo = [[ECardUserInfo alloc]init];
        userInfo.userID = result[@"userid"];
        userInfo.userAccount =  account;
        userInfo.userPwd =  pwd;
        userInfo.bg = [DataUtil getString:result key:@"bg"];
        userInfo.headImage = [DataUtil getString:result key:@"head"];
        userInfo.userPhone = [DataUtil getString:result key:@"phone"];
        userInfo.userHash = result[@"hash"];
        //保存
        NSDictionary* d = [result valueForKey:@"token"];
        [[EBusinessServerHandler runningInstance]setToken:d];
        //编码
        NSMutableDictionary* dic = [[NSMutableDictionary alloc]initWithDictionary:result];
        [dic setValue:[NSString stringWithFormat:@"%@", result[@"userid"] ] forKey:@"userid"];
        [dic setValue:account forKey:@"account"];
        
        userInfo.data = dic;
        
        //
        [DMAccount onLoginSuccess:userInfo];

    }else if(code==1){
        //弹出手机验证码的框框
        
        
    }else if(code==2){
        //弹出验证码的框框
        
        
        
    }
    
    
    
   
}


-(void)save{
    
    //保存一下
    
    [super save];
    
}

+(void)logout{
    if([DMAccount isLogin]){
        [[DMAccount current] logout];
    }else{
        [[DMNotifier notifier]notifyObservers:@"n_n_logout" data:nil];
    }
    [PushUtil onLogout];
}
-(NSDictionary*)toJson{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    
    NSError* error;
    NSData* data = nil;
    if(self.data){
        data = [NSJSONSerialization dataWithJSONObject:self.data
                                        options:0
                                          error:&error];
        NSString* str = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        [dic setValue:str forKey:@"json"];
    }
    
    return dic;
}

@end
