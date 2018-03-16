//
//  DMBarButtonItem.m
//  DMLib
//
//  Created by randy ren on 16/2/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMBarButtonItem.h"

@implementation DMBarButtonItem

-(id)initWithImage:(UIImage*)image{
    if(self = [super initWithImage:image style:UIBarButtonItemStyleDone target:self action:@selector(onClick)]){
        
    }
    return self;
}

-(id)initWithText:(NSString*)text{
    if(self = [super initWithTitle:text style:UIBarButtonItemStyleDone target:self action:@selector(onClick)]){
        
    }
    return self;

}
-(void)onClick{
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
