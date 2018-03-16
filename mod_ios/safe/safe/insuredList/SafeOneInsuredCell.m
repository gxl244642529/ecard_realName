//
//  SafeOneInsuredCell.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeOneInsuredCell.h"

@implementation SafeOneInsuredCell

- (void)awakeFromNib {
    [_name addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [_idCard addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}


-(NSString*)validate:(NSMutableDictionary *)data{
    
    //判断是否正确
    if([_name.text isEqualToString:@""]){
        return @"请输入姓名";
    }
    if([_idCard.text isEqualToString:@""]){
        return @"请输入身份证号";
    }
 /*   if(_idCard.text.length!=18){
        return @"请输入正确的二代身份证号";
    }
    
    if(![ValidateUtil isIdCard:_idCard.text]){
        return @"请输入正确的二代身份证号";
    }*/
    
    
    data[@"insured"] = @[@{
        @"name":_name.text,
        @"idCard":_idCard.text
        }];
    
    return nil;
}

-(id)val{
    return nil;
}

-(void)setVal:(SafeContact*)data{
    _name.text = data.name;
    _idCard.text = data.idCard;
}

-(NSArray<NSDictionary*>*)getInsured{
    return @[@{
                 @"name":_name.text,
                 @"idCard":_idCard.text
                 }];
}

-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}
@end
