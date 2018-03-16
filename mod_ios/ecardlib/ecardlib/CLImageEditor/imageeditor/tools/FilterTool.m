//
//  FilterTool.m
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "FilterTool.h"
#import "ImageUtil.h"

#import <ecardlib/ecardlib.h>
#import "DiyFilterView.h"
#import "TabItemGroup.h"
#import "EditorUtil.h"
#import "CLImageToolBase.h"
#import <DMLib/DMLib.h>

#define BOTTOM_HEIGHT 55

@interface FilterTool()
{
    UIScrollView* _scrollView;
    __weak TouchableView<ISelectable>* _currentView;
    UIImage *_originalImage;
    UIImage *_thumnailImage;
}

@end

@implementation FilterTool
-(void)dealloc{
    _scrollView = NULL;
    _currentView  = NULL;
    
    _originalImage = nil;
    _thumnailImage = nil;
}
-(void)cleanup{
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _scrollView.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_scrollView.top);
                     }
                     completion:^(BOOL finished) {
                         [_scrollView removeFromSuperview];
                         _scrollView = NULL;
                     }];
}
+(NSString*)title{
    return @"滤镜";
}




-(void)setup{
    
    
    _originalImage = self.editor.imageView.image;
    _thumnailImage = [_originalImage resize:self.editor.imageView.frame.size];
    
    CGRect frame = self.editor.view.frame;
    _scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, BOTTOM_HEIGHT)];
    [self.editor.view addSubview:_scrollView];
    LAYOUT_ALIGN_BOTTOM(_scrollView);
    CGFloat W = 70;
    CGFloat x = 0;
    float h = 100 * [EditorUtil rate];
    UIImage* img = [ImageUtil scaleImage:self.editor.imageView.image toSize:CGSizeMake(100,h)];
    for(int i=0; i <= 13; ++i){
        DiyFilterView *view = [[DiyFilterView alloc] initWithFrame:CGRectMake(x, 0, W, BOTTOM_HEIGHT)];
        view.tag = i;
        [view setImage:[ImageUtil applyFilter:img index:i]];
        [view setTarget:self withAction:@selector(onClick:)];
        [view setLabel:g_filter_names[i]];
        x += W;
        [_scrollView addSubview:view];
        if(i==0){
            _currentView = view;
            [_currentView setSelected:YES];
        }
    }
    
    
 
    _scrollView.contentSize = CGSizeMake(x, 0);
     _scrollView.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_scrollView.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _scrollView.transform = CGAffineTransformIdentity;
                     }];
    
}
-(void)onClick:(TouchableView<ISelectable>*)sender{
    //应用滤镜
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0), ^{
        UIImage* image= [ImageUtil applyFilter:_thumnailImage index:sender.tag];
        dispatch_async(dispatch_get_main_queue(), ^{
            self.editor.imageView.image = image;
        });
    });
    
    if(_currentView){
        [_currentView setSelected:NO];
    }
    
    
    [sender setSelected:YES];
    _currentView = sender;
    
    sender.alpha = 0.2;
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         sender.alpha = 1;
                     }
     ];
    
}

-(void)executeWithCompletionBlock:(void (^)(UIImage *, NSError *, NSDictionary *))completionBlock{
    //应用滤镜
    [SVProgressHUD show];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0), ^{
        if(_currentView && _currentView.tag > 0){
            UIImage* image= [ImageUtil applyFilter:_originalImage index:_currentView.tag];
            dispatch_async(dispatch_get_main_queue(), ^{
                [SVProgressHUD dismiss];
                completionBlock(image,NULL,NULL);
            });

        }else{
            dispatch_async(dispatch_get_main_queue(), ^{
                 [SVProgressHUD dismiss];
                completionBlock(_originalImage,NULL,NULL);
            });
        }
    });

}

@end
