//
//  SafeBuyAddressView.m
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyAddressView.h"




@implementation SafeBuyAddressView


-(void)awakeFromNib{
     [_addr addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}

-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}
-(NSString*)validate:(NSMutableDictionary*)data{
    if(!_area.city){
        return @"请选择区域";
    }
    
    if([_addr.text isEqualToString:@""]){
        return @"请输入详细地址";
    }
    
    data[@"addr"] = [NSString stringWithFormat:@"%@%@",[_area getArea],_addr.text];
    
    return nil;
    
}

@end
