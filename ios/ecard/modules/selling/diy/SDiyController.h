//
//  SDiyController.h
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "FileArrayJsonTask.h"
#import "ECardTaskManager.h"
#import "PopupWindow.h"
#import "SDiyFController.h"
#import <ecardlib/ecardlib.h>

@interface SDiyController : MyViewController<UIAlertViewDelegate,UIActionSheetDelegate,SDiyFControllerDelegate,CLImageEditorDelegate,IRequestResult>

-(void)setModel:(FileArrayJsonTask*)model;


@end
