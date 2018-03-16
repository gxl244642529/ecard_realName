//
//  CommonButton.h
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>



//页面主button
IBInspectable
@interface PageButton : UIButton

//是否一开始的时候是不可点击的
@property (nonatomic,assign) IBInspectable BOOL initDisabled;

@end

//统统用表单提交按钮
@interface CommonButton : DMSubmitButton

+(void)createBackground:(UIView*)view;

@end
