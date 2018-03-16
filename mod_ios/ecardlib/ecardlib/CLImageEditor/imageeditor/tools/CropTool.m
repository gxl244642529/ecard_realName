//
//  CropTool.m
//  ecard
//
//  Created by randy ren on 15-2-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CropTool.h"
#import "Enums.h"
#import <DMLib/DMLib.h>

#import <ecardlib/ecardlib.h>
#import "TabItemView.h"
#import "ColorUtil.h"

#import "EditorUtil.h"
#import "CLImageToolBase.h"
#import "EditorUtil.h"
#define BOTTOM_HEIGHT 58











#pragma mark- UI components

@interface MyClippingCircle : UIView

@property (nonatomic, strong) UIColor *bgColor;
@property (nonatomic) Dir dir;

@end

@implementation MyClippingCircle
#define SIZE 12
- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGRect rct = self.bounds;
    CGContextSetFillColorWithColor(context, self.bgColor.CGColor);
    
    switch ((int)self.dir) {
        case Dir_LT:
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2-2, 4, SIZE));
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2-2, SIZE, 4));
            break;
        case Dir_LB:
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2-SIZE+2, 4, SIZE));
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2-2, SIZE, 4));
            break;
        case Dir_RT:
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2-2, 4, SIZE));
            CGContextFillRect(context, CGRectMake(rct.size.width/2+2-SIZE, rct.size.height/2-2, SIZE, 4));
            break;
        case Dir_RB:
            CGContextFillRect(context, CGRectMake(rct.size.width/2-2, rct.size.height/2+2-SIZE, 4, SIZE));
            CGContextFillRect(context, CGRectMake(rct.size.width/2+2-SIZE, rct.size.height/2-2, SIZE, 4));
            break;
    }
    
    
    //CGContextFillEllipseInRect(context, rct);
}

@end

@interface MyGridLayar : CALayer
@property (nonatomic, assign) CGRect clippingRect;
@property (nonatomic, strong) UIColor *bgColor;
@property (nonatomic, strong) UIColor *gridColor;
@property (nonatomic,weak) UIView* view;
@end

@implementation MyGridLayar

-(void)dealloc{
    self.bgColor  = NULL;
    self.gridColor = NULL;
}

+ (BOOL)needsDisplayForKey:(NSString*)key
{
    if ([key isEqualToString:@"clippingRect"]) {
        return YES;
    }
    return [super needsDisplayForKey:key];
}

- (id)initWithLayer:(id)layer
{
    self = [super initWithLayer:layer];
    if(self && [layer isKindOfClass:[MyGridLayar class]]){
        self.bgColor   = ((MyGridLayar*)layer).bgColor;
        self.gridColor = ((MyGridLayar*)layer).gridColor;
        self.clippingRect = ((MyGridLayar*)layer).clippingRect;
         self.view = ((MyGridLayar*)layer).view;
    }
    return self;
}

- (void)drawInContext:(CGContextRef)context
{
    CGRect rct = self.view.bounds;
    CGContextSetFillColorWithColor(context, self.bgColor.CGColor);
    CGContextFillRect(context, rct);
    
    CGContextClearRect(context, _clippingRect);
    
    CGContextSetStrokeColorWithColor(context, self.gridColor.CGColor);
    CGContextSetLineWidth(context, 1);
    
    rct = self.clippingRect;
    
    CGContextBeginPath(context);
    CGFloat dW = 0;
    for(int i=0;i<4;++i){
        CGContextMoveToPoint(context, rct.origin.x+dW, rct.origin.y);
        CGContextAddLineToPoint(context, rct.origin.x+dW, rct.origin.y+rct.size.height);
        dW += _clippingRect.size.width/3;
    }
    
    dW = 0;
    for(int i=0;i<4;++i){
        CGContextMoveToPoint(context, rct.origin.x, rct.origin.y+dW);
        CGContextAddLineToPoint(context, rct.origin.x+rct.size.width, rct.origin.y+dW);
        dW += rct.size.height/3;
    }
    CGContextStrokePath(context);
}

@end

@implementation MyClippingPanel
{
    MyGridLayar *_gridLayer;
    MyClippingCircle *_ltView;
    MyClippingCircle *_lbView;
    MyClippingCircle *_rtView;
    MyClippingCircle *_rbView;
    float _offsetX;
    float _offsetY;
    UILabel* _label;
    UIImageView* _warningIcon;
    
    int _realWidth;
    int _realHeight;
}

-(void)dealloc{
    _gridLayer = nil;
    _ltView = nil;
    _lbView = nil;
    _rtView = nil;
    _rbView = nil;
    _warningIcon = nil;
    _label = nil;
}

- (MyClippingCircle*)clippingCircleWithTag:(NSInteger)tag dir:(Dir)dir
{
    MyClippingCircle *view = [[MyClippingCircle alloc] initWithFrame:CGRectMake(0, 0, 45, 45)];
    view.dir = dir;
    view.backgroundColor = [UIColor clearColor];
    view.bgColor = [UIColor whiteColor];
    view.tag = tag;
    
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panCircleView:)];
    [view addGestureRecognizer:panGesture];
    
    [self.superview addSubview:view];
    
    return view;
}

- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if(self){
        [superview addSubview:self];
        
        
        _gridLayer = [[MyGridLayar alloc] init];
        _gridLayer.frame = self.bounds;
        _gridLayer.bgColor   = [UIColor colorWithWhite:1 alpha:0.6];
        _gridLayer.gridColor = [UIColor colorWithWhite:1 alpha:0.6];
        _gridLayer.view = self;
        [self.layer addSublayer:_gridLayer];
        
        
        _label = [[UILabel alloc]init];
        [self addSubview:_label];
        _label.backgroundColor = [UIColor clearColor];
        _label.font = [UIFont systemFontOfSize:9];
        _label.textColor = [UIColor whiteColor];
        _label.numberOfLines = 0;

        
        _ltView = [self clippingCircleWithTag:0 dir:Dir_LT];
        _lbView = [self clippingCircleWithTag:1 dir:Dir_LB];
        _rtView = [self clippingCircleWithTag:2 dir:Dir_RT];
        _rbView = [self clippingCircleWithTag:3 dir:Dir_RB];
        
        UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panGridView:)];
        [self addGestureRecognizer:panGesture];
        
        self.clippingRect = self.bounds;
    }
    return self;
}


- (void)removeFromSuperview
{
    [super removeFromSuperview];
    
    [_ltView removeFromSuperview];
    [_lbView removeFromSuperview];
    [_rtView removeFromSuperview];
    [_rbView removeFromSuperview];
}

- (void)setBgColor:(UIColor *)bgColor
{
    _gridLayer.bgColor = bgColor;
}

- (void)setGridColor:(UIColor *)gridColor
{
    _gridLayer.gridColor = gridColor;
    _ltView.bgColor = _lbView.bgColor = _rtView.bgColor = _rbView.bgColor = [gridColor colorWithAlphaComponent:1];
}
-(CGSize)realSize{
    return CGSizeMake(_realWidth, _realHeight);
}
- (void)setClippingRect:(CGRect)clippingRect
{
    _clippingRect = clippingRect;
    
    _ltView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y) fromView:self];
    _lbView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y+_clippingRect.size.height) fromView:self];
    _rtView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y) fromView:self];
    _rbView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y+_clippingRect.size.height) fromView:self];
    
    _gridLayer.clippingRect = clippingRect;
    
    //
    float rate = self.rate;
    
    int width = clippingRect.size.width / rate;
    int height =clippingRect.size.height / rate;
    
    _realWidth = width;
    _realHeight = height;
    
    
    if(width < [EditorUtil minWidth] || height < [EditorUtil minHeight]){
        if(!_warningIcon){
            _warningIcon = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ic_warning"]];
            [self addSubview:_warningIcon];
        }else{
            if(_warningIcon.hidden)_warningIcon.hidden = NO;
        }
        _warningIcon.center = CGPointMake(_clippingRect.origin.x + _clippingRect.size.width/2, _clippingRect.origin.y + _clippingRect.size.height/2);
        
    }else{
        if(_warningIcon && !_warningIcon.hidden){
            _warningIcon.hidden = YES;
        }
    }
    
    _label.text = [NSString stringWithFormat:@"%dX%d", width,  height];
    _label.frame = CGRectZero;
    [_label sizeToFit];
    _label.center = CGRectCenter(clippingRect);
    
    
    [self setNeedsDisplay];
}
-(void)setFrame:(CGRect)frame{
    [super setFrame:frame];
     _gridLayer.clippingRect = _clippingRect;
    _gridLayer.frame = CGRectMake(0, 0, frame.size.width, frame.size.height);
    [_gridLayer setNeedsDisplay];
    [_gridLayer displayIfNeeded];
    _ltView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y) fromView:self];
    _lbView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y+_clippingRect.size.height) fromView:self];
    _rtView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y) fromView:self];
    _rbView.center = [self.superview convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y+_clippingRect.size.height) fromView:self];
}

- (void)setClippingRect:(CGRect)clippingRect animated:(BOOL)animated
{
    self.clippingRect = clippingRect;
    
    /*
    if(animated){
        [UIView animateWithDuration:kCLImageToolFadeoutDuration
                         animations:^{
                             _ltView.center = [self.superview convertPoint:CGPointMake(clippingRect.origin.x, clippingRect.origin.y) fromView:self];
                             _lbView.center = [self.superview convertPoint:CGPointMake(clippingRect.origin.x, clippingRect.origin.y+clippingRect.size.height) fromView:self];
                             _rtView.center = [self.superview convertPoint:CGPointMake(clippingRect.origin.x+clippingRect.size.width, clippingRect.origin.y) fromView:self];
                             _rbView.center = [self.superview convertPoint:CGPointMake(clippingRect.origin.x+clippingRect.size.width, clippingRect.origin.y+clippingRect.size.height) fromView:self];
                         }
         ];
        
        CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"clippingRect"];
        animation.duration = kCLImageToolFadeoutDuration;
        animation.fromValue = [NSValue valueWithCGRect:_clippingRect];
        animation.toValue = [NSValue valueWithCGRect:clippingRect];
        [_gridLayer addAnimation:animation forKey:nil];
        
        _gridLayer.clippingRect = clippingRect;
        _clippingRect = clippingRect;
        [self setNeedsDisplay];
    }
    else{
        
    }*/
}

- (void)clippingRatioDidChange:(BOOL)animate
{
    CGRect rect = self.bounds;
    if(self.clippingRatio){
        CGFloat H = rect.size.width * self.clippingRatio.ratio;
        if(H<=rect.size.height){
            rect.size.height = H;
        }
        else{
            rect.size.width *= rect.size.height / H;
        }
        
        rect.origin.x = (self.bounds.size.width - rect.size.width) / 2;
        rect.origin.y = (self.bounds.size.height - rect.size.height) / 2;
    }
    [self setClippingRect:rect animated:animate];
}

- (void)setClippingRatio:(MyRatio *)clippingRatio
{
    if(clippingRatio != _clippingRatio){
        _clippingRatio = clippingRatio;
        [self clippingRatioDidChange:YES];
    }
}

- (void)setNeedsDisplay
{
    [super setNeedsDisplay];
    [_gridLayer setNeedsDisplay];
}

- (void)panCircleView:(UIPanGestureRecognizer*)sender
{
    CGPoint point=[ sender locationInView:self];
    
    if(sender.state == UIGestureRecognizerStateBegan){
        CGPoint oldCenter = sender.view.center;
        CGPoint superPoint = [sender locationInView:self.superview];
        //记录offset
        _offsetX = superPoint.x - oldCenter.x;
        _offsetY = superPoint.y - oldCenter.y;
        
    }
     point = CGPointMake(point.x-_offsetX, point.y-_offsetY);
    CGPoint dp = [sender translationInView:self];
    
    CGRect rct = self.clippingRect;
    
    const CGFloat W = self.frame.size.width;
    const CGFloat H = self.frame.size.height;
    CGFloat minX = 0;
    CGFloat minY = 0;
    CGFloat maxX = W;
    CGFloat maxY = H;
    
    CGFloat ratio = (sender.view.tag == 1 || sender.view.tag==2) ? -self.clippingRatio.ratio : self.clippingRatio.ratio;
    
    switch (sender.view.tag) {
        case 0: // upper left
        {
            maxX = MAX((rct.origin.x + rct.size.width)  - 0.1 * W, 0.1 * W);
            maxY = MAX((rct.origin.y + rct.size.height) - 0.1 * H, 0.1 * H);
            
            if(ratio!=0){
                CGFloat y0 = rct.origin.y - ratio * rct.origin.x;
                CGFloat x0 = -y0 / ratio;
                minX = MAX(x0, 0);
                minY = MAX(y0, 0);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y > 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            }
            else{
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = rct.size.width  - (point.x - rct.origin.x);
            rct.size.height = rct.size.height - (point.y - rct.origin.y);
            rct.origin.x = point.x;
            rct.origin.y = point.y;
            break;
        }
        case 1: // lower left
        {
            maxX = MAX((rct.origin.x + rct.size.width)  - 0.1 * W, 0.1 * W);
            minY = MAX(rct.origin.y + 0.1 * H, 0.1 * H);
            
            if(ratio!=0){
                CGFloat y0 = (rct.origin.y + rct.size.height) - ratio* rct.origin.x ;
                CGFloat xh = (H - y0) / ratio;
                minX = MAX(xh, 0);
                maxY = MIN(y0, H);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y < 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            }
            else{
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = rct.size.width  - (point.x - rct.origin.x);
            rct.size.height = point.y - rct.origin.y;
            rct.origin.x = point.x;
            break;
        }
        case 2: // upper right
        {
            minX = MAX(rct.origin.x + 0.1 * W, 0.1 * W);
            maxY = MAX((rct.origin.y + rct.size.height) - 0.1 * H, 0.1 * H);
            
            if(ratio!=0){
                CGFloat y0 = rct.origin.y - ratio * (rct.origin.x + rct.size.width);
                CGFloat yw = ratio * W + y0;
                CGFloat x0 = -y0 / ratio;
                maxX = MIN(x0, W);
                minY = MAX(yw, 0);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y > 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            }
            else{
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = point.x - rct.origin.x;
            rct.size.height = rct.size.height - (point.y - rct.origin.y);
            rct.origin.y = point.y;
            break;
        }
        case 3: // lower right
        {
            minX = MAX(rct.origin.x + 0.1 * W, 0.1 * W);
            minY = MAX(rct.origin.y + 0.1 * H, 0.1 * H);
            
            if(ratio!=0){
                CGFloat y0 = (rct.origin.y + rct.size.height) - ratio * (rct.origin.x + rct.size.width);
                CGFloat yw = ratio * W + y0;
                CGFloat xh = (H - y0) / ratio;
                maxX = MIN(xh, W);
                maxY = MIN(yw, H);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y < 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            }
            else{
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = point.x - rct.origin.x;
            rct.size.height = point.y - rct.origin.y;
            break;
        }
        default:
            break;
    }
    self.clippingRect = rct;
}

- (void)panGridView:(UIPanGestureRecognizer*)sender
{
    static BOOL dragging = NO;
    static CGRect initialRect;
    
    if(sender.state==UIGestureRecognizerStateBegan){
        CGPoint point = [sender locationInView:self];
        dragging = CGRectContainsPoint(_clippingRect, point);
        initialRect = self.clippingRect;
    }
    else if(dragging){
        CGPoint point = [sender translationInView:self];
        CGFloat left  = MIN(MAX(initialRect.origin.x + point.x, 0), self.frame.size.width-initialRect.size.width);
        CGFloat top   = MIN(MAX(initialRect.origin.y + point.y, 0), self.frame.size.height-initialRect.size.height);
        
        CGRect rct = self.clippingRect;
        rct.origin.x = left;
        rct.origin.y = top;
        self.clippingRect = rct;
    }
}
@end




@implementation MyRatio
{
    CGFloat _rate;
}

- (id)initWithValue:(CGFloat)value
{
    self = [super init];
    if(self){
        _rate = value;
    }
    return self;
}



- (CGFloat)ratio
{
   return _rate;
}

@end


@implementation MyRotatePanel

- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame
{
    self = [super initWithFrame:superview.bounds];
    if(self){
        self.gridRect = frame;
        [superview addSubview:self];
    }
    return self;
}

-(void)dealloc{
    self.bgColor = nil;
    self.gridColor = nil;
    
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



@interface CropTool()
{
    MyClippingPanel* _gridView;
    MyRotatePanel* _rotatePanel;
    UIView* _container;
    
    __weak UISlider* _rotateSlider;
    
    NSInteger _flipState1;
    NSInteger _flipState2;
    
    CATransform3D _initialTransform;
    CGRect _initialRect;
    float rate;
    BOOL _rotated;
}

@end






@implementation CropTool

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
    
    //与原来图片的比例

    CGFloat Rw = _rotatePanel.width / Wnew;
    CGFloat Rh = _rotatePanel.height / Hnew;
    CGFloat scale = MIN(Rw, Rh) * 0.95;
    
    transform = CATransform3DScale(transform, scale, scale, 1);
    _imageView.layer.transform = transform;
    _gridView.rate = Wnew / _imageView.image.size.width;
    _gridView.frame = _imageView.frame;
    _rotatePanel.gridRect = _imageView.frame;
}
- (UISlider*)sliderWithValue:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max
{
    UISlider *slider = [[UISlider alloc] initWithFrame:CGRectMake(10, 0, 300, 30)];
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
    
    _rotateSlider.center = CGPointMake(frame.size.width/2, _container.top-20);
    
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

    _gridView = [[MyClippingPanel alloc] initWithSuperview:_parentView frame:_imageView.frame];
    _gridView.rate = _imageView.frame.size.width / _image.size.width;
    _gridView.backgroundColor = [UIColor clearColor];
    _gridView.bgColor = [[UIColor whiteColor] colorWithAlphaComponent:0.8];
    _gridView.gridColor = [[UIColor darkGrayColor] colorWithAlphaComponent:0.8];
    _gridView.clipsToBounds = NO;
    _gridView.clippingRatio = [[MyRatio alloc]initWithValue:[EditorUtil rate]];
    _gridView.clippingRatio.isLandscape = YES;
    
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
    _gridView = NULL;
    _container = NULL;
    _rotatePanel = NULL;
    _rotateSlider = NULL;
}

-(void)destroy{
    
    
    _imageView.layer.transform = _initialTransform;
    
    
    [_gridView removeFromSuperview];
    _gridView = NULL;
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
    
    
    [SVProgressHUD show];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        UIImage *image;
        NSLog(@"%f %f",_image.size.width,_image.size.height);
        image=[self buildImage:_image];
        //image = [_image rotateByDegree:_rotateSlider.value * 180];
         NSLog(@"%f %f",image.size.width,image.size.height);
        image = [self cropImage:image scale: _gridView.frame.size.width /image.size.width];
        //image = [_image rotateByDegree:_rotateSlider.value * 180];//[self buildImage:_image];
       //  NSLog(@"%f %f",image.size.width,image.size.height);
      //  image= [_image imageRotatedByRads:_rotateSlider.value * M_PI transform:CATransform3DGetAffineTransform([self rotateTransform:_initialTransform clockwise:NO])];
       // image = [self cropImage:image scale: _gridView.frame.size.width /image.size.width];
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
            _imageView.layer.transform = _initialTransform;
           completionBlock(image, nil, nil);
        });
    });
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
