//
//  ShareModel.m
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ShareModel.h"
#import <ecardlib/ecardlib.h>
#import "MyShareView.h"
#import "CommonMacro.h"
#import <ecardlib/ecardlib.h>

@interface ShareModel()
{
  
}

@property (nonatomic,weak) UIView* contentView;

@end

@implementation ShareModel

-(void)dealloc{
  
}

-(void)share:(NSString*)content view:(UIView*)view{
  
  MyShareView* shareView = [ViewUtil createViewFormNibName:@"MyShareView" owner:self];

  [shareView.btn0 setTarget:self withAction:@selector(onShare:)];
  [shareView.btn1 setTarget:self withAction:@selector(onShare:)];
  [shareView.btn2 setTarget:self withAction:@selector(onShare:)];
  [shareView.btn3 setTarget:self withAction:@selector(onShare:)];
  [shareView.btn4 setTarget:self withAction:@selector(onShare:)];
  [shareView.btn5 setTarget:self withAction:@selector(onShare:)];
  
  shareView.btn2.hidden = YES;
  shareView.btn3.hidden = YES;
  shareView.btn4.hidden = YES;
  shareView.btn5.hidden = YES;
  
  _contentView = view;
  
  Control_AddTarget(shareView.btnCancel, onDismiss);
  
  [DMPopup bottom:shareView listener:^(__kindof UIView * shareView, BOOL isOk) {
    
  }];
 /*
    self.contentView = view;
  
    
    [shareView.btnCancel addTarget:self action:@selector(onCancelShare:) forControlEvents:UIControlEventTouchUpInside];
    _shareView = [[PopupView alloc]initWithTarget:self selector:@selector(onDismiss:)];
    [_shareView show:shareView];*/
  
  
  
}

ON_EVENT(onDismiss){
    [DMPopup dismiss];
}


ON_VIEW_EVENT(onShare){
  [DMShareUtil share:@"" title:@"" parent:_contentView type: (DMShareType)sender.tag];
}

ON_EVENT(onCancelShare){
  
 
  
}

@end
