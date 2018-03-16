//
//  Form.m
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "Form.h"
#import <ecardlib/ecardlib.h>

#import "JsonTaskManager.h"

@interface Form()
{
    ObjectJsonTask* _task;
    __weak NSObject<IFormSubmit>* _listener;
}
@end

@implementation Form
-(void)dealloc{
    _task = NULL;
    _scrollView = NULL;
    _model = NULL;
    _contentView = NULL;
    self.submitMessage = NULL;
}


-(id)initWithParent:(UIView*)parent contentNibName:(NSString*)contentNibName frame:(CGRect)frame api:(NSString*)api  listener:(NSObject<IFormSubmit>*)listener{
    if(self=[super init]){
        _task = [[JsonTaskManager sharedInstance]createObjectJsonTask:api cachePolicy:DMCachePolicy_NoCache];
        [_task setListener:self];
        _model = [[FormModel alloc]initWithListener:self];
        _scrollView = [[FormScrollView alloc]initWithFrame:frame contentNibName:contentNibName];
        _contentView = [_scrollView contentView];
        _parentView = parent;
        [parent addSubview:_scrollView];
        _listener = listener;
        
        [listener initForm:_model contentView:_contentView];
    }
    return self;
}


-(void)task:(NSObject<IJsonTask> *)task result:(id)result{
    [Alert cancelWait:_parentView];
    //成功
    [_listener onSubmitComplete:[_model formData] result:result];
}

-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [Alert cancelWait:_parentView];
    [Alert alert:errorMessage];
}


-(void)onFormError:(NSString*)error{
    [Alert alert:error];
}
-(void)onFormSubmit:(NSDictionary*)data{
    if([_listener respondsToSelector:@selector(beforeSubmit:contentView:)]){
        if(![_listener beforeSubmit:[_model formData] contentView:_contentView]){
            return;
        }
    }
    [Alert wait:_parentView message:self.submitMessage==NULL?@"正在提交...":self.submitMessage];
    for(NSString* key in data){
        [_task put:key value:data[key]];
    }
    [_task execute];
}
@end
