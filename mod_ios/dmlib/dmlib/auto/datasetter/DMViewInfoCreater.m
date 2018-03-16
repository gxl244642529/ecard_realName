//
//  DMViewInfoCreater.m
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewInfoCreater.h"
#import "DMDataSetter.h"
#import "DMValue.h"
#import "DMMutilValue.h"
#import "DMMacro.h"
#import "DMServers.h"
#import "UIView+ViewNamed.h"
#import "DMFormElement.h"
#import "DMMacro.h"
#import "DataUtil.h"
#import "DMJobManager.h"
#import "DMServers.h"
#import "ReflectUtil.h"
#import <objc/runtime.h>
#import "DMTableView.h"

#import "DMValue.h"
#import "DMMutilValue.h"




#define BEGIN_DECLARE_DATA_SETTER(Name,ViewType) @interface Name##DataSetter : DMDataSetter \
{ \
__weak ViewType* _view; \
} \
-(id)initWithName:(NSString*)name view:(ViewType*)view; \
@end \
@implementation Name##DataSetter \
-(id)initWithName:(NSString *)name view:(ViewType *)view{ \
    if(self = [super initWithName:name]){ \
        _view = view; \
    } \
    return  self; \
} \
-(void)setValue:(id)data{ \
    BEGIN_TRY 

#define END_DECLARE_DATA_SETTER BEGIN_CATCH \
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data); \
    END_CATCH \
} \
@end






@interface BgInfo : DMViewInfo

@end


@interface LabelInfo : DMViewInfo

@end

@interface TextFieldInfo : DMViewInfo

@end



@interface HiddenInfo : DMViewInfo

@end
@interface LocalImageInfo : DMViewInfo

@end


@interface DiskImageInfo : DMViewInfo

@end

@interface ImageInfo : DMViewInfo

@end

@interface ButtonInfo : DMViewInfo

@end

@interface TextViewInfo : DMViewInfo

@end
@interface ValueInfo : DMViewInfo

@end


@interface MutilValueInfo : DMViewInfo

@end




////////////////////////////////////////////////////////////////
@interface HiddenDataSetter : DMDataSetter
{
    __weak UIView* _view;
}
-(id)initWithName:(NSString*)name view:(UIView*)view;
@end

////////////////////////////////////////////////////////////////
@interface DiskImageDataSetter : DMDataSetter
{
    __weak UIImageView* _view;
}
-(id)initWithName:(NSString*)name view:(UIImageView*)view;
@end

////////////////////////////////////////////////////////////////
@interface LocalImageDataSetter : DMDataSetter
{
    __weak UIImageView* _view;
}
-(id)initWithName:(NSString*)name view:(UIImageView*)view;
@end

////////////////////////////////////////////////////////////////
@interface BgDataSetter : DMDataSetter
{
    __weak UIView* _view;
}
-(id)initWithName:(NSString*)name view:(UIView*)view;
@end

//--------------------------------------------------------------------------------------------------------
@interface LabelDataSetter : DMDataSetter
{
    __weak UILabel* _view;
}
-(id)initWithName:(NSString*)name view:(UILabel*)view;
@end
//--------------------------------------------------------------------------------------------------------






@interface ImageDataSetter : DMDataSetter
{
    __weak UIImageView* _view;
}
-(id)initWithName:(NSString*)name view:(UIImageView*)view;
@end
//--------------------------------------------------------------------------------------------------------
@interface TextFieldDataSetter : DMDataSetter
{
    __weak UITextField* _view;
}
-(id)initWithName:(NSString*)name view:(UITextField*)view;
@end
//--------------------------------------------------------------------------------------------------------
@interface ButtonDataSetter : DMDataSetter
{
    __weak UIButton* _view;
}
-(id)initWithName:(NSString*)name view:(UIButton*)view;
@end
//--------------------------------------------------------------------------------------------------------
@interface TextViewDataSetter : DMDataSetter
{
    __weak UITextView* _view;
}
-(id)initWithName:(NSString*)name view:(UITextView*)view;
@end




//--------------------------------------------------------------------------------------------------------
@interface ValueDataSetter : DMDataSetter
{
    __weak id<DMValue> _view;
}
-(id)initWithName:(NSString*)name view:(id<DMValue>)view;
@end
//--------------------------------------------------------------------------------------------------------
@interface MutilValueDataSetter : NSObject<DMDataSetter>
{
    __weak id<DMMutilValue> _view;
}
-(id)initWithSelector:(SEL)selector args:(NSArray*)args view:(id<DMMutilValue>)view;
@end


/////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@implementation DMViewInfoCreater
-(DMViewInfo*)createViewInfo:(__kindof UIView*)view{
    DMViewInfo* info;
    //否则进行解析
    if([view conformsToProtocol:@protocol(DMValue)]){
        info = [[ValueInfo alloc]init];
    }else if([view conformsToProtocol:@protocol(DMMutilValue)]){
        info = [[MutilValueInfo alloc]init];
    }else if([view isKindOfClass:[UILabel class]]){
        info= [[LabelInfo alloc]init];
    }else if([view isKindOfClass:[UITextField class]]){
        info= [[TextFieldInfo alloc]init];
    }else if([view isKindOfClass:[UIImageView class]]){
        info= [[ImageInfo alloc]init];
    }else if([view isKindOfClass:[UIButton class]]){
        info= [[ButtonInfo alloc]init];
    }else if([view isKindOfClass:[UITextView class]]){
        info = [[TextViewInfo alloc]init];
    }
    info.name = view.viewName;
    info.tag = view.tag;
    
    return info;

}
@end


@implementation DMHiddenInfoInfoCreater

-(DMViewInfo*)createViewInfo:(__kindof UIView*)view{
    DMViewInfo* info = [[HiddenInfo alloc]init];
    info.name = view.viewName;
    info.tag = view.tag;
    return info;
}

@end



@implementation DMLocalImgInfoCreater

-(DMViewInfo*)createViewInfo:(__kindof UIView*)view{
    DMViewInfo* info = [[DiskImageInfo alloc]init];
    info.name = view.viewName;
    info.tag = view.tag;
    return info;
}

@end

@implementation DMImageInfoInfoCreater

-(DMViewInfo*)createViewInfo:(__kindof UIView*)view{
    DMViewInfo* info = [[LocalImageInfo alloc]init];
    info.name = view.viewName;
    info.tag = view.tag;
    return info;
}

@end

@implementation DMBgViewInfoCreater

-(DMViewInfo*)createViewInfo:(__kindof UIView*)view{
    DMViewInfo* info = [[BgInfo alloc]init];
    info.name = view.viewName;
    info.tag = view.tag;
    return info;
}

@end

/**
 datasetter定义了哪一种具体的视图如何设置值
 设置值得类型有:  (1)直接设置值 UITextField UILabel DMValue DMMutilValue , (2)设置可见性(UIView) (3) 设置背景颜色(UIView) (4)设置使用哪种图标(UIImageView)
 
 创建datasetter的要素有:实体类的方法名称和控件类型
 方法名称决定了如何设置值
 
 (1)直接设置:方法名/属性名只能以驼峰式命名
 (2)可见性:hd_  名称对应的视图为UIView(一定)
 (3)背景:bg_   名称对应的视图为UIView(一定)   bg_n_ bg_h_ bg_s_  分别为 通常\高亮\选择
 (4)图标:ic_   名称对应的视图为UIImageView(不一定,如果发现视图不是UIImageView则给出警告)
 */


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@implementation LabelInfo

-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[LabelDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}

@end

@implementation BgInfo

-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[BgDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
}

@end



@implementation TextFieldInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[LabelDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end



@implementation HiddenInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[HiddenDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end



@implementation DiskImageInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[DiskImageDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end

@implementation LocalImageInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[LocalImageDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end


@implementation ImageInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[ImageDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end

@implementation ButtonInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[ButtonDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end
@implementation TextViewInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[TextViewDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end

@implementation ValueInfo : DMViewInfo
-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[ValueDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag]];
    
}
@end


@interface MutilValueInfo()
{
    SEL _selector;
    NSArray* _args;
}

@end

@implementation MutilValueInfo : DMViewInfo

-(void)setName:(NSString *)name{
    //注意name的格式必须为selector的格式
    _args = [name componentsSeparatedByString:@","];
    NSMutableString* selectorName = [[NSMutableString alloc]init];
    for (NSString* key in _args) {
        [selectorName appendString:key];
        [selectorName appendString:@":"];
    }
    _selector = NSSelectorFromString(selectorName);
    [super setName:name];
}

-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return [[MutilValueDataSetter alloc]initWithSelector:_selector args:_args view:[parentView viewWithTag:self.tag]];
    
}


-(BOOL)shouldHandle:(NSDictionary*)properties{
    //这些都有
    for (NSString* key in _args) {
        if([properties objectForKey:key]==nil){
            return FALSE;
        }
    }
    return TRUE;
}

@end


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@implementation ImageDataSetter

-(id)initWithName:(NSString *)name view:(UIImageView *)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    NSString* url =[data valueForKey:_name];
    if(!url || url.length==0){
        return;
    }
    if([url characterAtIndex:0]=='/'){
        url = [DMServers formatUrl:0 url:url ];
    }
    [[DMJobManager sharedInstance]loadImage:url image:_view];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}


@end




//-----------------------

@implementation HiddenDataSetter

-(id)initWithName:(NSString *)name view:(UIView *)view{
    if(self = [super initWithName:[NSString stringWithFormat:@"hd_%@",name]]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.hidden = [[data valueForKey:_name]boolValue];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end
//-----------------------

@implementation DiskImageDataSetter

-(id)initWithName:(NSString *)name view:(UIImageView *)view{
    if(self = [super initWithName:[NSString stringWithFormat:@"lc_%@",name]]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.image = [UIImage imageWithContentsOfFile:[data valueForKey:_name]];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end



//-----------------------

@implementation LocalImageDataSetter

-(id)initWithName:(NSString *)name view:(UIImageView *)view{
    if(self = [super initWithName:[NSString stringWithFormat:@"img_%@",name]]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.image = [data valueForKey:_name];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end
//-----------------------

@implementation BgDataSetter

-(id)initWithName:(NSString *)name view:(UIView *)view{
    if(self = [super initWithName:[NSString stringWithFormat:@"bg_%@",name]]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.backgroundColor = [data valueForKey:_name];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end


//--------------------------------------------------------------------------------------------------------
@implementation LabelDataSetter

-(id)initWithName:(NSString *)name view:(UILabel *)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.text =[DataUtil getString:data key:_name];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end

//--------------------------------------------------------------------------------------------------------

@implementation TextFieldDataSetter

-(id)initWithName:(NSString *)name view:(UITextField *)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    _view.text =[DataUtil getString:data key:_name];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end


//--------------------------------------------------------------------------------------------------------

@implementation ButtonDataSetter

-(id)initWithName:(NSString *)name view:(UIButton *)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    [_view setTitle:[DataUtil getString:data key:_name] forState:UIControlStateNormal];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end
//--------------------------------------------------------------------------------------------------------

@implementation TextViewDataSetter

-(id)initWithName:(NSString *)name view:(UITextView *)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    [_view setText:[DataUtil getString:data key:_name]];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end



//--------------------------------------------------------------------------------------------------------

@implementation ValueDataSetter

-(id)initWithName:(NSString *)name view:(id<DMValue>)view{
    if(self = [super initWithName:name]){
        _view = view;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    [_view setVal:[data valueForKey:_name]];
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _name,data);
    END_CATCH
    
}

@end



//--------------------------------------------------------------------------------------------------------

@interface MutilValueDataSetter()
{
    __weak NSArray* _args;
    SEL _selector;
}

@end

@implementation MutilValueDataSetter



-(id)initWithSelector:(SEL)selector args:(NSArray*)args view:(id<DMMutilValue>)view{
    if(self = [super init]){
        _view = view;
        _selector = selector;
        _args = args;
    }
    return  self;
}

-(void)setValue:(id)data{
    BEGIN_TRY
    [_view setVal:data];
    /*
    switch (_args.count) {
        case 2:
        {
             void (*action)(id, SEL, id,id) = (void (*)(id, SEL, id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]]);
            
        }
            break;
        case 3:
        {
            void (*action)(id, SEL, id,id,id) = (void (*)(id, SEL, id,id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]],[data valueForKey:_args[2]] );
        }
            break;
        case 4:
        {
            void (*action)(id, SEL, id,id,id,id) = (void (*)(id, SEL, id,id,id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]],[data valueForKey:_args[2]],[data valueForKey:_args[3]] );
        }
            break;
        case 5:
        {
            void (*action)(id, SEL, id,id,id,id,id) = (void (*)(id, SEL, id,id,id,id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]],[data valueForKey:_args[2]],[data valueForKey:_args[3]] ,[data valueForKey:_args[4]] );
        }   break;
        case 6:
        {
            void (*action)(id, SEL, id,id,id,id,id,id) = (void (*)(id, SEL, id,id,id,id,id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]],[data valueForKey:_args[2]],[data valueForKey:_args[3]] ,[data valueForKey:_args[4]],[data valueForKey:_args[5]]  );
        }   break;
        case 7:
        {
            void (*action)(id, SEL, id,id,id,id,id,id,id) = (void (*)(id, SEL, id,id,id,id,id,id,id))objc_msgSend;
            action(_view,_selector,[data valueForKey:_args[0]],[data valueForKey:_args[1]],[data valueForKey:_args[2]],[data valueForKey:_args[3]] ,[data valueForKey:_args[4]],[data valueForKey:_args[5]] ,[data valueForKey:_args[6]] );
        }   break;
        default:
            break;
    }
    
    
    */
    BEGIN_CATCH
    NSLog(@"Error: %@, Name %@ is not exists in data: %@",exception.reason, _args,data);
    END_CATCH
    
}

@end

/**
 char * type = property_copyAttributeValue(property, "T");
 
 switch(type[0]) {
 case 'f' : //float
 {
 return [NSNumber numberWithDouble:[dbValue floatValue]];
 }
 break;
 case 'd' : //double
 {
 return [NSNumber numberWithDouble:[dbValue doubleValue]];
 }
 break;
 
 case 'c':   // char
 {
 return [NSNumber numberWithDouble:[dbValue charValue]];
 }
 break;
 case 's' : //short
 {
 return [NSNumber numberWithDouble:[dbValue shortValue]];
 }
 break;
 case 'i':   // int
 {
 return [NSNumber numberWithDouble:[dbValue longValue]];
 }
 break;
 case 'l':   // long
 {
 return [NSNumber numberWithDouble:[dbValue longValue]];
 }
 break;
 
 case '*':   // char *
 break;
 
 case '@' : //ObjC object
 //Handle different clases in here
 {
 NSString *cls = [NSString stringWithUTF8String:type];
 cls = [cls stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
 cls = [cls stringByReplacingOccurrencesOfString:@"@" withString:@""];
 cls = [cls stringByReplacingOccurrencesOfString:@"\"" withString:@""];
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSString class]]) {
 return [NSString  stringWithFormat:@"%@", dbValue];
 }
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSNumber class]]) {
 return [NSNumber numberWithDouble:[dbValue doubleValue]];
 }
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSDictionary class]]) {
 return [NSDictionary stringWithObject:dbValue];
 }
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSArray class]]) {
 return [NSDictionary stringWithObject:dbValue];
 }
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSDate class]]) {
 if ([dbValue isKindOfClass:[NSDate class]]) {
 return [NSString stringWithFormat:@"%@", [NSDate stringWithDate:dbValue]];
 } else {
 return @"";
 }
 
 }
 
 if ([NSClassFromString(cls) isSubclassOfClass:[NSValue class]]) {
 return [NSData dataWithData:dbValue];
 }
 }
 break;
 }
 
 */

