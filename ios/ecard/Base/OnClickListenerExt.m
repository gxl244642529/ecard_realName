//
//  OnClickListener.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "OnClickListenerExt.h"
#import "BaseViewController.h"


//普通
@interface OnClickListenerClass : OnClickListener
-(id)initWithParentAndClass:(UIViewController*)controller clsRef:(Class)clsRef;
@end

//普通
@interface OnClickListenerNibName : OnClickListener
-(id)initWithParentAndNibName:(UIViewController*)controller nibName:(NSString*)nibName;
@end


//内容视图，简单
@interface OnItemClickListenerSimpleViewController : OnClickListener
-(id)initWithListener:(UIViewController<IViewControllerListener>*)controller;
@end


@interface OnClickListenerEx(){
   
}

@end

@implementation OnClickListenerEx


+(OnClickListener*)parent:(UIViewController*)controller clsRef:(Class)clsRef{
    return [[OnClickListenerClass alloc]initWithParentAndClass:controller clsRef:clsRef];
}
+(OnClickListener*)parent:(UIViewController*)controller nibName:(NSString*)nibName{
    return [[OnClickListenerNibName alloc]initWithParentAndNibName:controller nibName:nibName];
}
+(OnClickListener*)listener:(UIViewController<IViewControllerListener>*)listener{
    return [[OnItemClickListenerSimpleViewController alloc]initWithListener:listener];
}
@end


/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
@interface OnClickListenerClass(){
     Class _classRef;
}

@end


@implementation OnClickListenerClass
-(void)dealloc{
    _classRef = NULL;
}
-(id)initWithParentAndClass:(UIViewController*)controller clsRef:(Class)clsRef{
    if(self=[self init]){
        _parent  = controller;
        _classRef = clsRef;
    }
    return self;
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
     MyViewController* viewController = [ [_classRef alloc]init];
    [viewController setData:data];
    [_parent.navigationController pushViewController:viewController animated:YES];
}
@end
/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
@interface OnClickListenerNibName(){
    NSString* _nibName;
}

@end

@implementation OnClickListenerNibName
-(void)dealloc{
    _nibName = NULL;
}
-(id)initWithParentAndNibName:(UIViewController*)controller nibName:(NSString*)nibName{
    if(self=[self init]){
        _parent  = controller;
        _nibName = nibName;
    }
    return self;
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    Class cls = NSClassFromString(_nibName);
    BaseViewController* viewController = [[cls alloc]initWithNibName:_nibName bundle:NULL];
    [viewController setData:data];
    [_parent.navigationController pushViewController:viewController animated:YES];
}
@end

/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
//内容视图，简单
@interface OnItemClickListenerSimpleViewController(){
  
}

@end

@implementation OnItemClickListenerSimpleViewController
-(id)initWithListener:(UIViewController<IViewControllerListener>*)controller{
    if(self=[self init]){
        _parent  = controller;
    }
    return self;
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    SimpleViewController* viewController = [[SimpleViewController alloc]initWithListener:(NSObject<IViewControllerListener>*)_parent];
    viewController.data = data;
    [_parent.navigationController pushViewController:viewController animated:YES];
}
@end
/////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////
//内容视图，简单
@interface OnItemClickListenerOpenForResult()
{
    Class _clsRef;
    NSInteger requestCode;
}

@end

@implementation OnItemClickListenerOpenForResult
-(void)dealloc{
    _clsRef = NULL;
}
-(id)initWithParent:(BaseViewController*)parent clsRef:(Class)clsRef requestCode:(NSInteger)requestCode{
    if(self=[self init]){
        _parent  = parent;
        _clsRef = clsRef;
    }
    return self;
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    BaseViewController* viewController = [[_clsRef alloc]init];
    BaseViewController* p = (BaseViewController*)_parent;
    [p openControllerForResult:viewController requestCode:requestCode data:data modal:NO];
}
@end