//
//  ECardCaller.m
//  ecardlib
//
//  Created by 任雪亮 on 16/9/24.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "ECardCaller.h"
#import <dmlib/dmlib.h>
#import "LoginController.h"
#import "DMAliPay.h"
#import "DMWXPay.h"
#import "MainController.h"
#import "RootController.h"
#import "ECardPayCashierController.h"
#import "WXApi.h"
#import "DMCMBPay.h"
#import "ECardPay.h"
#import "UmaPay.h"
#import "EBusinessServerHandler.h"

@interface ECardPlatform : NSObject<DMLoginCaller,DMPayFactory,DMServerRegisterDelegate>

@end


ECardPlatform* platform;
DMJobManager* _taskManager;


@implementation ECardPlatform

-(void)callLoginController:(id<DMLoginDelegate>)delegate{
    
    LoginController* controller = [[LoginController alloc]init];
    controller.delegate = delegate;
    
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
}
-(DMViewController*)createPayCashier:(Class)payCashierClass{
    if(payCashierClass){
        return [[payCashierClass alloc]init];
    }
    return [[ECardPayCashierController alloc]init];
}
-(NSArray*)registerServerHandler:(DMApiHandler*)handler parsers:(NSArray<id<DMApiParser>>*)parsers delegate:(id<DMApiDelegate>) delegate cache:(id<DMCache>)cache{
    
    
    PhpServerHandler* phpServerHandler = [[PhpServerHandler alloc]init];
    [phpServerHandler initParam:parsers delegate:delegate cache:cache];
    [handler registerServerHandler:0 handler:phpServerHandler];


    EBusinessServerHandler* javaHandler = [[EBusinessServerHandler alloc]init];
    [javaHandler initParam:parsers delegate:delegate cache:cache];
    [handler registerServerHandler:1 handler:javaHandler];
   
     /*
    JavaServerHandler* javaHandler = [[JavaServerHandler alloc]init];
    [javaHandler initParam:parsers delegate:delegate cache:cache];
    [handler registerServerHandler:1 handler:javaHandler];
*/
    
   return @[@"php",@"java"];
    
    
    
}
-(DMPay*)createPay:(DMPayType)type{
    switch (type) {
        case DMPAY_ALIPAY:
            return [[DMAliPay alloc]init];
        case DMPAY_WEIXIN:
            return [[DMWXPay alloc]init];
        case DMPAY_ETONGKA:
            return [[ECardPay alloc]init];
        case DMPAY_CMB:
            return [[DMCMBPay alloc]init];
        case DMPAY_UMA:
            return [[UmaPay alloc]init];
        default:
            return nil;
    }
}

@end
@interface ECardCaller()
{
   
}

@end


@implementation ECardCaller


+(void)setRootController:(DMViewController *)controller{
     _taskManager.rootViewController = controller;
}



+(void)callECard:(UIViewController*)parent account:(NSString*)account{
    if(!platform){
        platform  = [[ECardPlatform alloc]init];
    }
    
    //加载application
    _taskManager = [[DMJobManager alloc]initWithRegisterServerHandler:platform];
    _taskManager.rootViewController = parent;
    _taskManager.factory = platform;
    //开始
    _taskManager.loginCaller = platform;
    [DMAccount setLoginCaller:platform];
    [WXApi registerApp:_taskManager.wxId withDescription: @"" ];
    

}
+(UINavigationController*)createMain{
    MainController* main = [[MainController alloc]init];
    
    return main;
    
  //  RootController* root = [[RootController alloc]initWithRootViewController:main];
   // return root;
}
+(BOOL)handleOpenURL:(NSURL*)url{
    if(![DMPayModel runningInstance]){
        return false;
    }
    return [[DMPayModel runningInstance]handleOpenUrl:url];
}
@end
