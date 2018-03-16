//
//  SafeInsuredCell.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredCell.h"
#import "SafeUtil.h"

@interface SafeInsuredCell()
{

}

@end

@implementation SafeInsuredCell

-(NSString*)validate:(SafeContact*)data{
    
    if(_name.text.length==0){
        return @"请输入被保险人姓名";
    }
    
    
    if(_idCard.text.length==0){
        return @"请输入被保险人身份证号";
    }
    
    if(![ValidateUtil isIdCard:_idCard.text]){
        //这里需要判断在哪里
        return @"被保险人身份证号不正确";
    }
    
    

    
    data.name = _name.text;
    data.idCard = _idCard.text;
    
    return nil;
    
}
-(void)awakeFromNib{
    [super awakeFromNib];
    
    _name = [self viewWithTag:10];
    _idCard = [self viewWithTag:11];
    
    [_name addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
     [_name addTarget:self action:@selector(onEditChanged:) forControlEvents:UIControlEventEditingChanged];
    [_idCard addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
     [_idCard addTarget:self action:@selector(onEditChanged:) forControlEvents:UIControlEventEditingChanged];
    
}

-(void)onEditChanged:(id)sender{
    if(sender==_name){
        _data.name = _name.text;
    }else{
        _data.idCard = _idCard.text;
    }
}

-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}
@end
