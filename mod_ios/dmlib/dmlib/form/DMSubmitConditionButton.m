//
//  DMSubmitConditionButton.m
//  DMLib
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMSubmitConditionButton.h"
#import "DMJobManager.h"
#import "UIView+ViewNamed.h"
#import "Alert.h"

@interface DMSubmitConditionButton()
{
    DMApiJob* _job;
    NSArray* _submitArray;
    NSMutableDictionary* _data;
}

@end

@implementation DMSubmitConditionButton

-(void)dealloc{
    _job = nil;
}


-(void)setForm:(NSObject<DMForm> *)form{
    
    [super setForm:form];
    //这里需要筛选
    _submitArray = [_checkFields componentsSeparatedByString:@","];
    
    
}

-(void)jobSuccess:(DMApiJob*)request{
    
    [self doSubmit:_data];
    _data = nil;
}


-(void)doTask{//这里直接调用
    if(!_job){
        _job = [[DMJobManager sharedInstance]createJsonTask:_checkApi cachePolicy:DMCachePolicy_NoCache server:self.server];
        _job.delegate = self;
        _job.crypt = (CryptType)_checkCrypt;
        _job.waitingMessage = @"请稍等...";
        _job.button = self;
    }
    
    for (NSString* key in _submitArray) {
        [_job put:key value:_data[key]];
    }
    [_job execute];
    
}

-(BOOL)doValidate:(NSMutableDictionary *)dic{
    BOOL ret = [super doValidate:dic];
    if(ret){
        _data = dic;
        if(self.confirmMessage){
            __weak typeof(self) __self = self;
            [Alert confirm:self.confirmMessage confirmListener:^(NSInteger buttonIndex) {
                [__self doTask];
            }];
        }else{
            
            [self doTask];
            
            

        }
    }
   
   return FALSE;
}

@end
