//
//  OnItemClickListener.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IOnItemClickListener.h"
@interface OnItemClickListener : NSObject<IOnItemClickListener>
{
    UIViewController* _parent;
}

@end
