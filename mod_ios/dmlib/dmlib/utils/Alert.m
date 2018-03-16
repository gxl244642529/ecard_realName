//
//  Alert.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "Alert.h"
#import "SVProgressHUD.h"
#import <objc/runtime.h>




AlertDelegate* g_Alert;




@implementation AlertDelegate
+(AlertDelegate*)create{
    if(!g_Alert){
        g_Alert = [[AlertDelegate alloc]init];
    }
    return g_Alert;
}
-(void)dealloc{
    self.confirmListener = nil;
}

-(void)show:(NSString*)message title:(NSString*)title{
    [Alert confirm:message title:title delegate:self];
    
    
}
-(void)showAlert:(NSString*)message title:(NSString*)title{
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}
-(void)show:(NSString*)message title:(NSString*)title yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton{
    [Alert confirm:message yesButton:yesButton cancelButton:cancelButton delegate:self withTitle:title];
}
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(self.confirmListener){
       self.confirmListener(buttonIndex);
    }
   
}

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex{
    self.confirmListener = nil;
}

@end

@implementation Alert

char alertKey;
+(void)confirm:(NSString*)message title:(NSString*)title confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex){
    [Alert confirm:message title:title yesButton:@"确定" cancelButton:@"取消" confirmListener:confirmListener];
}
+(void)confirm:(NSString*)title confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex){
    [Alert confirm:nil title:title yesButton:@"确定" cancelButton:@"取消" confirmListener:confirmListener];
}
+(void)confirm:(NSString*)message title:(NSString*)title yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex){
    
    AlertDelegate* alert = [AlertDelegate create];
    alert.confirmListener = confirmListener;
    [alert show:message title:title yesButton:yesButton cancelButton:cancelButton];
    
}

+(void)alert:(NSString*)message title:(NSString*)title dialogListener:BLOCK_PARAM(dialogListener,NSInteger buttonIndex){
    AlertDelegate* alert = [AlertDelegate create];
    alert.confirmListener = dialogListener;
    [alert showAlert:message title:title];
}
+(void)confirm:(NSString*)message yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton delegate:(id)delegate withTitle:(NSString*)title{
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:title message:message delegate:delegate cancelButtonTitle:cancelButton otherButtonTitles:yesButton, nil];
    [alert show];
}
+(void)confirm:(NSString*)message title:(NSString*)title delegate:(id)delegate tag:(NSInteger)tag{
    [Alert confirm:message yesButton:@"确认" cancelButton:@"取消" delegate:delegate withTitle:title tag:tag];
}
+(void)confirm:(NSString*)message title:(NSString*)title delegate:(id)delegate{
    [Alert confirm:message yesButton:@"确认" cancelButton:@"取消" delegate:delegate withTitle:title];
}
+(void)confirm:(NSString*)message yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton delegate:(NSObject<UIAlertViewDelegate>*)delegate withTitle:(NSString*)title tag:(NSInteger)tag{
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:title message:message delegate:delegate cancelButtonTitle:cancelButton otherButtonTitles:yesButton, nil];
    alert.tag = tag;
    [alert show];
    
}
+(void)alert:(NSString*)message delegate:(id)delegate{
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:@"温馨提示" message:message delegate:delegate cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}
+(void)alert:(NSString*)message
{
    UIAlertView *alert= [[UIAlertView alloc] initWithTitle:@"温馨提示" message:message delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}


+(void)toast:(NSString*)message{
    [SVProgressHUD showSuccessWithStatus:message];
}

+(void)wait:(NSString*)message
{
    [SVProgressHUD showWithStatus:message];
}
+(void)cancelWait
{
    [SVProgressHUD dismiss];
}
+(void)wait:(id)view message:(NSString*)message{
     [SVProgressHUD showSuccessWithStatus:message];
}

+(void)cancelWait:(id)view{
     [SVProgressHUD dismiss];
}

@end

