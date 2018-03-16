//
//  DMScrollTabContainer.m
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMScrollTabContainer.h"

#import "DMViewUtil.h"
#import "UIView+ViewNamed.h"
#import "DMMacro.h"

@interface DMScrollTabContainer()
{
    NSArray* _names;
    NSMutableArray<UIView*>* _views;
    __weak UIViewController* _currentController;
}

@end
@implementation DMScrollTabContainer

-(void)dealloc{
    _names = nil;
    _views = nil;
}

-(void)awakeFromNib{
    self.pagingEnabled = YES;
    self.delegate = self;
   
    _currentController = [self findViewController];
    _names = [_viewNames componentsSeparatedByString:@","];
    self.contentSize = CGSizeMake(SCREEN_WIDTH*_names.count, 0);
    _views = [[NSMutableArray alloc]initWithCapacity:_names.count];
    for(NSInteger i =0 ; i < _names.count; ++i){
        UIView*  view = [self createView:i];
        [_views addObject:view];
    }
}
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
   NSInteger index= scrollView.contentOffset.x /SCREEN_WIDTH;
    [_tabDelegate tab:self didSelectIndex:index];
}

-(UIView*)createView:(NSInteger)index{
    Class clazz = NSClassFromString(_names[index]);
    UIViewController* controller = [[clazz alloc]init];
    [_currentController addChildViewController:controller];
    controller.view.frame = CGRectMake(index*SCREEN_WIDTH, 0, SCREEN_WIDTH, self.frame.size.height);
    [self addSubview:controller.view];
    return controller.view;
    
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger index = 0;
    for (UIView* view in self.subviews) {
        view.frame = CGRectMake(index*SCREEN_WIDTH, 0, SCREEN_WIDTH, self.frame.size.height);
        ++index;
    }
}

-(void)tab:(id)tab didSelectIndex:(NSInteger)index{

  [self setContentOffset:CGPointMake(index*SCREEN_WIDTH, 0) animated:YES];
}

@end
