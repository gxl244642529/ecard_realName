//
//  WebViewGroup.h
//  ecard
//
//  Created by randy ren on 14-9-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IPullToRefreshViewDelegate.h"
#import <ecardlib/ecardlib.h>
#import <UIKit/UIKit.h>
@interface WebViewGroup : NSObject<UIWebViewDelegate,IPullToRefreshViewDelegate,LoadingStateDelegate>
{
    
}

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent;
-(void)loadUrl:(NSString*)url;
@end
