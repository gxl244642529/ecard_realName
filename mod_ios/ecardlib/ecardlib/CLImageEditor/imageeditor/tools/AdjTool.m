//
//  AdjTool.m
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AdjTool.h"
#import "ViewUtil.h"
#import "AdjView.h"
#import "UIView+Frame.h"

#import <ecardlib/ecardlib.h>
#import <DMLib/DMLib.h>

#import "CLImageToolBase.h"

@interface AdjTool()
{
    UIImage *_originalImage;
    UIImage *_thumnailImage;
    AdjView* _adjView;
    
}
@end

@implementation AdjTool
-(NSString*)title{
    return @"调整颜色";
}
-(void)dealloc{
    _originalImage = NULL;
    _thumnailImage = NULL;
    _adjView = NULL;
}
- (void)sliderWithValue:( UISlider*) slider value:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max action:(SEL)action
{
    slider.continuous = YES;
    [slider addTarget:self action:action forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
}



-(void)destroy{
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _adjView.transform = CGAffineTransformMakeTranslation(0, _contentView.height-_adjView.top);
                     }
                     completion:^(BOOL finished) {
                         [_adjView removeFromSuperview];
                         _originalImage = NULL;
                         _thumnailImage = NULL;
                         _adjView = NULL;
                     }];
}

-(void)createImpl{
    
    _originalImage = _image;
    _thumnailImage = [_originalImage resize:_imageView.frame.size];
    CGRect frame = _contentView.frame;
    _adjView = [DMViewUtil createViewFormNibName:@"AdjView" bundle:CREATE_BUNDLE(ecardlibbundle.bundle) owner:self];
    [_contentView addSubview:_adjView];
    LAYOUT_ALIGN_BOTTOM(_adjView);

    [self sliderWithValue:_adjView.brightness value:0 minimumValue:-1 maximumValue:1 action:@selector(sliderDidChange:)];
    [self sliderWithValue:_adjView.saturation value:1 minimumValue:0 maximumValue:2 action:@selector(sliderDidChange:)];
    [self sliderWithValue:_adjView.contrast value:1 minimumValue:0.5 maximumValue:1.5 action:@selector(sliderDidChange:)];
    
     _adjView.transform = CGAffineTransformMakeTranslation(0, _contentView.height-_adjView.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _adjView.transform = CGAffineTransformIdentity;
                     }];
    
}


- (void)sliderDidChange:(UISlider*)sender
{
    static BOOL inProgress = NO;
    
    if(inProgress){ return; }
    inProgress = YES;
    __weak AdjTool* __self = self;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        UIImage *image = [__self filteredImage:_thumnailImage];
        [_imageView performSelectorOnMainThread:@selector(setImage:) withObject:image waitUntilDone:NO];
        inProgress = NO;
    });
}

- (UIImage*)filteredImage:(UIImage*)image
{
    CIImage *ciImage = [[CIImage alloc] initWithImage:image];
    CIFilter *filter = [CIFilter filterWithName:@"CIColorControls" keysAndValues:kCIInputImageKey, ciImage, nil];
    
    [filter setDefaults];
    [filter setValue:[NSNumber numberWithFloat:_adjView.saturation.value] forKey:@"inputSaturation"];
    
    filter = [CIFilter filterWithName:@"CIExposureAdjust" keysAndValues:kCIInputImageKey, [filter outputImage], nil];
    [filter setDefaults];
    CGFloat brightness = 2*_adjView.brightness.value;
    [filter setValue:[NSNumber numberWithFloat:brightness] forKey:@"inputEV"];
    
    filter = [CIFilter filterWithName:@"CIGammaAdjust" keysAndValues:kCIInputImageKey, [filter outputImage], nil];
    [filter setDefaults];
    CGFloat contrast   = _adjView.contrast.value*_adjView.contrast.value;
    [filter setValue:[NSNumber numberWithFloat:contrast] forKey:@"inputPower"];
    
    CIContext *context = [CIContext contextWithOptions:nil];
    CIImage *outputImage = [filter outputImage];
    CGImageRef cgImage = [context createCGImage:outputImage fromRect:[outputImage extent]];
    
    UIImage *result = [UIImage imageWithCGImage:cgImage];
    
    CGImageRelease(cgImage);
    
    return result;
}


- (void)executeWithCompletionBlock:(void(^)(UIImage *image, NSError *error, NSDictionary *userInfo))completionBlock
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD show];
    });
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        UIImage *image = [self filteredImage:_originalImage];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
            completionBlock(image, nil, nil);
        });
    });
}

@end
