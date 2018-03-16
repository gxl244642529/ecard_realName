//
//  FormModel.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "FormModel.h"

#import "CommonMacro.h"


@interface FormModel(){
    __weak NSObject<IFormListener>* _listener;
        //所有表单
    NSMutableArray* arr;
    NSMutableArray* _formRules;
    NSMutableArray* _names;
    NSMutableDictionary* _formData;
    __weak UITextField* _end;
}
@end

@implementation FormModel

-(void)dealloc{
    arr = NULL;
    _formRules = NULL;
    _names=NULL;
    _formData = NULL;
}

-(id)initWithListener:(NSObject<IFormListener>*)listener{
    if(self=[super init]){
        arr = [[NSMutableArray alloc]init];
        _listener = listener;
        _formRules = [[NSMutableArray alloc]init];
        _names = [[NSMutableArray alloc]init];
    }
    return self;
}
-(void)add:(UITextField*)textField name:(NSString*)name rules:(NSInteger)rule{
    [self add:textField name:name rules:rule returnKeyType:UIReturnKeyNext];
}

-(void)add:(UIButton*)checkButton name:(NSString*)name{
    [arr addObject:checkButton];
    [_formRules addObject:@0];
    [_names addObject:name];
}
-(void)add:(UITextField*)textField name:(NSString*)name rules:(NSInteger)rules returnKeyType:(UIReturnKeyType)returnKeyType{
    [textField addTarget:self action:@selector(onDidEndEdit:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [textField addTarget:self action:@selector(onEditing:) forControlEvents:UIControlEventAllEditingEvents];
    textField.returnKeyType = returnKeyType;
    [arr addObject:textField];
    [_formRules addObject:[NSNumber numberWithInteger:rules]];
    [_names addObject:name];
    if(rules & RULES_PASSWORD){
        textField.secureTextEntry = YES;
    }
}
-(void)onEditing:(id)sender{
    _end = sender;
}
-(void)onDidEndEdit:(id)sender{
    NSUInteger index = [arr indexOfObject:sender];
    if(index < arr.count-1){
        id next = arr[index+1];
        [next becomeFirstResponder];
        _end =next;
    }else{
        //done
        if([sender isKindOfClass:[UITextField class]]){
            UITextField* text = sender;
            if(text.returnKeyType == UIReturnKeyDone){
                [self validate];
            }
        }
        _end = sender;
     }
    
}

-(void)addOkButton:(UIButton*)button{
    AddTarget(button,onOk);
}
ON_EVENT(onOk){
    if([self validate]){
        if(_end!=NULL){
            [_end resignFirstResponder];
            _end = NULL;
        }else{
            for(UITextField* text in arr){
                [text resignFirstResponder];
            }
        }
        
    }
}
-(NSMutableDictionary*)formData{
    return _formData;
}
-(void)resignFirstResponder{
    if(_end){
         [_end resignFirstResponder];
        _end = NULL;
    }
   
}
-(BOOL)validate{
    _formData= [[NSMutableDictionary alloc]init];
    for(NSInteger i=0,count = arr.count; i < count; ++i){
        NSInteger rule = [_formRules[i]integerValue];
        NSString* name = _names[i];
        if([arr[i] isKindOfClass:[UITextField class]]){
            UITextField* text = arr[i];
            NSString* value = text.text;
           
            if(rule & RULES_REQUIRED){
                if([value isEqualToString:@""]){
                    [_listener onFormError:text.placeholder];
                    return FALSE;
                }
            }
            if(rule & RULES_EMAIL){
                if(![ValidateUtil isEmail:value]){
                    [_listener onFormError:@"请输入正确的email"];
                    [text becomeFirstResponder];
                    [text performSelector:@selector(selectAll:) withObject: text];
                    return FALSE;
                }
            }
            if(rule & RULES_PHONE){
                if(![ValidateUtil isPhoneNumber:value]){
                    [_listener onFormError:@"请输入正确的手机号码"];
                    [text becomeFirstResponder];
                    [text performSelector:@selector(selectAll:) withObject: text];
                    return FALSE;
                }
            }
            if(rule & RULES_PASSWORD){
                _formData[name] = [CommonUtil md5:value];
            }else{
                _formData[name] = value;
            }
        }else if([arr[i]isKindOfClass:[UIButton class]]){
                UIButton* button = arr[i];
                _formData[name] = button.selected ? @1: @0;
            }
        
    }
    [_listener onFormSubmit:_formData];
     return TRUE;

}


@end
