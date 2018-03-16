//
//  DMCellDataSetter.m
//  libs
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMCellDataSetter.h"
#import "DataSetterUtil.h"
#import "DMViewUtil.h"
#import "ReflectUtil.h"
#import "DataUtil.h"
#import "DMDataSetter.h"
#import "DMViewPropFinder.h"
#import "DMNotifier.h"
#import "DMNotifierInfo.h"
#import "DataSetterUtil.h"
#import <objc/runtime.h>
#import "UIView+ViewNamed.h"

@interface DMCellDataSetterWithItemEvent : DMCellDataSetter{
    
}
-(id)initWithCellName:(NSString *)cellName itemEventArray:(NSArray*)itemEventArray;
@end


@interface DMCellDataSetter()
{
    NSMutableArray<DMViewInfo*>* _infos;
    NSString* _cellName;
    __weak id<IDataAdapterListener> _listener;
}

@end

@implementation DMCellDataSetter


+(DMCellDataSetter*)createWithView:(UIView*)view cellName:(NSString*)cellName{
    UIViewController* controller = [view findViewController];
    return [DMCellDataSetter create:controller cellName:cellName];
}
+(DMCellDataSetter*)create:(id)controller cellName:(NSString*)cellName{
    NSMutableArray* itemEventArray = [[DMNotifier notifier]getItemEventArray:NSStringFromClass([controller class])];
    if(!itemEventArray){
        return [[DMCellDataSetter alloc]initWithCellName:cellName];
    }
    return [[DMCellDataSetterWithItemEvent alloc]initWithCellName:cellName itemEventArray:itemEventArray];
}

-(void)dealloc{
    _infos = nil;
    _cellName = nil;
}
-(void)setListener:(id<IDataAdapterListener>)listener{
    _listener = listener;
}
-(id)initWithCellName:(NSString*)cellName{
    if(self = [super init]){
        _infos = [DataSetterUtil cachedInfos:cellName];
        _cellName = cellName;
    }
    return self;
}




char* key = "CellDataSetter";

-(void)createInfos:(NSMutableArray*)infos view:(UIView*)view{
    
}

-(void)onInitializeView:(UIView *)parent cell:(UIView *)cell data:(NSObject *)data index:(NSInteger)index{
    //查找本视图对应的视图设置器
    NSMutableArray<id<DMDataSetter>>* setters =objc_getAssociatedObject(cell, key);
    if(!setters){
        if(!_infos){
            if([data isKindOfClass:[NSDictionary class]]){
                _infos =[DataSetterUtil findAllViewInfo:cell];
            }else{
                _infos =[DMViewPropFinder findAllViewInfo:cell class:[data class]];
            }
            [self createInfos:_infos view:cell];
            [DataSetterUtil appendCache:_cellName arr:_infos];
        }
        setters = [DataSetterUtil createDataSetters:cell infos:_infos];
        objc_setAssociatedObject(cell, key, setters, OBJC_ASSOCIATION_RETAIN);
    }
    for (id<DMDataSetter> setter in setters) {
        [setter setValue:data];
    }
    
    if(_listener){
        [_listener onInitializeView:parent cell:cell data:data index:index];
    }
    
}

@end


@interface ItemEventDataSetter : DMDataSetter

{
    UIButton* _view;
    __weak id _data;
    __weak DMNotifierInfo* _notifierInfo;
}

-(id)initWithName:(NSString *)name view:(UIButton*)view notifierInfo:(DMNotifierInfo*)info;

@end


@implementation ItemEventDataSetter

-(id)initWithName:(NSString *)name view:(UIButton*)view notifierInfo:(DMNotifierInfo*)info{
    if(self = [super initWithName:name]){
        Control_AddTarget(view, onClick);
        _notifierInfo = info;
    }
    return self;
}

-(void)setValue:(id)data{
    _data = data;
}

-(void)onClick:(id)sender{
    PerformSelector(_notifierInfo.observer, _notifierInfo.selector, _data);
}

@end

@interface DMCellDataSetterWithItemEvent()
{
    __weak NSArray* _itemEventArray;
}

@end

@interface DMItemViewInfo : DMViewInfo
{
    __weak DMNotifierInfo* _info;
}
-(void)setNotifierInfo:(DMNotifierInfo*)info;
@end

@implementation DMItemViewInfo

-(void)setNotifierInfo:(DMNotifierInfo*)info{
    _info = info;
}

-(id<DMDataSetter>)createSetter:(UIView*)parentView{
    return [[ItemEventDataSetter alloc]initWithName:self.name view:[parentView viewWithTag:self.tag] notifierInfo:_info];
}

@end


@implementation DMCellDataSetterWithItemEvent

-(void)dealloc{
    
}

-(id)initWithCellName:(NSString *)cellName itemEventArray:(NSArray*)itemEventArray{
    if(self = [super initWithCellName:cellName]){
        _itemEventArray = itemEventArray;
    }
    return self;
}
-(void)createInfos:(NSMutableArray*)infos view:(UIView*)view{
    
    for(DMNotifierInfo* info in _itemEventArray){
        NSString* name = [info.notificaion substringFromIndex:4];
        //查找视图
        UIView* itemView = [DMViewUtil findViewByName:name view:view];
        if([itemView isKindOfClass:[UIButton class]]){
            DMItemViewInfo* itemInfo = [[DMItemViewInfo alloc]init];
            itemInfo.name = name;
            itemInfo.tag = itemView.tag;
            [itemInfo setNotifierInfo:info];
            [infos addObject:itemInfo];
        }
    }
    
    
}
@end
