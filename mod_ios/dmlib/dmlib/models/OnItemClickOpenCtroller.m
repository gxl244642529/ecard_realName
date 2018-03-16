//
//  OnItemClickOpenCtroller.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "OnItemClickOpenCtroller.h"
#import "DMViewController.h"
#import "DMJobManager.h"

@interface OnItemClickOpenCtroller()
{
    Class _controllerClass;
    __weak id<DMControllerDelegate> _delegate;
    
}

@end

@implementation OnItemClickOpenCtroller

-(void)dealloc{
    _controllerClass = nil;
}

-(id)initWithControllerName:(NSString*)controllerName  delete:(id<DMControllerDelegate>)delegate{
    if(self = [super init]){
        _controllerClass  = NSClassFromString(controllerName);
        _delegate = delegate;
    }
    return self;
}

-(void)onItemClick:(UIView *)parent data:(NSObject *)data index:(NSInteger)index{
    
    DMViewController* controller = [[_controllerClass alloc]init];
    controller.data = data;
    controller.controllerDelegate = _delegate;
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
    
    
}

@end
