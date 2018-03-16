//
//  MyDelegate.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMDelegate.h"
#import "DMUncaughtExceptionHandler.h"
#import "DMJobManager.h"
#import "MobClick.h"

@interface DMDelegate()
{
     DMJobManager* _taskManager;
}
@end

@implementation DMDelegate
-(DMJobManager*)manager{
    return _taskManager;
}

-(void)dealloc{
    
}

-(void)initYoumen:(NSString*)key{
    [MobClick startWithAppkey:key reportPolicy:BATCH channelId:nil];
    NSString *version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    [MobClick setAppVersion:version];

}

-(void)createWithServers:(NSArray*)servers rootController:(DMViewController*)rootController{
  //  InstallUncaughtExceptionHandler();
    _taskManager = [[DMJobManager alloc]init];
    NSInteger index = 0;
    for (NSString* server in servers) {
        [_taskManager registerServer:index++ url:server];
    }
    _taskManager.rootViewController = rootController;
    
    
        //[MobClick updateOnlineConfig];  //在线参数配置
  /*  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onlineConfigCallBack:) name:UMOnlineConfigDidFinishedNotification object:nil];
    */
}



- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
   _taskManager = nil;
}

@end
