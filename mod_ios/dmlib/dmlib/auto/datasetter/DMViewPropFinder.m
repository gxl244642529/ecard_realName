//
//  DMViewPropFinder.m
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewPropFinder.h"
#import "DMPropManager.h"
#import "UIView+ViewNamed.h"
#import "DMTableView.h"
#import "DMPropManager.h"

@implementation DMViewPropFinder

+(NSMutableArray<DMViewInfo*>*)findAllViewInfo:(UIView*)view class:(Class)clazz{
    DMPropManager* manager = [DMPropManager sharedInstance];
    return [self findAllViewInfo:view dic:[manager parseData:clazz]];
}

+(NSMutableArray<DMViewInfo*>*)findAllViewInfo:(UIView*)view dic:(NSDictionary<NSString*,NSArray<NSObject<DMViewInfoCreater>*>*>*)dic{
    NSMutableArray* result = [[NSMutableArray alloc]init];
    
    [self visitSubviews:view arr:result dic:dic];
    
    return result;
    
}

+(void)visitSubviews:(UIView*)view arr:(NSMutableArray*)arr dic:(NSDictionary<NSString*,NSArray<NSObject<DMViewInfoCreater>*>*>*)dic{
    for(UIView* subView in view.subviews){
        NSString* viewName = subView.viewName;
        if(viewName){
            NSArray<NSObject<DMViewInfoCreater>*>* creaters ;
            if( [viewName rangeOfString:@","].location != NSNotFound ){
                NSArray* arr = [viewName componentsSeparatedByString:@","];
                BOOL shoundHandle = YES;
                for (NSString* name in arr) {
                    if(![dic objectForKey:name]){
                        shoundHandle = NO;
                        break;
                    }
                }
                if(shoundHandle){
                    creaters = [dic objectForKey:arr[0]];
                }
            }else{
                creaters = [dic objectForKey:viewName];
            }
            if(creaters){
                for (NSObject<DMViewInfoCreater>* creater in creaters) {
                    DMViewInfo* info =  [creater createViewInfo:subView];
                    if(info==nil){
                        continue;
                    }
                    [arr addObject:info];
                }
            }
            if([subView isKindOfClass:[DMTableView class]]){
                DMTableView* tableView = (DMTableView*)subView;
                [self visitSubviews:tableView.realHeaderView arr:arr dic:dic];
            }
            continue;
        }
        
        if(subView.subviews.count>0){
            //下一层
            [self visitSubviews:subView arr:arr dic:dic];
        }
    }
}


@end
