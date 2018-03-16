 //
//  DetailView.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMDetailView.h"
#import "DMJobManager.h"
#import "UIView+ViewNamed.h"
#import "DataSetterUtil.h"
#import "ReflectUtil.h"
#import "DataUtil.h"
#import "DMViewDecoder.h"
#import "DMViewUtil.h"

@interface DMDetailView()
{
    DMApiJob* _task;
    __weak DMViewController* _currentController;
}

@end

@implementation DMDetailView

-(void)dealloc{
    if(_task){
        [_task cancel];
        _task = nil;
    }
    
    self.api = nil;
    self.dataKey = nil;
    self.paramKey = nil;
}


-(void)awakeFromNib{
    [super awakeFromNib];
    //datakey
    #if TARGET_INTERFACE_BUILDER
#else
    _currentController = [self findViewController];
    id data = _currentController.data;
    
    if(data!=NULL && _setValueOnInit){
        dispatch_async(dispatch_get_main_queue(), ^{
#ifdef DEBUG
            if(!self.viewName){
                NSLog(@"View %@ has no name",self);
                return;
            }
#endif
            [DMViewUtil setViewData:self data:data key:self.viewName];
        });
    }
    
    if(_api){
        dispatch_async(dispatch_get_main_queue(), ^{
            
            DMCachePolicy cachePolicy = [DMViewDecoder cachePolicy:_cachePolicy];
            _task = [[DMJobManager sharedInstance] createJsonTask:_api cachePolicy:cachePolicy server:_server];
            if(_entityName){
                _task.clazz = NSClassFromString(_entityName);
            }
            
            if(_dataKey && _paramKey){
                id idData = [data valueForKey:_dataKey];
                [_task put:_paramKey value:idData];
                if(self.extraData){
                    [_task putAll:[[NSMutableDictionary alloc]initWithDictionary:self.extraData]];
                }
            }
            _task.crypt = (CryptType)_crypt;
            _task.delegate  = self;
            if(_autoExecute){
                [_task execute];
            }
            
        });
        
    }

    #endif
   
}

-(void)execute{
     [_task execute];
}

-(void)jobSuccess:(DMApiJob*)request{
    if(!request.data){
        return;
    }
    
    NSString* detailKey=[NSString stringWithFormat:@"%@%@",request.api,self.viewName];
    [DMViewUtil setViewData:self data:request.data key:detailKey];
    
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
