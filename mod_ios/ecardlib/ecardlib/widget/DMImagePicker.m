//
//  DMImagePicker.m
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMImagePicker.h"
#import <dmlib/dmlib.h>
#import "EditorUtil.h"
#import "CommonMacro.h"

@interface DMImagePicker()
{
    __weak DMFormImage* _image;
}

@end

@implementation DMImagePicker


-(void)awakeFromNib{
    [super awakeFromNib];
    
    for(__kindof UIView* view in self.subviews){
        if([view isKindOfClass:[DMFormImage class]]){
            
            _image = view;
            break;
        }
    }
    _currentController = [self findViewController];
    //这里需要
    
    [self setTarget:self withAction:@selector(onClick:)];
}

- (void)imageEditorDidFinishEdittingWithImage:(UIImage*)image{
    [self onSetImage:image];
}
-(void)imageEditorWillFinishEditingWidthImage:(UIImage*)image{
    
}


-(void)onSetImage:(UIImage*)image{
    [Alert toast:@"加载图片..."];
    dispatch_async(GLOBAL_QUEUE, ^{
        UIImage* result = [_image resizeImage:image];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [Alert cancelWait];
            [_image setVal:result];
        });
    });
}


-(void)onShowImage:(UIImage*)image{
    //
    if(_edit){
        [EditorUtil configEditor:1 minWidth:0 minHeight:0];
        [EditorUtil editImage:image parent:_currentController delegate:self title:_title autoEdit:_forceCrop];
    }else{
        [self onSetImage:image];
    }
    
}
-(void)onClick:(id)sender{
    //上传图片
    __weak DMImagePicker* __self = self;
    if(_album){
        [ExternalUtil showActionSheet:_currentController completion:^(UIImage * image) {
            [__self onShowImage:image];
        }];
        
    }else{
        [ExternalUtil captureImage:_currentController completion:^(UIImage * image) {
            [__self onShowImage:image];
        }];
    }

    
}


@end
