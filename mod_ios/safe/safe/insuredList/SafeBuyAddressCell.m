//
//  SafeBuyAddressCell.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyAddressCell.h"

@implementation SafeBuyAddressCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [_addr addTarget:self action:@selector(onEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
}
-(void)onEndEdit:(id)sender{
    [sender resignFirstResponder];
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(NSString*)validate:(NSMutableDictionary *)data{
    
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
