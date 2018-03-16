//
//  DMLoginCaller.m
//  DMLib
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMLoginCaller.h"

@interface DMSegueLoginCaller()
{
    NSString* _identifier;
    __weak UIStoryboard* _storyboard;
}

@end


@implementation DMSegueLoginCaller

-(void)dealloc{
    _identifier = nil;
}

-(id)initWithId:(NSString*)identifier storyboard:(UIStoryboard*)storyboard{
    if(self = [super init]){
        _storyboard = storyboard;
        _identifier = identifier;
    }
    return self;
}


-(UIViewController<DMLoginController>*)callLoginController{
    UIViewController<DMLoginController>* result = [_storyboard instantiateViewControllerWithIdentifier:_identifier];
    return result;
}

@end

