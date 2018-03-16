//
//  CLRotateTool.m
//
//  Created by sho yakushiji on 2013/11/08.
//  Copyright (c) 2013年 CALACULU. All rights reserved.
//

#import "CLRotateTool.h"
#import "CropTool.h"
#import "UIView+Frame.h"
#import "EditorUtil.h"
#import <DMLib/DMLib.h>

#import <ecardlib/ecardlib.h>
#import "RoateToolView.h"
#import "ViewUtil.h"


#define BOTTOM_HEIGHT 58

@interface CLRotatePanel : UIView
@property(nonatomic, strong) UIColor *bgColor;
@property(nonatomic, strong) UIColor *gridColor;
@property(nonatomic, assign) CGRect gridRect;
- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame;


@end



@interface CLRotateTool()
{
   
}
BLOCK_PROPERTY(complte,UIImage *, NSError *, NSDictionary *);
@end

@implementation CLRotateTool
{
    NSInteger _flipState1;
    NSInteger _flipState2;
    CATransform3D _initialTransform;
    CGRect _initialRect;
    
    
    CLRotatePanel *_rotatePanel;
    UISlider *_rotateSlider;
    RoateToolView *_menuScroll;
    MyClippingPanel* _gridView;
}

-(void)dealloc{
    self.complte = nil;
    _gridView = NULL;
    _menuScroll = NULL;
    _rotatePanel = NULL;
    _rotateSlider = NULL;
}
+ (NSString*)title
{
    return @"编辑";
}

+ (BOOL)isAvailable
{
    return ([UIDevice iosVersion] >= 5.0);
}


- (void)setup
{
    CGFloat minZoomScale = self.editor.scrollView.minimumZoomScale;
    self.editor.scrollView.maximumZoomScale = 0.95*minZoomScale;
    self.editor.scrollView.minimumZoomScale = 0.95*minZoomScale;
    [self.editor.scrollView setZoomScale:self.editor.scrollView.minimumZoomScale animated:YES];
    
    _initialTransform = self.editor.imageView.layer.transform;
    _initialRect = self.editor.imageView.frame;
    
    _flipState1 = 0;
    _flipState2 = 0;
    
    _rotatePanel = [[CLRotatePanel alloc] initWithSuperview:self.editor.scrollView frame:self.editor.imageView.frame];
    _rotatePanel.backgroundColor = [UIColor clearColor];
    _rotatePanel.bgColor = [self.editor.view.backgroundColor colorWithAlphaComponent:0.8];
    _rotatePanel.gridColor = [[UIColor darkGrayColor] colorWithAlphaComponent:0.8];
    _rotatePanel.clipsToBounds = NO;
    
   
  
    _menuScroll =  [DMViewUtil createViewFormNibName:@"RoateToolView" bundle:CREATE_BUNDLE_WHEN_NOT_NULL(@"ecardlibbundle.bundle") owner:self];
    [self.editor.view addSubview:_menuScroll];
    CGRect frame = self.editor.view.frame;
    LAYOUT_ALIGN_BOTTOM(_menuScroll);
    _menuScroll.frame = CGRectMake(0, _menuScroll.frame.origin.y, self.editor.view.width, _menuScroll.height);
    
    _menuScroll.btn1.tag = 0;
    _menuScroll.btn2.tag = 1;
    _menuScroll.btn3.tag = 2;
    
    [_menuScroll.btn1 setTarget:self withAction:@selector(tappedMenu:)];
    [_menuScroll.btn2 setTarget:self withAction:@selector(tappedMenu:)];
    [_menuScroll.btn3 setTarget:self withAction:@selector(tappedMenu:)];
    
    _rotateSlider = _menuScroll.slider;
    [self sliderWithValue:0 minimumValue:-1 maximumValue:1];
    
    
    _gridView = [[MyClippingPanel alloc] initWithSuperview:self.editor.scrollView frame:self.editor.imageView.frame];
    _gridView.rate = self.editor.imageView.frame.size.width / self.editor.imageView.image.size.width;
    _gridView.backgroundColor = [UIColor clearColor];
    _gridView.bgColor = [[UIColor whiteColor] colorWithAlphaComponent:0.8];
    _gridView.gridColor = [[UIColor darkGrayColor] colorWithAlphaComponent:0.8];
    _gridView.clipsToBounds = NO;
    _gridView.clippingRatio = [[MyRatio alloc]initWithValue:[EditorUtil rate]];
    _gridView.clippingRatio.isLandscape = YES;

    
    _menuScroll.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_menuScroll.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _menuScroll.transform = CGAffineTransformIdentity;
                     }];
}

- (void)cleanup
{
    [_rotatePanel removeFromSuperview];
    [_gridView removeFromSuperview];

    
    self.editor.imageView.layer.transform = _initialTransform;
    [self.editor resetZoomScaleWithAnimate:YES];
    
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _menuScroll.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_menuScroll.top);
                     }
                     completion:^(BOOL finished) {
                         [_menuScroll removeFromSuperview];
                     }];
}

-(void)executeWithCompletionBlockImpl:(void(^)(UIImage *image, NSError *error, NSDictionary *userInfo))completionBlock{
    [SVProgressHUD show];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSLog(@"%f %f",self.editor.imageView.image.size.width,self.editor.imageView.image.size.height);
        UIImage *image = [self buildImage:self.editor.imageView.image];
        image = [self cropImage:image scale: _gridView.frame.size.width /image.size.width];
        NSLog(@"%f %f",image.size.width,image.size.height);
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
            completionBlock(image, nil, nil);
        });
    });
}

- (void)executeWithCompletionBlock:(void(^)(UIImage *image, NSError *error, NSDictionary *userInfo))completionBlock
{
    
    CGSize size = _gridView.realSize;
    if(size.width < [EditorUtil minWidth] || size.height < [EditorUtil minHeight]){
        self.complte = completionBlock;
        [Alert confirm:@"保存的图片清晰度低于打印标准,可能造成定制卡不清晰,确定要保存吗?" title:@"温馨提示" delegate:self];
        NSError* error = [[NSError alloc]init];
         completionBlock(NULL,error,NULL);
    }else{
        [self executeWithCompletionBlockImpl:^(UIImage *image, NSError *error, NSDictionary *userInfo) {
            completionBlock(image,error,userInfo);
        }];
    }
    
}
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex == 1){
        [self executeWithCompletionBlockImpl:^(UIImage *image, NSError *error, NSDictionary *userInfo) {
            self.complte(image,error,userInfo);
        }];
    }
}
#pragma mark-


- (void)tappedMenu:(UIView*)sender
{
  
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

- (void)sliderWithValue:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max
{
    UISlider *slider = _rotateSlider;
    
    
    slider.continuous = YES;
    [slider addTarget:self action:@selector(sliderDidChange:) forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
    
}

- (void)sliderDidChange:(UISlider*)slider
{
    [self rotateStateDidChange];
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
    self.editor.imageView.layer.transform = transform;
    _rotatePanel.gridRect = self.editor.imageView.frame;
    
    _gridView.rate = Wnew / self.editor.imageView.image.size.width;
    _gridView.frame = self.editor.imageView.frame;
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
-(UIImage*)cropImage:(UIImage*)image scale:(CGFloat)zoomScale{
    //这里要重新计算一下zoomscale
    CGRect rct = _gridView.clippingRect;
    rct.size.width  /= zoomScale;
    rct.size.height /= zoomScale;
    rct.origin.x    /= zoomScale;
    rct.origin.y    /= zoomScale;
    NSLog(@"%d %d %d %d",(int)rct.origin.x,(int)rct.origin.y,(int)rct.size.width,(int)rct.size.height);
    return [image crop:rct];
    
}
@end







@implementation CLRotatePanel

-(void)dealloc{
    self.bgColor = nil;
    self.gridColor = nil;
}

- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame
{
    self = [super initWithFrame:superview.bounds];
    if(self){
        self.gridRect = frame;
        [superview addSubview:self];
    }
    return self;
}

- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGRect rct = self.gridRect;
    
    CGContextSetStrokeColorWithColor(context, self.gridColor.CGColor);
    CGContextStrokeRect(context, rct);
}

- (void)setGridRect:(CGRect)gridRect
{
    _gridRect = gridRect;
    [self setNeedsDisplay];
}
@end
