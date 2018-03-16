//
//  DMAccounts.m
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMAccount.h"
#import "DMJobManager.h"
#import "Alert.h"
#import "ReflectUtil.h"
#import "DMNotifier.h"
#import "PushUtil.h"


DMAccount* g_account;
Class _accountClass;
__weak id<DMLoginCaller> _caller;

@implementation DMAccount

+(void)setLoginCaller:(id<DMLoginCaller>)caller{
    _caller = caller;
}
+(void)setAccountClass:(Class)clazz{
    _accountClass = clazz;
}
+(void)callLoginController:(id<DMLoginDelegate>)delegate{
/*    UIViewController<DMLoginController>* controller = [_caller callLoginController];
    controller.delegate = delegate;
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];*/
  [_caller callLoginController:delegate];
}
-(NSDictionary*)toJson{
  NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
  [dic setValue:self.userID forKey:@"userid"];
  [dic setValue:self.userAccount forKey:@"account"];
  return dic;
}

+(__kindof DMAccount*)current{
  if(!g_account){
    [DMAccount loadAccount];
  }
  return g_account;
}


+(void)onLoginSuccess:(DMAccount*)account{
    g_account = account;
    [account save];
    [PushUtil onLoginSuccess:account];
    [[DMNotifier notifier]notifyObservers:@"n_n_loginSuccess" data:account];
}

+(BOOL)loadAccount{
    //这里加载
    //加载
    NSUserDefaults* df = [NSUserDefaults standardUserDefaults];
    id valaue = [df valueForKey:@"Account_userID"];
    if(!valaue){
        //表示没有用户
        return FALSE;
    }
    DMAccount* account = [[_accountClass alloc]init];
    [account load:df];
    g_account = account;
    return TRUE;
}
+(BOOL)isLogin{
    return g_account!=NULL;
}
+(void)onLoginFail:(__weak id<DMLoginDelegate>)listener{
    dispatch_async(dispatch_get_main_queue(), ^{
        //if wait ,stop it
        [Alert cancelWait];
        [Alert alert:@"您的登录信息已经过期,请重新登录" title:@"温馨提示" dialogListener:^(NSInteger buttonIndex) {
            
            [DMAccount callLoginController:listener];
            
        }];

    });
}

-(void)load:(NSUserDefaults*)df{
  NSArray* arr = [ReflectUtil propertyKeys:[self class]];
  for (NSString* key in arr) {
      id value = [df valueForKey:[NSString stringWithFormat:@"Account_%@",key]];
      if([key isEqualToString:@"data"]){
          
          if(value){
              
              NSError* error = nil;
              NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:[value dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:&error];
              resultMap = [[NSMutableDictionary alloc]initWithDictionary:resultMap];
               [self setValue: resultMap forKey:key];
          }
      }else{
           [self setValue:value forKey:key];
      }
      
     
  }
}

-(void)save{
    NSUserDefaults* df = [NSUserDefaults standardUserDefaults];
    NSArray* arr = [ReflectUtil propertyKeys:[self class]];
    for (NSString* key in arr) {
#ifdef DEBUG
        NSLog(@"Account_%@:%@",key,[self valueForKey:key]);
#endif
        id value = [self valueForKey:key];
        if([value isKindOfClass:[NSDictionary class]]){
            NSError* error;
            NSData* data = [NSJSONSerialization dataWithJSONObject:value
                                                           options:0
                                                             error:&error];
            NSString* str = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            if(error){
                continue;
            }
            [df setValue:str forKey:[NSString stringWithFormat:@"Account_%@",key]];
        }else{
            [df setValue:value forKey:[NSString stringWithFormat:@"Account_%@",key]];
        }
    }
    [df synchronize];
}
-(void)logout{
    
    
    //保存状态0.
    NSUserDefaults* df = [NSUserDefaults standardUserDefaults];
   NSArray* arr = [ReflectUtil propertyKeys:[self class]];
    for (NSString* key in arr) {
#ifdef DEBUG
        NSLog(@"Account_%@:%@",key,[self valueForKey:key]);
#endif
        [df removeObjectForKey:[NSString stringWithFormat:@"Account_%@",key]];
    }
    [df synchronize];
    g_account = nil;
    //登出
     [[DMNotifier notifier]notifyObservers:@"n_n_logout" data:nil];
    
    [[DMJobManager sharedInstance]logout];
    
    
}

@end
