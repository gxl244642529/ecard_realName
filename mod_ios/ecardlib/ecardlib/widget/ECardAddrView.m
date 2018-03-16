//
//  ECardAddrView.m
//  ecard
//
//  Created by randy ren on 16/2/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardAddrView.h"
#import "ViewUtil.h"

@interface ECardAddrView()
{
    __weak UITextField* _text;
    __weak ECardAreaSelector* _selector;
}

@end

@implementation ECardAddrView

-(void)awakeFromNib{
    _selector = self.subviews[0];
    _text = [ViewUtil findView:self.subviews[1] class:[UITextField class]];
    [_text addTarget:self action:@selector(onEnd:) forControlEvents:UIControlEventEditingDidEndOnExit];
    
}

-(void)onEnd:(id)sender{
    [sender resignFirstResponder];
}


-(NSString*)validate:(NSMutableDictionary*)data{
    
    if(!_selector.province){
        return @"请选择省市区";
    }
    
    
    if([_text.text isEqualToString:@""]){
        return @"请输入详细地址";
    }
    
    
    data[self.viewName] = self.val;
    return nil;
}
-(id)val{
    return [NSString stringWithFormat:@"%@%@",[_selector getArea],_text.text];
    
}
-(void)setVal:(id)val{
    
}

@end
