//
//  FormModel.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>


@protocol IFormListener <NSObject>

-(void)onFormError:(NSString*)error;
-(void)onFormSubmit:(NSDictionary*)data;

@end

#define RULES_REQUIRED 0x1
#define RULES_EMAIL 0x2
#define RULES_PHONE 0x4
#define RULES_PASSWORD 0x8

@interface FormModel : NSObject


-(id)initWithListener:(NSObject<IFormListener>*)listener;

//单选、选中（是否，如是否同意协议
-(void)add:(UIButton*)checkButton name:(NSString*)name;
-(void)add:(UITextField*)textField name:(NSString*)name rules:(NSInteger)rules;
-(void)add:(UITextField*)textField name:(NSString*)name rules:(NSInteger)rules returnKeyType:(UIReturnKeyType)returnKeyType;

-(void)addOkButton:(UIButton*)button;
-(void)resignFirstResponder;
-(BOOL)validate;
-(NSMutableDictionary*)formData;

@end
