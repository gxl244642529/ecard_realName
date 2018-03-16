
//
//  BaseDetailInfoView.m
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "BaseDetailInfoView.h"

@interface BaseDetailInfoView ()
{
    NSString* _selfName;
}

@end

@implementation BaseDetailInfoView
-(void)dealloc{
    _selfName = nil;
}
-(void)awakeFromNib{
    [super awakeFromNib];
    _selfName = @"被保险人";
    _name = [self viewWithTag:10];
    _idCard = [self viewWithTag:11];
    [_name addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
     [_idCard addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}


-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}

-(NSString*)validate{
    if([_name.text isEqualToString:@""]){
        return [NSString stringWithFormat:@"%@姓名不能为空",_selfName];
    }
    if([_idCard.text isEqualToString:@""]){
        return [NSString stringWithFormat:@"%@身份证号不能为空",_selfName];
    }
   /* if(_idCard.text.length!=18){
        return [NSString stringWithFormat:@"%@身份证必须为正确的二代身份证号码",_selfName];
    }
    
    if(![ValidateUtil isIdCard:_idCard.text]){
        return [NSString stringWithFormat:@"%@身份证必须为正确的二代身份证号码",_selfName];
    }*/
    
    
    return nil;
}
-(void)getInsured:(SafeContact*)dic{
   dic.name = _name.text;
    dic.idCard = _idCard.text;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
