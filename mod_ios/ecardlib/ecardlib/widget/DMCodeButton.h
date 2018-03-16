//
//  DMCodeButton.h
//  ecard
//
//  Created by randy ren on 16/1/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>

/**
 获取验证码,带倒计时功能
 当调用成功以后,调用成功以后,将对应的手机号码的输入框设置为不可以编辑
 */
IB_DESIGNABLE
@interface DMCodeButton : DMSubmitButton<DMApiDelegate>


//提交按钮
@property (weak,nonatomic) UIButton* btnSubmit;

//手机
@property (weak,nonatomic) UITextField* txtPhone;
//验证码
@property (weak,nonatomic) UITextField* txtCode;

@property (nonatomic,copy) NSString* verifyId;

@end
