//
//  CommonMacro.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#define OVERRIDE


#define T(String) @#String

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
 nsnotification center
 */


#define AddObserver(Observer,Name,Selector) [[NSNotificationCenter defaultCenter] addObserver:Observer selector:@selector(Selector) name:Name object:nil]
#define RemoveObserverByName(Observer,Name) [[NSNotificationCenter defaultCenter] removeObserver:Observer name:Name object:nil]
#define SendNotification(Name,Object) [[NSNotificationCenter defaultCenter] postNotificationName:Name object:Object];


/**
 keyboard listener
 */
#define Keyboard_AddObserver_hide(onHide)  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onHide:) name:UIKeyboardDidHideNotification object:nil]
#define Keyboard_RemoveObserver_hide [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidHideNotification object:nil]

#define Keyboard_AddObserver_show(onShow) [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onShow:) name:UIKeyboardDidShowNotification object:nil]
#define Keyboard_RemoveObserver_show [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidShowNotification object:nil]

#define Keyboard_AddObserver_change(onChange) [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onChange:) name:UIKeyboardWillChangeFrameNotification object:nil]

#define RemoveObserver(Observer) [[NSNotificationCenter defaultCenter] removeObserver:Observer]

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

#define Control_AddTarget(ButtonName,SelectorName) [ButtonName addTarget:self action:@selector(SelectorName:) forControlEvents:UIControlEventTouchUpInside];

#define Control_RemoveTarget(ButtonName,SelectorName) [ButtonName removeTarget:self action:@selector(SelectorName:) forControlEvents:UIControlEventTouchUpInside];

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
 color
 */
#define RGB(R,G,B) [UIColor colorWithRed:(float)0x##R/255 green:(float)0x##G/255 blue:(float)0x##B/255 alpha:1]

#define RGB_WHITE(WHITE) [UIColor colorWithWhite:(CGFloat)0x##WHITE/255 alpha:1]
/**
 scrollview
 */
#define ScrollView_AddContentView(ScrollView,ContentViewName,ContentViewClass) ContentViewName = [ViewUtil createViewFormNibName:@#ContentViewClass owner:self]; \
[ScrollView addSubview:ContentViewName]; \
ScrollView.contentSize = CGSizeMake(0, ContentViewName.frame.size.height);



#define POST_ASYNC()

#define VIEW_BOTTOM(view) (view.frame.origin.y + view.frame.size.height);


#define BLOCK_PROPERTY_VOID(NAME) @property (copy,readwrite,nonatomic) void(^NAME)();
#define BLOCK_PROPERTY(NAME,...) @property (copy,readwrite,nonatomic) void(^NAME)(__VA_ARGS__);
#define BLOCK_PARAM_VOID(NAME) (void (^)(void))NAME
#define BLOCK_PARAM(NAME,...) (void (^)(__VA_ARGS__))NAME
/**
 tap
 */
#define AddTapGestureRecognizer(View,Selector) [View addGestureRecognizer:[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(Selector)]];


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
