//
//  ContentViewController.m
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ContentViewController.h"
#import "CommonMacro.h"

#import <ecardlib/ecardlib.h>

@interface ContentViewController ()
{
    NSString* _nibName;
    NSString* _strTitle;
    CGRect _frame;
    
    __weak NSObject<IContentViewListener>* _listener;
    
}
@end

@implementation ContentViewController

-(void)dealloc{
    _nibName = NULL;
    _strTitle = NULL;
    _scrollView = NULL;
}

-(void)setListener:(NSObject<IContentViewListener>*)listener{
    _listener = listener;
}

-(id)initWithContentNibName:(NSString*)nibName title:(NSString*)title  frame:(CGRect)frame{
    if(self = [super init]){
        _nibName = nibName;
        _strTitle = title;
        _frame = frame;
    }
    return self;
}
-(id)initWithContentNibName:(NSString*)nibName title:(NSString*)title frame:(CGRect)frame listener:(NSObject<IContentViewListener>*)listener{
    if(self = [super init]){
        _nibName = nibName;
        _strTitle = title;
        _frame = frame;
        _listener = listener;
    }
    return self;

}
-(void)initContentView:(UIView*)contentView{
    [_listener initContentView:contentView scrollView:_scrollView data:self.data];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:_strTitle];
    _scrollView  = [[UIScrollView alloc]initWithFrame:_frame];
    [self.view addSubview:_scrollView];
    UIView* contentView = [ViewUtil createViewFormNibName:_nibName owner:self];
    [_scrollView addSubview:contentView];
    _scrollView.contentSize=CGSizeMake(0, CGRectGetHeight(contentView.frame));
    [self initContentView:contentView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
