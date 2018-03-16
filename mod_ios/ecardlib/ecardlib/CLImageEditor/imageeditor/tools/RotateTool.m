//
//  RotateTool.m
//  ecard
//
//  Created by randy ren on 15-2-10.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "RotateTool.h"

#import <ecardlib/ecardlib.h>
#import "TabItemView.h"
#import "ColorUtil.h"
#import "CLImageToolBase.h"



#define BOTTOM_HEIGHT 58
@interface RotateTool()
{
    MyRotatePanel* _rotatePanel;
    UIView* _container;
    
    __weak UISlider* _rotateSlider;
    
    NSInteger _flipState1;
    NSInteger _flipState2;
    
    CATransform3D _initialTransform;
    CGRect _initialRect;

}

@end

@implementation RotateTool

ON_VIEW_EVENT(onRotate){
    switch (sender.tag) {
        case 0:
        {
            CGFloat value = (int)floorf((_rotateSlider.value + 1)*2) + 1;
            if(value>4){ value -= 4; }
            _rotateSlider.value = value / 2 - 1;
            _rotatePanel.hidden = YES;
            break;
        }
        case 1:
            _flipState1 = (_flipState1==0) ? 1 : 0;
            break;
        case 2:
            _flipState2 = (_flipState2==0) ? 1 : 0;
            break;
        default:
            break;
    }
    
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         [self rotateStateDidChange];
                     }
                     completion:^(BOOL finished) {
                         _rotatePanel.hidden = NO;
                     }
     ];
    
}
- (CATransform3D)rotateTransform:(CATransform3D)initialTransform clockwise:(BOOL)clockwise
{
    CGFloat arg = _rotateSlider.value*M_PI;
    if(!clockwise){
        arg *= -1;
    }
    
    CATransform3D transform = initialTransform;
    transform = CATransform3DRotate(transform, arg, 0, 0, 1);
    transform = CATransform3DRotate(transform, _flipState1*M_PI, 0, 1, 0);
    transform = CATransform3DRotate(transform, _flipState2*M_PI, 1, 0, 0);
    
    return transform;
}
- (void)rotateStateDidChange
{
    CATransform3D transform = [self rotateTransform:_initialTransform clockwise:YES];
    
    CGFloat arg = _rotateSlider.value*M_PI;
    CGFloat Wnew = fabs(_initialRect.size.width * cos(arg)) + fabs(_initialRect.size.height * sin(arg));
    CGFloat Hnew = fabs(_initialRect.size.width * sin(arg)) + fabs(_initialRect.size.height * cos(arg));
    
    CGFloat Rw = _rotatePanel.width / Wnew;
    CGFloat Rh = _rotatePanel.height / Hnew;
    CGFloat scale = MIN(Rw, Rh) * 0.95;
    
    transform = CATransform3DScale(transform, scale, scale, 1);
    _imageView.layer.transform = transform;
    _rotatePanel.gridRect = _imageView.frame;
}
- (UISlider*)sliderWithValue:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max
{
    UISlider *slider = [[UISlider alloc] initWithFrame:CGRectMake(10, 0, 260, 30)];
    /*
     UIView *container = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 280, slider.height)];
     container.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.3];
     container.layer.cornerRadius = slider.height/2;
     */
    slider.continuous = YES;
    [slider addTarget:self action:@selector(sliderDidChange:) forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
    
    //  [container addSubview:slider];
    [_contentView addSubview:slider];
    
    return slider;
}
-(void)sliderDidChange:(UISlider*)sender{
    [self rotateStateDidChange];
}

-(void)createImpl{
    CGRect frame = _contentView.frame;
    _container = [[UIView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, BOTTOM_HEIGHT)];
    LAYOUT_ALIGN_BOTTOM(_container);
    [_contentView addSubview:_container];
    //按钮
    [self createItem:@"旋转" icon:@"s_ic_rotate" index:0 selector:@selector(onRotate:)];
    [self createItem:@"水平翻转" icon:@"s_ic_flip_h" index:1 selector:@selector(onRotate:)];
    [self createItem:@"垂直翻转" icon:@"s_ic_flip_v" index:2 selector:@selector(onRotate:)];
    
    _rotateSlider = [self sliderWithValue:0 minimumValue:-1 maximumValue:1];
    
    _rotateSlider.center = CGPointMake(frame.size.width/2, _container.top-30);
    
    
    _initialTransform = _imageView.layer.transform;
    _initialRect = _imageView.frame;
    
    _flipState1 = 0;
    _flipState2 = 0;
    
    
    
    _rotatePanel = [[MyRotatePanel alloc] initWithSuperview:_parentView frame:_imageView.frame];
    _rotatePanel.backgroundColor = [UIColor clearColor];
    _rotatePanel.bgColor = [[UIColor whiteColor] colorWithAlphaComponent:0.8];
    _rotatePanel.gridColor = [[UIColor darkGrayColor] colorWithAlphaComponent:0.8];
    _rotatePanel.clipsToBounds = NO;
    _rotatePanel.userInteractionEnabled = NO;
    
    
    
    _container.transform = CGAffineTransformMakeTranslation(0, _contentView.height-_container.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _container.transform = CGAffineTransformIdentity;
                     }];
    
}


-(TabItemView*)createItem:(NSString*)text icon:(NSString*)icon index:(int)index selector:(SEL)selector{
    TabItemView* item = [[TabItemView alloc]initWithFrame:CGRectMake(index*BOTTOM_HEIGHT, 0, BOTTOM_HEIGHT, BOTTOM_HEIGHT)];
    [item setText:text];
    [item setIcon:icon highlighted:icon];
    [item setPadding:3];
    [item setTextColor:[UIColor blackColor] highlighted:[ColorUtil titleColor]];
    [_container addSubview:item];
    item.tag = index;
    [item setTarget:self withAction:selector];
    return item;
}

-(NSString*)title{
    return @"编辑图片";
}

-(void)dealloc{
    _container = NULL;
    _rotatePanel = NULL;
    _rotateSlider = NULL;
}

-(void)destroy{
    [_rotatePanel removeFromSuperview];
    _rotatePanel = NULL;
    [_rotateSlider removeFromSuperview];
    _rotateSlider = NULL;
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _container.transform = CGAffineTransformMakeTranslation(0, _contentView.height-_container.top);
                     }
                     completion:^(BOOL finished) {
                         [_container removeFromSuperview];
                         _container = NULL;
                     }];
    
}

- (UIImage*)buildImage:(UIImage*)image
{
    CIImage *ciImage = [[CIImage alloc] initWithImage:image];
    CIFilter *filter = [CIFilter filterWithName:@"CIAffineTransform" keysAndValues:kCIInputImageKey, ciImage, nil];
    
    //NSLog(@"%@", [filter attributes]);
    
    [filter setDefaults];
    CGAffineTransform transform = CATransform3DGetAffineTransform([self rotateTransform:CATransform3DIdentity clockwise:NO]);
    [filter setValue:[NSValue valueWithBytes:&transform objCType:@encode(CGAffineTransform)] forKey:@"inputTransform"];
    
    
    CIContext *context = [CIContext contextWithOptions:nil];
    CIImage *outputImage = [filter outputImage];
    CGImageRef cgImage = [context createCGImage:outputImage fromRect:[outputImage extent]];
    
    UIImage *result = [UIImage imageWithCGImage:cgImage];
    
    CGImageRelease(cgImage);
    
    return result;
}
- (void)executeWithCompletionBlock:(void (^)(UIImage*, NSError *, NSDictionary *))completionBlock
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSLog(@"%f %f",_image.size.width,_image.size.height);
        UIImage *image = [self buildImage:_image];
        NSLog(@"%f %f",image.size.width,image.size.height);
        dispatch_async(dispatch_get_main_queue(), ^{
            completionBlock(image, nil, nil);
        });
    });
}
@end
