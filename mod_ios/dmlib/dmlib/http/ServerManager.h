//
//  ServerManager.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NetworkStatus.h"


@interface ServerManager : NSObject


-(void)registerServer:(NSString*)url;
-(NSString*)getUrl:(NSInteger)index;
-(NetworkStatus)getStatus:(NSInteger)index;



-(void)start;
-(void)stop;


@end
