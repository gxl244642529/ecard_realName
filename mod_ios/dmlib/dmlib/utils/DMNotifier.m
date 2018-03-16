//
//  Notifier.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMNotifier.h"
#import <objc/runtime.h>

#import "DMMacro.h"

#import "DMNotifierInfo.h"

__weak DMNotifier* _notifier;


@interface DMNotifier()
{
    NSMutableDictionary<NSString*,NSMutableArray<DMNotifierInfo*>*>* _notificationMap;
    NSMutableDictionary<NSString*,NSMutableArray<DMNotifierInfo*>*>* _classMap;
    
    
     NSMutableDictionary<NSString*,NSMutableArray<DMNotifierInfo*>*>* _itemEventMap;
}

@end


@implementation DMNotifier
+(DMNotifier*)notifier{
    return _notifier;
}
-(void)dealloc{
    _notificationMap = nil;
    _classMap = nil;
}
-(id)init{
    if(self = [super init]){
        _notificationMap = [[NSMutableDictionary alloc]init];
        _classMap = [[NSMutableDictionary alloc]init];
        _itemEventMap =[[NSMutableDictionary alloc]init];
        _notifier = self;
    }
    return self;
}
-(void)removeObserver:(id)observer{
     NSMutableArray<DMNotifierInfo*>* arr = [_classMap objectForKey:NSStringFromClass([observer class])];
    if(arr){
        for(DMNotifierInfo* info in arr){
            NSMutableArray<DMNotifierInfo*>* list = [_notificationMap objectForKey:info.notificaion];
            if(list){
                [list removeObject:info];
                if(list.count==0){
                    [_notificationMap removeObjectForKey:info.notificaion];
                }
            }
        }
    }
}


-(void)addObserver:(id)observer notification:(NSString*)notification selector:(SEL)selector{
    DMNotifierInfo* info = [[DMNotifierInfo alloc]init];
    info.observer = observer;
    info.notificaion =notification;
    info.selector = selector;
    NSMutableArray* list = [_notificationMap objectForKey:notification];
    if(!list){
        list = [[NSMutableArray alloc]init];
        [_notificationMap setValue:list forKey:notification];
    }
    [list addObject:info];
}



-(void)addObserver:(id)observer{
    Class clazz = [observer class];
    NSString* className = NSStringFromClass(clazz);
    NSMutableArray<DMNotifierInfo*>* arr = [_classMap objectForKey:className];
    if(arr){
        for(DMNotifierInfo* info in arr){
            info.observer = observer;
            NSMutableArray* list = [_notificationMap objectForKey:info.notificaion];
            if(!list){
                list = [[NSMutableArray alloc]init];
                [_notificationMap setValue:list forKey:info.notificaion];
            }
            [list addObject:info];
        }
    }else{
        arr = [[NSMutableArray alloc]init];
        [_classMap setValue:arr forKey:className];
        
        
        unsigned int count = 0;
        Method* list = class_copyMethodList(clazz,&count);
        for(unsigned int i=0; i  < count; ++i){
            Method method = list[i];
            SEL name = method_getName(method);
            NSString *strName = [NSString  stringWithCString:sel_getName(name) encoding:NSUTF8StringEncoding];
            NSString* notication = strName;
            BOOL isItemEvent = NO;
            if([strName compare:@"n_s_" options:0 range:NSMakeRange(0, 4)]==0){
                //如果api_success
                
            }else if([strName compare:@"n_e_" options:0 range:NSMakeRange(0, 4)]==0){
                //如果api_error
                
            }else if([strName compare:@"n_m_" options:0 range:NSMakeRange(0, 4)]==0){
                //如果api_message
                
            }else  if([strName compare:@"n_n_" options:0 range:NSMakeRange(0, 4)]==0){
                //notification
               
            }else if([strName compare:@"n_i_" options:0 range:NSMakeRange(0, 4)]==0){
                //item event
                isItemEvent = TRUE;
                
            }else {
                 continue;
            }
            notication = [notication stringByReplacingOccurrencesOfString:@":" withString:@""];
            
            NSMutableArray* notificationList = [_notificationMap objectForKey:notication];
            if(!notificationList){
                notificationList = [[NSMutableArray alloc]init];
                [_notificationMap setValue:notificationList forKey:notication];
            }
            DMNotifierInfo* info=[[DMNotifierInfo alloc]init];
            info.observer = observer;
            info.selector = name;
            info.notificaion = notication;
            
            if(isItemEvent){
                
               NSMutableArray<DMNotifierInfo*>* itemEventList = [_itemEventMap objectForKey:className];
                if(!itemEventList){
                    itemEventList = [[NSMutableArray alloc]init];
                    _itemEventMap[className] = itemEventList;
                }
                
                [itemEventList addObject:info];
                
            }else{
                [notificationList addObject:info];
            }
            [arr addObject:info];
            
        }

    }
}

-(NSMutableArray*)getItemEventArray:(NSString*)className{
    return [_itemEventMap objectForKey:className];
}

-(BOOL)sendNotification:(NSString *)notification data:(id)data{
    
    return [self notifyObservers:[NSString stringWithFormat:@"n_n_%@",notification] data:data];
    
}

-(BOOL)notifyObservers:(NSString*)notification data:(id)data{
     NSMutableArray<DMNotifierInfo*>* arr = [_notificationMap objectForKey:notification];
    if(arr){
        for (DMNotifierInfo* info in arr) {
            PerformSelector(info.observer, info.selector, data);
        }
        return YES;
    }
    return NO;
}

@end
