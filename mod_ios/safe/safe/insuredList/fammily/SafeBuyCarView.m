//
//  SafeBuyCarView.m
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyCarView.h"

@implementation SafeBuyCarView


-(void)awakeFromNib{
    [_carNo addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [_carFrame addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}
-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}
-(NSString*)validate:(NSMutableDictionary*)data{
    if([_carNo.text isEqualToString:@""]){
        return @"请输入车牌号";
    }
    
    if([_carFrame.text isEqualToString:@""]){
        
        return @"请输入车架号";
    }
    
    
    data[@"carNo"]=_carNo.text;
    data[@"carFrame"]=_carFrame.text;
    
    return nil;
    
    
}


@end
