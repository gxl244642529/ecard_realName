//
//  ViewUtil.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewUtil.h"
#import <UIKit/UIKit.h>
#import "UIView+ViewNamed.h"
#import "DMViewInfo.h"
#import "DataSetterUtil.h"

@implementation DMViewUtil
+(id)createViewFormNibName:(NSString*)nibName bundle:(NSBundle*)bundle owner:(id)owner{
    if(!bundle){
        bundle =[NSBundle mainBundle] ;
    }
    NSArray *nib = [bundle loadNibNamed:nibName owner:owner options:nil];
    return [nib objectAtIndex:0];
}
+(id)createViewFormNibName:(NSString*)nibName  bundle:(NSBundle*)bundle{
    if(!bundle){
        bundle =[NSBundle mainBundle] ;
    }
    NSArray *nib = [bundle loadNibNamed:nibName owner:nil options:nil];
    return [nib objectAtIndex:0];
}

+(CGFloat)hideView:(UIView*)view{
    
    
    NSLayoutConstraint* constraint = [view findHeight];
    CGFloat c = constraint.constant;
    constraint.constant = 0;
    
    
    view.hidden = YES;
    
    return c;
}
+(void)showView:(UIView*)view height:(CGFloat)height{
    NSLayoutConstraint* constraint = [view findHeight];
    constraint.constant = height;
    
    view.hidden = NO;
}

+(NSMutableArray*)findAllViews:(UIView*)view class:(Class)clazz{
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    [DMViewUtil visitSubviews:view class:clazz arr:arr];
    return arr;
}


+(NSMutableArray*)findAllViews:(UIView*)view protocol:(Protocol*)protocol{
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    [DMViewUtil visitSubviews:view protocol:protocol arr:arr];
    return arr;
}
+(void)visitSubviews:(UIView*)view protocol:(Protocol*)protocol arr:(NSMutableArray*)arr{
    for(UIView* subView in view.subviews){
        if([subView conformsToProtocol:protocol]){
            [arr addObject:subView];
        }
        
        if(subView.subviews.count>0){
            //下一层
            [self visitSubviews:subView protocol:protocol arr:arr];
        }
    }
}
+(void)visitSubviews:(UIView*)view class:(Class)clazz arr:(NSMutableArray*)arr{
    for(UIView* subView in view.subviews){
        if([subView isKindOfClass:clazz]){
            [arr addObject:subView];
        }
        
        if(subView.subviews.count>0){
            //下一层
            [self visitSubviews:subView class:clazz arr:arr];
        }
    }
}

+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView size:(float)size bold:(BOOL)bold gray:(BOOL)gray{
    UILabel* view = [[UILabel alloc]initWithFrame:CGRectZero];
    view.text = label;
    if(bold){
        view.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:size];
    }else{
        view.font = [UIFont fontWithName:@"HelveticaNeue" size:size];
    }
    
    if(gray){
        view.textColor = [UIColor darkGrayColor];
        
    }
    view.numberOfLines = 0;
    view.backgroundColor = [UIColor clearColor];
    [view sizeToFit];
    [parentView addSubview:view];
    return view;
}
+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView{
    UILabel* view = [[UILabel alloc]initWithFrame:CGRectZero];
    view.text = label;
    view.font = [UIFont fontWithName:@"HelveticaNeue" size:12];
    view.numberOfLines = 0;
    view.backgroundColor = [UIColor clearColor];
    [view sizeToFit];
    [parentView addSubview:view];
    return view;
}
+(void)setViewData:(UIView*)view data:(id)data key:(NSString*)detailKey{
    NSArray<DMViewInfo*>* infos = [DataSetterUtil cachedInfos:detailKey];
    if(!infos){
        infos = [DataSetterUtil cacheInfosForDetailView:view key:detailKey data:data];
    }
    for (id<DMDataSetter> setter in [DataSetterUtil createDataSetters:view infos:infos]) {
        [setter setValue:data];
    }

}
+(void)setViewData:(UIView*)view data:(id)data{
    NSString* detailKey=[NSString stringWithFormat:@"%@%@",NSStringFromClass([data class]),view.viewName];
    [DMViewUtil setViewData:view data:data key:detailKey];
}

-(void)setupScrollView:(UIScrollView*)scrollView{
    scrollView.delaysContentTouches = NO; // iterate over all the UITableView's subviews
    for (id view in scrollView.subviews) { // looking for a UITableViewWrapperView
        if ([NSStringFromClass([view class]) isEqualToString:@"UITableViewWrapperView"]) { // this test is necessary for safety and because a "UITableViewWrapperView" is NOT a UIScrollView in iOS7
            if([view isKindOfClass:[UIScrollView class]]) { // turn OFF delaysContentTouches in the hidden subview
                UIScrollView *scroll = (UIScrollView *) view;
                scroll.delaysContentTouches = NO;
            }
            break;
        }
    }
}
+(CGFloat)measureViewHeight:(UIView*)view{
    [view layoutIfNeeded];
    [view updateConstraintsIfNeeded];
    return[view systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height ;
}
+(CGFloat)measureViewHeight:(UIView*)view width:(CGFloat)width{
    [view layoutIfNeeded];
    [view updateConstraintsIfNeeded];
    return[view systemLayoutSizeFittingSize:CGSizeMake(width, 0)].height ;
}



+(UIView*)findViewByName:(NSString *)name view:(UIView*)view{
    for(UIView* subView in view.subviews){
        NSString* viewName = subView.viewName;
        if(viewName && [viewName isEqualToString:name]){
            return subView;
        }
        
        if(subView.subviews.count>0){
            //下一层
            UIView* result = [DMViewUtil findViewByName:name view:subView];
            if(result){
                return result;
            }
        }
    }
    return nil;

}


@end
