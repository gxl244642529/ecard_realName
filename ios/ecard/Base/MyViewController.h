//
//  MyViewController.h
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IData.h"
#import "IViewControllerListener.h"
#import "JsonTaskManager.h"
#import "BaseMacro.h"

#import <ecardlib/ecardlib.h>

#import <DMLib/dmlib.h>




@interface MyViewController : DMViewController<IData>


-(UIButton*)createTitleButton:(NSString*)title;
-(UIButton*)createTitleButton:(NSString*)title width:(NSInteger)width;


-(id)initWithData:(id)data;

-(void)backToPrevious:(id)sender;

@end
