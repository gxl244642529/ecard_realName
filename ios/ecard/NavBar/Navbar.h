

//
//  Created by YeJian on 13-8-12.
//  Copyright (c) 2013年 YeJian. All rights reserved.
//

#define SysNavbarHeight 44


#import <UIKit/UIKit.h>


@interface Navbar : UINavigationBar

 /**< 适用于ios7*/
//@property (nonatomic,strong)UIColor *stateBarColor;/**< 默认black*/
//@property (nonatomic,assign)UIBarStyle cusBarStyele;/**< 默认UIBarStyleBlackOpaque*/

//- (void)setDefault;


+(void)titleColor:(UIColor*)titleColor titleTextColor:(UIColor*)titleTextColor;

@end


