//
//  Alert.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DMMacro.h"


#define ALERT_BUTTON_YES 1
#define ALERT_BUTTON_NO 0

@interface AlertDelegate : NSObject<UIAlertViewDelegate>

+(AlertDelegate*)create;

BLOCK_PROPERTY(confirmListener,NSInteger buttonIndex)

-(void)show:(NSString*)message title:(NSString*)title;

-(void)showAlert:(NSString*)message title:(NSString*)title;

-(void)show:(NSString*)message title:(NSString*)title yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton;

@end



@interface Alert : NSObject
+(void)confirm:(NSString*)message title:(NSString*)title confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex);

+(void)confirm:(NSString*)message title:(NSString*)title yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex);

+(void)confirm:(NSString*)title confirmListener:BLOCK_PARAM(confirmListener,NSInteger buttonIndex);

+(void)confirm:(NSString*)message yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton delegate:(id)delegate withTitle:(NSString*)title;
+(void)confirm:(NSString*)message title:(NSString*)title delegate:(id)delegate tag:(NSInteger)tag;
+(void)confirm:(NSString*)message title:(NSString*)title delegate:(id)delegate;

+(void)confirm:(NSString*)message yesButton:(NSString*)yesButton cancelButton:(NSString*)cancelButton delegate:(id)delegate withTitle:(NSString*)title tag:(NSInteger)tag;

+(void)alert:(NSString*)message;
+(void)alert:(NSString*)message delegate:(id)delegate;


+(void)toast:(NSString*)message;
+(void)alert:(NSString*)message title:(NSString*)title dialogListener:BLOCK_PARAM(dialogListener,NSInteger buttonIndex);
+(void)wait:(NSString*)message;
+(void)wait:(id)view message:(NSString*)message;
+(void)cancelWait;
+(void)cancelWait:(id)view;
@end
