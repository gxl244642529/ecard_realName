//
//  FormButton.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMSubmitButton.h"
#import "Alert.h"
#import "UIView+ViewNamed.h"


#import "DMJobManager.h"

@interface DMSubmitButton ()
{
    DMApiJob* _task;
    //需要提交的表单
    NSMutableArray* _submitElements;
}

@end

@implementation DMSubmitButton

-(void)dealloc{
    if(_task){
        [_task cancel];
        _task = nil;
    }
    _submitElements = nil;
    _api = nil;
    _waitingMessage = nil;
    _submitItems = nil;
    
}
-(DMApiJob*)apiJob{
    return _task;
}

-(void)awakeFromNib{
    [super awakeFromNib];
  
    
    if(_api){
          Control_AddTarget(self, onSubmit);
          UIViewController* currentController = [self findViewController];
           
           if([currentController conformsToProtocol:@protocol(DMSubmitDelegate)]){
               self.delegate = (id<DMSubmitDelegate>)currentController;
           }
        [self createJob];
        if([currentController conformsToProtocol:@protocol(DMApiDelegate)]){
            _task.delegate = currentController;
        }
    }
    
    
}

-(DMApiJob*)createJob{
    _task = [[DMJobManager sharedInstance]createJsonTask:_api cachePolicy:DMCachePolicy_NoCache server:_server];
    _task.crypt = (CryptType)_crypt;
    _task.waitingMessage = _waitingMessage ? _waitingMessage : @"请稍等...";
    _task.button = self;
    
    return _task;

}
-(void)setForm:(NSObject<DMForm>*)form{
    //这里需要进行筛选
    _form = form;
    if(_submitItems){
        //这里需要筛选
        NSArray* arr = [_submitItems componentsSeparatedByString:@","];
        //如果有存在的话
        _submitElements = [[NSMutableArray alloc]init];
        for(NSString* key in arr){
            //如果一致,则尽心
            for(UIView<DMFormElement>* element in form.elements){
                if([element.viewName isEqualToString:key]){
                    [_submitElements addObject:element];
                    break;
                }
            }
        }
    }else{
        _submitElements = form.elements;
    }
}

-(void)onSubmit:(id)sender{
    [self submit];
    
}
-(NSString*)validate:(NSMutableDictionary*)dic{
    //找到规则,并
    for (UIView<DMFormElement>* item in _submitElements) {
        if(!item.hidden){
            NSString* error = [item validate:dic];
            if(error){
                return error;
            }
        }
       
    }
    return nil;
}



-(void)releaseKeyboard{
    for(NSInteger i = _form.elements.count-1; i >=0 ; --i){
        UIView* view = (UIView*)_form.elements[i];
        if([view isFirstResponder]){
            [view resignFirstResponder];
            break;
        }
        
    }
}


-(BOOL)doValidate:(NSMutableDictionary*)dic{
    /**
     验证数据
     */
    
    NSString* error;
    error = [self validate:dic];
    if(error){
        if(error.length > 0){
            //如果是空的字符串的话，由控件自己处理错误
            [Alert alert:error];
        }
         return FALSE;
    }
    if(_delegate && [_delegate respondsToSelector:@selector(submit:validate:)]){
        if(![_delegate submit:self validate:dic]){
            return FALSE;
        }
    }
    return TRUE;
}
-(void)doSubmit:(NSMutableDictionary*)dic{
    if(_delegate && [_delegate respondsToSelector:@selector(submit:completeData:)]){
        [_delegate submit:self completeData:dic];
    }
    if(_delegate && [_delegate respondsToSelector:@selector(submit:onSubmit:)]){
        if( [_delegate submit:self onSubmit:dic]){
            [self releaseKeyboard];
            return;
        }
        
    }
    
    [_task putAll:dic];
    [self releaseKeyboard];
    [_task execute];
}
-(void)submit{
   NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    if([self doValidate:dic]){
        if(_confirmMessage){
            __weak typeof(self) __self = self;
            [Alert confirm:_confirmMessage confirmListener:^(NSInteger buttonIndex) {
                [__self doSubmit:dic];
            }];
        }else{
            [self doSubmit:dic];
        }
        
    }
}





/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
