//
//  CLAdjustmentTool.m
//
//  Created by sho yakushiji on 2013/10/23.
//  Copyright (c) 2013年 CALACULU. All rights reserved.
//

#import "CLAdjustmentTool.h"

#import <DMLib/DMLib.h>
#import "UIView+Frame.h"
#import "AdjView.h"

#import <ecardlib/ecardlib.h>

@implementation CLAdjustmentTool
{
    UIImage *_originalImage;
    UIImage *_thumnailImage;
    
    UISlider *_saturationSlider;
    UISlider *_brightnessSlider;
    UISlider *_contrastSlider;
    
    AdjView* _adjView;
}

-(void)dealloc{
    _adjView = nil;
    _originalImage = nil;
    _thumnailImage = nil;
    _saturationSlider = nil;
    _brightnessSlider = nil;
    _contrastSlider = nil;
}

+ (NSString*)title
{
    return @"调整";
}

+ (BOOL)isAvailable
{
    return ([UIDevice iosVersion] >= 5.0);
}

- (void)setup
{
    _originalImage = self.editor.imageView.image;
    _thumnailImage = [_originalImage resize:self.editor.imageView.frame.size];
    
    CGFloat minZoomScale = self.editor.scrollView.minimumZoomScale;
    self.editor.scrollView.maximumZoomScale = 0.95*minZoomScale;
    self.editor.scrollView.minimumZoomScale = 0.95*minZoomScale;
    [self.editor.scrollView setZoomScale:self.editor.scrollView.minimumZoomScale animated:YES];
    
    [self setupSlider];
}

- (void)cleanup
{
    [_adjView removeFromSuperview];
    [self.editor resetZoomScaleWithAnimate:YES];
}

- (void)executeWithCompletionBlock:(void(^)(UIImage *image, NSError *error, NSDictionary *userInfo))completionBlock
{
    [SVProgressHUD show];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        UIImage *image =  [ImageToolUtil filteredImage:_originalImage saturation:_saturationSlider.value brightness:_brightnessSlider.value contrast:_contrastSlider.value];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
            completionBlock(image, nil, nil);
        });
    });
}

#pragma mark- 

- (UISlider*)sliderWithValue:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max action:(SEL)action
{
    UISlider *slider = [[UISlider alloc] initWithFrame:CGRectMake(10, 0, 240, 35)];
    
    UIView *container = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 260, slider.height)];
    container.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.3];
    container.layer.cornerRadius = slider.height/2;
    
    slider.continuous = YES;
    [slider addTarget:self action:action forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
    
    [container addSubview:slider];
    [self.editor.view addSubview:container];
    
    return slider;
}

- (void)setupSlider
{
    
    NSBundle* bundle = [NSBundle bundleWithPath: [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent: @"ecardlibbundle.bundle"]];
    _adjView = [DMViewUtil createViewFormNibName:@"AdjView" bundle:bundle owner:self];
    [self.editor.view addSubview:_adjView];
    
     CGRect frame = self.editor.view.frame;
      LAYOUT_ALIGN_BOTTOM(_adjView);
     _adjView.frame = CGRectMake(0, _adjView.frame.origin.y, self.editor.view.width, _adjView.height);
    
    
    _brightnessSlider = _adjView.brightness;
    _saturationSlider = _adjView.saturation;
    _contrastSlider = _adjView.contrast;
    
    
    [self sliderWithValue:_adjView.brightness value:0 minimumValue:-1 maximumValue:1 action:@selector(sliderDidChange:)];
    [self sliderWithValue:_adjView.saturation value:1 minimumValue:0 maximumValue:2 action:@selector(sliderDidChange:)];
    [self sliderWithValue:_adjView.contrast value:1 minimumValue:0.5 maximumValue:1.5 action:@selector(sliderDidChange:)];
    
    _adjView.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_adjView.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _adjView.transform = CGAffineTransformIdentity;
                     }];

}
- (void)sliderWithValue:( UISlider*) slider value:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max action:(SEL)action
{
    slider.continuous = YES;
    [slider addTarget:self action:action forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
}

- (void)sliderDidChange:(UISlider*)sender
{
    static BOOL inProgress = NO;
    
    if(inProgress){ return; }
    inProgress = YES;
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        UIImage *image = [ImageToolUtil filteredImage:_thumnailImage saturation:_saturationSlider.value brightness:_brightnessSlider.value contrast:_contrastSlider.value];
        [self.editor.imageView performSelectorOnMainThread:@selector(setImage:) withObject:image waitUntilDone:NO];
        inProgress = NO;
    });
}



@end
