//
//  CommonMacro.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <dmlib/dmlib.h>
#define OVERRIDE



/**
 单例模式
 */
#define DECLARE_SHARED_INSTANCE(PROTOCAL) +(NSObject<PROTOCAL>*)sharedInstance; \
+(void)destroyInstance;

#define IMPLEMENT_SHARED_INSTANCE(PROTOCAL,CLASS) static CLASS* instance; \
+(NSObject<PROTOCAL>*)sharedInstance{ \
if(!instance){ \
instance = [[CLASS alloc]init]; \
} \
return instance; \
} \
+(void)destroyInstance{ \
[instance destroy]; \
instance = NULL; \
}




#define DECLARE_SHARED_INSTANCE_DIRECT(Class) +(Class*)sharedInstance;


#define IMPLEMENT_SHARED_INSTANCE_DIRECT(Class) static Class* instance; \
+(Class*)sharedInstance{ \
if(!instance){ \
instance = [[Class alloc]init]; \
} \
return instance; \
} \
+(void)destroyInstance{ \
instance = nil; \
}

/**
 Controller
 */



#define AUTO_HIDE_NAVBAR \
-(void)viewWillAppear:(BOOL)animated{ \
[super viewWillAppear:animated]; \
[self.navigationController setNavigationBarHidden:YES animated:YES]; \
} \
-(void)viewWillDisappear:(BOOL)animated{ \
[super viewWillDisappear:animated]; \
[self.navigationController setNavigationBarHidden:NO animated:YES]; \
} \



#define Parent_Push_ViewControllerNibName(Parent,NibName) [Parent.navigationController pushViewController:[[NibName alloc]initWithNibName:@#NibName bundle:NULL] animated:YES];
#define Parent_Push_ViewController(Parent,CLASS) [Parent.navigationController pushViewController:[[CLASS alloc]init] animated:YES]


#define Push_ViewControllerNibName(NibName) Parent_Push_ViewControllerNibName(self,NibName)
#define Push_ViewController(CLASS) Parent_Push_ViewController(self,CLASS)

/**
 init
 */
#define INIT(X)  if(self = [super init]){ \
X \
} \
return self



/**
 格式抓花
 */

//数字转成字符串
#define ITOA(X) [NSString stringWithFormat:@"%d",(int)X]

#define INT(X) [NSNumber numberWithInt:X]
#define INTEGER(X) [NSNumber numberWithInteger:X]

/**
 event
 */
#define ON_EVENT(eventName) -(void)eventName:(id)sender

#define ON_VIEW_EVENT(eventName) -(void)eventName:(UIView*)sender

#define ON_EVENT_TYPE(eventName,type) -(void)eventName:(type)sender


#define Button_SetTitle(ButtonName,Title) [ButtonName setTitle:Title forState:UIControlStateNormal];



/**
 alloc
 */
#define ALLOC_INIT(CLASS,NAME) CLASS* NAME=[[CLASS alloc]init]


/**
 
 */
#define AddTarget(ButtonName,SelectorName) [ButtonName addTarget:self action:@selector(SelectorName:) forControlEvents:UIControlEventTouchUpInside];
/**
 setdata
 */
#define ValueForKey(DataName,KeyName) DataName[@#KeyName]
#define Data_ValueForKey(KeyName) ValueForKey(data,KeyName)

#define ValueForKey_IntegerValue(DataName,KeyName) [ValueForKey(DataName,KeyName)integerValue]
#define ValueForKey_IntValue(DataName,KeyName) [ValueForKey(DataName,KeyName)intValue]
#define ValueForKey_DoubleValue(DataName,KeyName) [ValueForKey(DataName,KeyName)doubleValue]
//cell 设置相关
#define  ValueForKey_CheckNull(DataName,KeyName,DEFAULT_VALUE) (([NSNull null]==ValueForKey(DataName,KeyName))?DEFAULT_VALUE:ValueForKey(DataName,KeyName))


//cell 设置相关
#define  Data_ValueForKey_CheckNull(KeyName,DEFAULT_VALUE) (([NSNull null]==Data_ValueForKey(KeyName))?DEFAULT_VALUE:Data_ValueForKey(KeyName))

#define SetText(Label,JsonKeyName) Label.text=Data_ValueForKey(JsonKeyName)
#define SetCellText(FieldName,JsonKeyName) SetText(cell.FieldName,JsonKeyName)

#define CheckSetText(Label,JsonKeyName) Label.text=Data_ValueForKey_CheckNull(JsonKeyName,@"")

#define CheckSetCellText(FieldName,JsonKeyName) CheckSetText(cell.FieldName,JsonKeyName)

/**
 nsstring
 */
#define FormatString(FORMAT,PARAM1) [NSString stringWithFormat:FORMAT,PARAM1];

/**
 布局
 */
#define LAYOUT_ALIGN_BOTTOM(view) view.center = CGPointMake(frame.size.width/2,frame.size.height - view.frame.size.height/2);


#define CGRectBottom(rect) rect.origin.y + rect.size.height

#define CGRectAlignBottom(rect,H) CGRectMake(0,rect.origin.y+rect.size.height-H,rect.size.width,H)

#define CGRectCenter(rect) CGPointMake(rect.origin.x + rect.size.width/2,rect.origin.y + rect.size.height/2)


/**
 scrollview
 */
#define ScrollView_AddContentView(ScrollView,ContentViewName,ContentViewClass) ContentViewName = [ViewUtil createViewFormNibName:@#ContentViewClass owner:self]; \
[ScrollView addSubview:ContentViewName]; \
ScrollView.contentSize = CGSizeMake(0, ContentViewName.frame.size.height);


#define GLOBAL_QUEUE dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)


#define POST_ASYNC()

#define VIEW_BOTTOM(view) (view.frame.origin.y + view.frame.size.height);



#define INIT_CONTROLLER(ControllerName) -(id)init{ \
if(self = [super initWithNibName:@#ControllerName bundle:nil]){ \
} \
return self; \
}


#define DELC_NOTE(NotificationName) +(NSString*)NotificationName; -(NSString*)NotificationName;

#define IMPL_NOTE(NotificationName) +(NSString*)NotificationName{\
return [NSString stringWithFormat:@"%@.%@",[self description],@#NotificationName];\
} \
-(NSString*)NotificationName{\
    return [NSString stringWithFormat:@"%@.%@",[self description],@#NotificationName];\
}

#define TRIM(X) [X stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]]


#define ON_SWITCH_VIEW(Name) -(void)Name;
