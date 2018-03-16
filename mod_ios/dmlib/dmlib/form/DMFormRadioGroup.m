//
//  FormRadio.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormRadioGroup.h"
#import "DataUtil.h"
#import "UIView+ViewNamed.h"

@interface DMFormRadioGroup()
{
    NSArray<NSString*>* _valuesArray;
}

@end



@implementation DMFormRadioGroup


-(void)dealloc{
    self.defaultValue = nil;
    self.values = nil;
    _valuesArray = nil;
}


-(void)awakeFromNib{
    [super awakeFromNib];
    NSInteger selectedIndex = -1;
    NSInteger index = 0;
    if(_values){
        _valuesArray = [_values componentsSeparatedByString:@","];
        if(_defaultValue){
            
            for (NSString* str in _valuesArray) {
                if([str isEqualToString:_defaultValue]){
                    //选择这个
                    selectedIndex = index;
                    break;
                }
                ++index;
            }
        }
    }
    index = 0;
    for (UIButton* button in self.subviews) {
        button.tag = index;
        if(index == selectedIndex){
            button.selected = YES;
        }
        [button addTarget:self action:@selector(onTap:) forControlEvents:UIControlEventTouchUpInside];
        index ++;
    }
    
}
-(void)onTap:(UIButton*)button{
    NSInteger index = 0;
    for (UIButton* btn in self.subviews) {
        if(btn == button){
            btn.selected = YES;
            [_delegate formRadio:self selectChanged:_valuesArray[index] index:index];
        }else{
            btn.selected = NO;
        }
        index ++;
    }
}

-(void)setVal:(id)val{
    NSInteger index = 0;
    NSString* dataValue = val;
    for (NSString* str in _valuesArray) {
        UIButton* button = self.subviews[index];
        if([str isEqualToString:dataValue]){
            button.selected = YES;
        }else{
            button.selected = NO;
        }
        index++;
    }
}

-(id)val{
    NSInteger index = 0;
    for (UIButton* button in self.subviews) {
        if(button.selected){
            return _valuesArray[index];
        }
        index ++;
    }
    return nil;
}

-(NSString*)validate:(NSMutableDictionary *)data{
    id value = [self val];
    if(value){
        [data setValue:value forKey:self.viewName] ;
        return nil;
    }
    
    //不然没有值
    [data removeObjectForKey:self.viewName];
    if(_required){
        return @"请选择";
    }
    return nil;
}



@end
