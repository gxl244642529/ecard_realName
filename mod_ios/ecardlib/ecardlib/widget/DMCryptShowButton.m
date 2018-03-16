//
//  DMCryptShowButton.m
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMCryptShowButton.h"
#import <DMLib/dmlib.h>

@interface DMCryptShowButton()
{
    __weak UITextField* _textField;
}

@end

@implementation DMCryptShowButton

-(void)awakeFromNib{
    
   _textField = [self.superview viewWithTag:_targetTag];
    _textField.secureTextEntry =YES;
    Control_AddTarget(self, onTap);
    
}

-(void)onTap:(id)sender{
    if([self.titleLabel.text isEqualToString:@"显示"]){
        [self setTitle:@"隐藏" forState:UIControlStateNormal];
        _textField.secureTextEntry = NO;
    }else{
        [self setTitle:@"显示" forState:UIControlStateNormal];
        _textField.secureTextEntry =YES;
    }
    
    
}

@end
