//
//  FormView.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormView.h"
#import "DMViewUtil.h"
#import "DMFormElement.h"
#import "UIView+ViewNamed.h"
#import "DataSetterUtil.h"
#import "DMSubmit.h"
#import "DMPropManager.h"


@interface DMFormView()
{
    //NSMutableArray<ViewSetterInfo*>* _infos;
    //所有表单元素
    NSMutableArray<UIView<DMFormElement>*>* _elements;
    //所有提交按钮
    NSMutableArray<UIView<DMSubmit>*>* _submitButtons;
}
@end

@implementation DMFormView



-(void)dealloc{
    _elements = nil;
    _submitButtons = nil;
}

-(NSArray<id<DMFormElement>>*)elements{
    return _elements;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
+(void)visitSubviews:(UIView*)view items:(NSMutableArray*)items buttons:(NSMutableArray*)buttons{
    for(UIView* subView in view.subviews){
        if([subView conformsToProtocol:@protocol(DMFormElement)]){
            if(!subView.viewName){
                NSLog(@"View %@ has no name",subView);
                continue;
            }
            if(subView.tag==0){
                 NSLog(@"View %@ tag is 0",subView);
                continue;
            }
            [items addObject:(id<DMFormElement>)subView];
        }else if([subView conformsToProtocol:@protocol(DMSubmit)]){
          /*  if(!subView.viewName){
                NSLog(@"View %@ has no name",subView);
                 @throw [NSException exceptionWithName:@"tag" reason:@"no name" userInfo:nil];
            }*/
            if(subView.tag==0){
                NSLog(@"View %@ tag is 0",subView);
                continue;
            }
            [buttons addObject:(id<DMSubmit>)subView];
        }else{
            if([subView isKindOfClass:[UILabel class]] || [subView isKindOfClass:[UITextView class]]
               || [subView isKindOfClass:[UITextField class]]
               || [subView isKindOfClass:[UIImage class]]
               ){
                continue;
            }
            //item不存在嵌套
            if(subView.subviews.count>0){
                //下一层
                [self visitSubviews:subView items:items buttons:buttons];
            }
        }
        
        
    }
}

-(void)awakeFromNib{
    [super awakeFromNib];
    
#if TARGET_INTERFACE_BUILDER
#else
    NSString* viewName = self.viewName;
    if(!viewName){
        //有错误
        //@throw [[NSException alloc]initWithName:@"ViewNameNotSet" reason:@"Form view must set a unique view name!" userInfo:nil];
        
        NSLog(@"Form %@ has no name!",self);
        
        return ;
    }
    
    //元素
    
    NSString* elementKey = [NSString stringWithFormat:@"%@elements",viewName];
    NSString* buttonsKey = [NSString stringWithFormat:@"%@buttons",viewName];
    NSMutableArray* elementInfos = [DataSetterUtil cachedInfos:elementKey];
    NSMutableArray* buttonInfos = [DataSetterUtil cachedInfos:buttonsKey];
    if(!elementInfos){
        _elements = [[NSMutableArray alloc]init];
        _submitButtons = [[NSMutableArray alloc]init];
        //查找所有的formitem
        [DMFormView visitSubviews:self items:_elements buttons:_submitButtons];
        //将这些item的name转换成tag
        elementInfos = [DMFormView convert:_elements];
        buttonInfos = [DMFormView convert:_submitButtons];
        
        [DataSetterUtil addCacheInfos:elementInfos key:elementKey];
        [DataSetterUtil addCacheInfos:buttonInfos key:buttonsKey];
    }else{
        //找到所有元素
        _elements = [DMFormView findAllViews:elementInfos view:self];
        //找到所有按钮
        _submitButtons = [DMFormView findAllViews:buttonInfos view:self];
    }
    
    
    for (id<DMSubmit> submit in _submitButtons) {
        [submit setForm:self];
    }

#endif
    
   }

+(NSMutableArray*)findAllViews:(NSArray*)src view:(UIView*)view{
    NSMutableArray* arr = [[NSMutableArray alloc]initWithCapacity:src.count];
    for(NSNumber* number in src){
        [arr addObject:[view viewWithTag:[number integerValue]]];
    }
    return arr;
}


+(NSMutableArray*)convert:(NSArray*)src{
    NSMutableArray* arr = [[NSMutableArray alloc]init];
  /* DMViewInfoCreater* creater = [DMPropManager sharedInstance].defaultCreater;
    for (UIView<DMFormElement>* view in src) {
        DMViewInfo* info = [creater createViewInfo:view];
        [arr addObject:info];
    }
    return arr;*/
    for (UIView<DMFormElement>* view in src) {
        [arr addObject:[NSNumber numberWithInteger:view.tag]];
    }
    return arr;

}

@end
