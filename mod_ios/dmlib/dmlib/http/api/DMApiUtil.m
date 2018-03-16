//
//  DMApiUtil.m
//  DMLib
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMApiUtil.h"
#import "DMEnable.h"

@implementation DMApiUtil
+(void)enableButton:(__kindof UIView*)button{
    if([button isKindOfClass:[UIButton class]]){
        UIButton* view = button;
        view.enabled = YES;
    }else if([button conformsToProtocol:@protocol(DMEnable)]){
        NSObject<DMEnable>* enable = (NSObject<DMEnable>*)button;
        enable.enabled = YES;
    }else{
        button.userInteractionEnabled = YES;
    }
}
+(void)disableButton:(__kindof UIView*)button{
    if([button isKindOfClass:[UIButton class]]){
        UIButton* view = button;
        view.enabled = NO;
    }else if([button conformsToProtocol:@protocol(DMEnable)]){
        NSObject<DMEnable>* enable = (NSObject<DMEnable>*)button;
        enable.enabled = NO;
    }else{
        button.userInteractionEnabled = NO;
    }

}
@end
