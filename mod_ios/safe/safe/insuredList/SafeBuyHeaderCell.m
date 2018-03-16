//
//  SafeBuyHeaderCell.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyHeaderCell.h"


@implementation SafeBuyHeaderCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [_name addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [_idCard addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [_phone addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}
-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}


-(NSString*)validate:(NSMutableDictionary*)data{
    
    if([_name.text isEqualToString:@""]){
        return @"请输入姓名";
    }
    if([_idCard.text isEqualToString:@""]){
        return @"请输入身份证号";
    }
   /* if(_idCard.text.length!=18){
        return @"请输入正确的二代身份证号";
    }
    
    if(![ValidateUtil isIdCard:_idCard.text]){
         return @"请输入正确的二代身份证号";
    }*/
    
    if([_phone.text isEqualToString:@""]){
        return @"请输入手机号码";
    }
    
    
    data[@"name"] = _name.text;
    data[@"idCard"] = _idCard.text;
    data[@"phone"] = _phone.text;
    
    return nil;
}

@end
