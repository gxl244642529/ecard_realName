//
//  DMItem.h
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMLoginController.h"


IB_DESIGNABLE
@interface DMItem : UIView<DMLoginDelegate>
{
    UIColor* _itemNormalColor;
    __weak UIViewController* _currentController;
}

-(void)onTouch:(id)sender;
-(void)trigger;
-(void)setTarget :(id)target withAction:(SEL)selector;
//高亮颜色
@property (nonatomic,retain) IBInspectable UIColor* highlightColor;

//
@property (nonatomic,assign) IBInspectable BOOL checkLogin;


@property (nonatomic,retain) IBInspectable NSString* controllerName;
@property (nonatomic,retain) IBInspectable NSString* segueName;



@end
