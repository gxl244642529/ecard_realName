//
//  OnClickListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>


#import "SimpleViewController.h"
#import "BaseViewController.h"
#import <ecardlib/ecardlib.h>
@interface OnClickListenerEx : NSObject


+(OnClickListener*)parent:(UIViewController*)controller clsRef:(Class)clsRef;
+(OnClickListener*)parent:(UIViewController*)controller nibName:(NSString*)nibName;
+(OnClickListener*)listener:(UIViewController<IViewControllerListener>*)listener;
@end


//内容视图
@interface OnItemClickListenerContentView : OnClickListener
-(id)initWithParentAndContentNibName:(UIViewController*)controller nibName:(NSString*)nibName;
@end
//内容视图，表单页面





//////////////
@interface OnItemClickListenerOpenForResult : OnClickListener
-(id)initWithParent:(BaseViewController*)parent clsRef:(Class)clsRef requestCode:(NSInteger)requestCode;
@end
