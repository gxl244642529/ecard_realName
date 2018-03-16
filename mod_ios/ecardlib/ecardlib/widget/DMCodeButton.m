//
//  DMCodeButton.m
//  ecard
//
//  Created by randy ren on 16/1/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMCodeButton.h"

@interface DMCodeButton()
{
    NSTimer* _sendTimer;
    NSInteger _time;
}

@end


@implementation DMCodeButton

-(void)dealloc{
    if(_sendTimer){
        [_sendTimer invalidate];
        _sendTimer = nil;
    }

}

-(void)doSubmit:(NSMutableDictionary *)dic{
    [super doSubmit:dic];
     _txtPhone.enabled = NO;
}



-(void)awakeFromNib{
    [super awakeFromNib];
    self.apiJob.delegate = self;
}

-(void)setBtnSubmit:(UIButton *)btnSubmit{
    _btnSubmit = btnSubmit;
    btnSubmit.enabled = NO;
}


-(void)setTxtCode:(UITextField *)txtCode{
    _txtCode = txtCode;
    [_txtCode addTarget:self action:@selector(onChanged:) forControlEvents:UIControlEventEditingChanged];
}



- (void)onChanged:(id)sender {
    if(_txtCode.text.length != 6){
        [_btnSubmit setEnabled:NO];
    }else{
        [_btnSubmit setEnabled:YES];
    }
}


-(void)onTimer:(id)sender{
    _time -- ;
    if(_time==0){
        [_sendTimer invalidate];
        _sendTimer = nil;
        
        [self setTitle:@"获取验证码" forState:UIControlStateNormal];
        self.enabled = YES;
    }else{
        NSString* label = [NSString stringWithFormat:@"(%ld)重新获取",(long)_time] ;
        self.titleLabel.text =label;
        [self setTitle:label forState:UIControlStateNormal];
    }
}


-(BOOL)jobError:(id)request{
    _txtPhone.enabled = YES;
    return NO;
}
-(BOOL)jobMessage:(DMApiJob*)request{
    
    _txtPhone.enabled = YES;
    return NO;
}
-(void)jobSuccess:(DMApiJob*)request{
    [Alert toast:@"获取验证码成功"];
    self.enabled = NO;
    _time = 60;
    //开始倒计时
    _sendTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(onTimer:) userInfo:nil repeats:YES];
    
    self.verifyId =[NSString stringWithFormat:@"%@",request.data];
    
}
@end
