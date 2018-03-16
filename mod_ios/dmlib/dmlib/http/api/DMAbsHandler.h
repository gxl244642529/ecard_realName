//
//  DMAbsHandler.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMJob.h"


@interface DMAbsHandler : NSObject<DMJobHandler,DMJobCtrl>
{
    __kindof DMJob* _currentJob;
}
-(void)handleTaskImpl:(id)task;
//停止当前任务
-(void)cancelProgress;
@end
