//
//  CommonMacro.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>


#define SCREEN_WIDTH [UIScreen mainScreen].bounds.size.width
#define SCREEN_HEIGHT [UIScreen mainScreen].bounds.size.height

#define OVERRIDE

#define APP_PACKAGE [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleIdentifierKey]
#define APP_VERSION [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleVersionKey]




#define T(String) @#String


#define PerformSelector(Target,Selector,WithObject) _Pragma("clang diagnostic push") \
_Pragma("clang diagnostic ignored \"-Warc-performSelector-leaks\"") \
[Target performSelector:Selector withObject:WithObject]; \
_Pragma("clang diagnostic pop")




#define DECLARE_SINGLETON(ClassName) +(ClassName*)sharedInstance
#define IMPLEMENT_SINGLETON(ClassName) static ClassName* g_##ClassName; \
+(ClassName*)sharedInstance{ \
    if(!g_##ClassName){ \
        g_##ClassName = [[ClassName alloc]init]; \
    } \
    return g_##ClassName; \
}

#define DECLARE_BG_PROPERTY(name) @property (nonatomic,readonly) UIColor* bg_##name
#define DECLARE_IMG_PROPERTY(name) @property (nonatomic,readonly) UIImage* img_##name
#define DECLARE_HIDDEN_PROPERTY(name) @property (nonatomic,readonly) BOOL hd_##name
#define DECLARE_DISKIMG_PROPERTY(name) @property (nonatomic,copy) NSString* lc_##name

#define IMPLEMENT_BG_PROPERTY(name) -(UIColor*)bg_##name
#define IMPLEMENT_IMG_PROPERTY(name) -(UIImage*)img_##name
#define IMPLEMENT_HIDDEN_PROPERTY(name) -(BOOL)hd_##name
#define IMPLEMENT_DISKIMG_PROPERTY(name) -(NSString*)lc_##name


#define FROM_JSON_INTEGER(name) _##name = [json[@#name]integerValue]
#define FROM_JSON(name) _##name = json[@#name]
#define FROM_JSON_DOUBLE(name) _##name = [json[@#name]doubleValue]
/**
 color
 */
#define RGB(R,G,B) [UIColor colorWithRed:(float)0x##R/255 green:(float)0x##G/255 blue:(float)0x##B/255 alpha:1]
#define RGB_WHITE(WHITE) [UIColor colorWithWhite:(CGFloat)0x##WHITE/255 alpha:1]
/**
block
*/
#define BLOCK_PROPERTY_VOID(NAME) @property (copy,readwrite,nonatomic) void(^NAME)();
#define BLOCK_PROPERTY(NAME,...) @property (copy,readwrite,nonatomic) void(^NAME)(__VA_ARGS__);
#define BLOCK_PARAM_VOID(NAME) (void (^)(void))NAME
#define BLOCK_PARAM(NAME,...) (void (^)(__VA_ARGS__))NAME

/**
 初始化
*/

#define INIT_CONTROLLER(ControllerName) -(id)init{ \
if(self = [super initWithNibName:@#ControllerName bundle:nil]){ \
} \
return self; \
}


/**
 controller
 */


#define Push_ViewController_Data(ControllerName,DataName) ControllerName* controller = [[ControllerName alloc]init]; \
controller.data = DataName; \
[self.navigationController pushViewController:controller animated:YES];


#define Parent_Push_ViewControllerNibName(Parent,NibName) [Parent.navigationController pushViewController:[[NibName alloc]initWithNibName:@#NibName bundle:NULL] animated:YES];
#define Parent_Push_ViewController(Parent,CLASS) [Parent.navigationController pushViewController:[[CLASS alloc]init] animated:YES]
#define Push_ViewControllerNibName(NibName) Parent_Push_ViewControllerNibName(self,NibName)
#define Push_ViewController(CLASS) Parent_Push_ViewController(self,CLASS)



#define INIT_CLASS(X) if(self = [super init]){ \
X \
} \
return self



#define BEGIN_TRY @try {
#define BEGIN_CATCH  } \
@catch (NSException *exception) {

#define END_CATCH  }


#define PROPERTY_BOOL(Name) @property (nonatomic,assign) BOOL Name
#define PROPERTY_INTEGER(Name) @property (nonatomic,assign) NSInteger Name
#define PROPERTY_DOUBLE(Name) @property (nonatomic,assign) double Name
#define PROPERTY_STRING(Name) @property (nonatomic,retain) NSString* Name

#define WRAP_DOUBLE(X) [NSNumber numberWithDouble:X]
#define WRAP_INTEGER(X) [NSNumber numberWithInteger:X]
#define WRAP_BOOL(X) [NSNumber numberWithBool:X]

#define GLOBAL_QUEUE dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)


/**
 tap
 */
#define AddTapGestureRecognizer(View,Selector) [View addGestureRecognizer:[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(Selector)]];



#define Control_AddTarget(ButtonName,SelectorName) [ButtonName addTarget:self action:@selector(SelectorName:) forControlEvents:UIControlEventTouchUpInside];

#define Control_RemoveTarget(ButtonName,SelectorName) [ButtonName removeTarget:self action:@selector(SelectorName:) forControlEvents:UIControlEventTouchUpInside];




#define CREATE_BUNDLE(NAME) [NSBundle bundleWithPath: [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent: @#NAME]]

#define CREATE_BUNDLE_WHEN_NOT_NULL(NAME) NAME ?  [NSBundle bundleWithPath: [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent: NAME]] :nil


#define INIT_BUNDLE_CONTROLLER(ControllerName,BundleName) -(id)init{ \
if(self = [super initWithNibName:@#ControllerName bundle:CREATE_BUNDLE(BundleName)]){ \
} \
return self; \
}









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
