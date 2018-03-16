//
//  UIScrollView+Adapter.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//


#import "UIScrollView+Adapter.h"
#import <objc/runtime.h>
char* UIScrollViewAdapter = "UIScrollViewAdapter";
@implementation UIScrollView(Adapter)


-(void)setAdapter:(DMDataAdapter *)adapter{
    objc_setAssociatedObject(self, UIScrollViewAdapter, adapter, OBJC_ASSOCIATION_RETAIN);
}

-(DMDataAdapter*)adapter{
    return objc_getAssociatedObject(self, UIScrollViewAdapter);
}
@end
