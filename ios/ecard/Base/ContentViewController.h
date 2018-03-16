//
//  ContentViewController.h
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "BaseViewController.h"
#import "IContentViewListener.h"
/**
 *  最基本的内容页面，无网络任务
 */
@interface ContentViewController : BaseViewController
{
    UIScrollView* _scrollView;
}

-(id)initWithContentNibName:(NSString*)nibName title:(NSString*)title frame:(CGRect)frame;
-(id)initWithContentNibName:(NSString*)nibName title:(NSString*)title frame:(CGRect)frame listener:(NSObject<IContentViewListener>*)listener;
-(void)initContentView:(UIView*)contentView;
-(void)setListener:(NSObject<IContentViewListener>*)listener;


@end
