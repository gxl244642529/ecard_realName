//
//  EditorUtil.m
//  ecard
//
//  Created by randy ren on 15/3/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "EditorUtil.h"
#import <DMLib/DMLib.h>


#import <ecardlib/ecardlib.h>
#import "_CLImageEditorViewController.h"

static CGFloat _rate;
static NSInteger _minWidth;
static NSInteger _minHeight;

@implementation EditorUtil

+(void)editImage:(UIImage*)image parent:(UIViewController*)parent delegate:(id<CLImageEditorDelegate>)delegate title:(NSString*)title autoEdit:(BOOL)autoEdit{
    [SVProgressHUD showWithStatus:@"准备图片中..."];
    dispatch_async(GLOBAL_QUEUE, ^{
        UIImage* copyImage = [image deepCopy];
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
            _CLImageEditorViewController* c=  [[_CLImageEditorViewController alloc]initWithImage:copyImage title:title autoEdit:autoEdit];
            c.delegate = delegate;
            [parent.navigationController pushViewController:c animated:YES];
        });
    });
    
}

+(NSInteger)minWidth{
    return _minWidth;
}
+(NSInteger)minHeight{
    return _minHeight;
}

+(void)configEditor:(CGFloat)rate minWidth:(NSInteger)minWidth minHeight:(NSInteger)minHeight{
 
    
    _rate = rate;
    _minWidth = minWidth;
    _minHeight = minHeight;
    
}
+(CGFloat)rate{
    return _rate;
}
@end
