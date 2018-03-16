//
//  MyNavigationController.m
//  eCard
//
//  Created by randy ren on 14-1-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "MyNavigationController.h"
#import "Navbar.h"
#import "NavBarItem.h"
#import "BaseMacro.h"
@interface MyNavigationController ()

@end

@implementation MyNavigationController

-(id)initWithRootViewController:(UIViewController *)rootViewController
{
    if(IOS7){
        self=[super initWithRootViewController:rootViewController];
        if(self)
        {
            
        }


    }else{
        
        self=[super initWithNavigationBarClass:[Navbar class] toolbarClass:nil];
        if(self)
        {
            self.viewControllers = @[rootViewController];
        }

    }
        return self;
}


@end
