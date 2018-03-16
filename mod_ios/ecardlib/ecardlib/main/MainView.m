//
//  MainView.m
//  ecard
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MainView.h"
#import <DMLib/DMLib.h>
#import "MainPane.h"

#define BOTTOM_BUTTON_SIZE 62

@interface MainView()
{
    CGFloat _offsetX;
    CGFloat _offsetY;
    CGPoint _initPos;
    CGSize _initSize;
    CGFloat _hwRate;
    
  __weak IBOutlet NSLayoutConstraint *_bb;
  __weak IBOutlet NSLayoutConstraint *_bl;
  
  __weak IBOutlet NSLayoutConstraint *_paneTop;
  __weak IBOutlet NSLayoutConstraint *_paneSize;
    CGFloat _lastCenterX;
    CGFloat _lastCenterY;
}

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewHeight;
@property (weak, nonatomic) IBOutlet UIButton *bottonButton;
@property (weak, nonatomic) IBOutlet DMScrollView *scrollView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgWidth;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgTop;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgLeft;

@end

@implementation MainView

-(void)updateMargin{
    _headView.layer.cornerRadius = 0.3125 * SCREEN_WIDTH / 2;
    _headView.layer.masksToBounds = YES;
}



-(void)awakeFromNib{
    [super awakeFromNib];
  
  CGFloat screenWidth = [UIScreen mainScreen].bounds.size.width;
  
    _bgWidth.constant = screenWidth;
    _bgHeight.constant = screenWidth / 320 * 549;
    _viewHeight.constant = SCREEN_HEIGHT-62;
    _headBg.userInteractionEnabled = YES;
    //这里加载
    _bottonButton.hidden = YES;
    UIPanGestureRecognizer* paneGesture = [[UIPanGestureRecognizer alloc]initWithTarget:self action:@selector(onPan:)];
    [_bottonButton addGestureRecognizer:paneGesture];
    [self performSelector:@selector(onSwapToSmall:) withObject:self afterDelay:1.0];
    
    _initSize = CGSizeMake([UIScreen mainScreen].bounds.size.width, 500* [UIScreen mainScreen].bounds.size.width/320 );
    _hwRate = _initSize.height / _initSize.width;
    _initPos = CGPointMake(0, -100);
  _paneTop.constant = 12;
 // _paneTop.constant =
  
  _lastCenterX = _bl.constant + BOTTOM_BUTTON_SIZE/2;
    _lastCenterY = [UIScreen mainScreen].bounds.size.height-65 -( _bb.constant + BOTTOM_BUTTON_SIZE/2);
    Control_AddTarget(_mainPane.icon, onSwapToSmall);
  
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
 
    /*
    CGPoint offset = scrollView.contentOffset;
    if(offset.y >0){
        [scrollView setContentOffset:CGPointMake(0, 0)];
        return;
    }
    //这里的大小为拖动的距离和width的比例
    //CGFloat rate = offset.y / [UIScreen mainScreen].bounds.size.width;
    
    CGFloat rate = - offset.y / 8;
    CGFloat width = _initSize.width + 2*rate;
  _bgTop.constant =_initPos.y+rate;
  _bgLeft.constant = -rate;
  _bgWidth.constant =width;
  _bgHeight.constant =_hwRate*width;
  
    */
}

-(void)onPan:(UIPanGestureRecognizer*)pane{
    
    CGPoint event = [pane locationInView:self];
    if(pane.state == UIGestureRecognizerStateBegan){
        CGPoint pt = pane.view.center;
        _offsetX = event.x - pt.x;
        _offsetY = event.y - pt.y;
    }else{
        CGFloat dx = event.x-_offsetX;
        CGFloat dy = event.y-_offsetY;
        dx = MAX(_bottonButton.frame.size.width/2,MIN(self.frame.size.width - _bottonButton.frame.size.width/2, dx));
        dy = MAX( _bottonButton.frame.size.height/2,MIN(self.frame.size.height - _bottonButton.frame.size.height/2, dy));
       // pane.view.center = CGPointMake(dx, dy);
        
        _lastCenterX = dx;
        _lastCenterY = dy;
      
      _bl.constant = dx-BOTTOM_BUTTON_SIZE/2;
      _bb.constant = self.frame.size.height - dy - BOTTOM_BUTTON_SIZE/2;
      
     //   _mainPane.center = pane.view.center;
    }
}



- (IBAction)onSwapToSmall:(id)sender {
    [_mainPane hideButtons];
    //截图s
    [UIView animateWithDuration:0.3
                          delay:0
                        options:UIViewAnimationOptionAllowUserInteraction  | UIViewAnimationCurveEaseInOut | UIViewAnimationOptionBeginFromCurrentState
                     animations:^{
                        
                         _mainPane.center = _bottonButton.center;
                         [_mainPane makeSmall];
                     }
                     completion:^(BOOL finished){
                         _bottonButton.hidden = NO;
                         _mainPane.hidden = YES;
                     }];
    
    
}
- (IBAction)onSwapToBig:(id)sender {
  
  Control_AddTarget(_mainPane.icon, onSwapToSmall);
    _mainPane.center = CGPointMake(_lastCenterX, _lastCenterY);
    _bottonButton.hidden = YES;
    _mainPane.hidden = NO;
    [_mainPane hideButtons];
   // [_pane makeSmall];
    
    
    [UIView animateWithDuration:0.3
                          delay:0
                        options:UIViewAnimationOptionAllowUserInteraction  | UIViewAnimationCurveEaseInOut | UIViewAnimationOptionBeginFromCurrentState
                     animations:^{
                         _mainPane.center = _headView.center;
                         _paneTop.constant = _headView.center.y - _paneSize.constant/2;
                         [_mainPane makeBig];
                     } completion:^(BOOL finished) {
                         [_mainPane showButtons];
                     }];
    
    
}



@end
