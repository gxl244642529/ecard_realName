//
//  DMViewInfoCreater.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMViewInfo.h"

@protocol DMViewInfoCreater <NSObject>


//根据视图创建,只有在第一次的时候会调用
-(DMViewInfo*)createViewInfo:(__kindof UIView*)view;
@end


//默认
//////////////////////////////
@interface DMViewInfoCreater : NSObject<DMViewInfoCreater>


@end

//////////////////////////////
@interface DMBgViewInfoCreater : NSObject<DMViewInfoCreater>

@end


//////////////////////////////
@interface DMImageInfoInfoCreater : NSObject<DMViewInfoCreater>

@end
//////////////////////////////
@interface DMHiddenInfoInfoCreater : NSObject<DMViewInfoCreater>

@end

//////////////////////////////
@interface DMLocalImgInfoCreater : NSObject<DMViewInfoCreater>

@end
