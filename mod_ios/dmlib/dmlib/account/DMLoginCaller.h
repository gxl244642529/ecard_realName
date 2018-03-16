//
//  DMLoginCaller.h
//  DMLib
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 damai. All rights reserved.
//
#import <UIKit/UIKit.h>
#import "DMLoginController.h"

@protocol DMLoginCaller <NSObject>

//-(UIViewController<DMLoginController>*)callLoginController;
-(void)callLoginController:(id<DMLoginDelegate>)delegate;

@end



@interface DMSegueLoginCaller : NSObject<DMLoginDelegate>


-(id)initWithId:(NSString*)identifier storyboard:(UIStoryboard*)storyboard;

@end

