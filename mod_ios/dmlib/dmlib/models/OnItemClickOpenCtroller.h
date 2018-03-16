//
//  OnItemClickOpenCtroller.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IDataAdapterListener.h"
#import "OnItemClickListener.h"
#import "DMControllerDelegate.h"

@interface OnItemClickOpenCtroller : OnItemClickListener
-(id)initWithControllerName:(NSString*)controllerName delete:(id<DMControllerDelegate>)delegate;
@end
