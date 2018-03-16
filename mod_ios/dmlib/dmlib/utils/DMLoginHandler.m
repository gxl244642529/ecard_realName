//
//  DMLoginHandler.m
//  DMLib
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMLoginHandler.h"

@interface DMLoginHandler()
{
    __weak UIViewController* _parent;
}

@end

@implementation DMLoginHandler


-(id)initWithButton:(UIButton*)button parent:(UIViewController*)parent{
    if(self = [super init]){
        [button addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
        _parent = parent;
    }
    return self;
}

-(void)onClick:(id)sender{
    if(![DMAccount isLogin]){
        [DMAccount callLoginController:self];
    }else{
        if(_segueName){
            [_parent performSegueWithIdentifier:_segueName sender:nil];
        }else{
            [_parent.navigationController pushViewController:[[_controllerClass alloc]init] animated:YES];
        }
    }

}
-(DMPopType)onLoginSuccess{
    [_parent.navigationController popToViewController:_parent animated:NO];
    if(_segueName){
        [_parent performSegueWithIdentifier:_segueName sender:nil];
    }else{
        [_parent.navigationController pushViewController:[[_controllerClass alloc]init] animated:YES];
    }
    
    return DMPopByDelegate;
}

@end
