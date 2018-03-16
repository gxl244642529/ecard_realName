//
//  Notifier.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMMacro.h"



//api成功
#define SUCCESS_NOTIFICATION n_s_
//api失败
#define ERROR_NOTIFICATION n_e_
//api消息
#define MESSAGE_NOTIFICATION n_m_

//这里为item
#define ITEM_EVENT_ID n_i_
#define EVENT_ID n_e_

#define NOTIFICATION n_n_




#define API_JOB_SUCCESS(Class,Method,Type) -(void)n_s_##Class##_##Method:(Type)result
#define API_JOB_ERROR(Class,Method) -(void)n_e_##Class##_##Method:(NSError*)error
#define API_JOB_MESSAGE(Class,Method) -(void)n_m_##Class##_##Method:(NSString*)message
#define ON_NOTIFICATION(Notification,Type) -(void)n_n_##Notification:(Type)data

#define ON_ITEM_EVENT(Item,Type) -(void)n_i_##Item:(Type)data

@interface DMNotifier : NSObject

+(DMNotifier*)notifier;

-(void)addObserver:(id)observer;

-(void)addObserver:(id)observer notification:(NSString*)notification selector:(SEL)selector;

-(void)removeObserver:(id)observer;

-(BOOL)notifyObservers:(NSString*)notification data:(id)data;

/**
 推荐使用这个版本
 */
-(BOOL)sendNotification:(NSString*)notification data:(id)data;

-(NSMutableArray*)getItemEventArray:(NSString*)className;

@end
