//
//  TextTool.m
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TextTool.h"
#import "ViewUtil.h"
#import "TextToolLabel.h"
#import "TextAdjView.h"
#import "ViewUtil.h"
#import "PopupView.h"

#import <ecardlib/ecardlib.h>
#import "PopupView.h"


#import "CLImageToolBase.h"

@interface TextTool()
{
    //移动frame
    CGRect _frame;
    CGFloat _offsetX;
    CGFloat _offsetY;
    TextToolLabel* _tool;
    TextAdjView* _adjView;
    
    CGFloat _minX;
    CGFloat _minY;
    CGFloat _maxX;
    CGFloat _maxY;
}

@end

@implementation TextTool

-(void)dealloc{
    _tool = NULL;
    _adjView = NULL;
}
+ (CGFloat)dockedNumber{
    return 3;
}
+ (NSString*)title{
    return @"文字";
}
+ (BOOL)isAvailable{
    return YES;
}
-(void)setup{
    
    _tool = [[TextToolLabel alloc]init];
    _tool.delegate = self;
    _frame = CGRectOffset(self.editor.imageView.frame, 0, self.editor.scrollView.frame.origin.y) ;
    //实际大小
    [_tool setFontSize:12];
    _tool.center = CGPointMake(_frame.origin.x+_frame.size.width/2, _frame.origin.y + _frame.size.height/2);
    
    [self.editor.view addSubview:_tool];
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panTextView:)];
    [_tool addGestureRecognizer:panGesture];
    UITapGestureRecognizer* tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onTapText:)];
     [_tool addGestureRecognizer:tapGesture];
    
    _adjView = [DMViewUtil createViewFormNibName:@"TextAdjView" bundle:CREATE_BUNDLE(ecardlibbundle.bundle)  owner:self];
    _adjView.select.delegate = self;
    [self.editor.view addSubview:_adjView];
    CGRect frame = self.editor.view.frame;
    LAYOUT_ALIGN_BOTTOM(_adjView);
    _adjView.frame = CGRectMake(0, _adjView.frame.origin.y, self.editor.view.width, _adjView.height);
    
    _adjView.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_adjView.top);
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _adjView.transform = CGAffineTransformIdentity;
                     }];
    
    
    [self sliderWithValue:_adjView.sSize value:12 minimumValue:5 maximumValue:25 action:@selector(onChangeSize:)];
    [self sliderWithValue:_adjView.sh value:1 minimumValue:0 maximumValue:2 action:@selector(onChangeH:)];
    [self sliderWithValue:_adjView.sv value:1 minimumValue:0.5 maximumValue:1.5 action:@selector(onChangeH:)];

    [self updateMaxMove];
}

-(void)onColorSelected:(UIColor*)color{
    [_tool setTextColor:color];
}

-(void)onTapText:(UITapGestureRecognizer*)sender{
    

    
    
    UIAlertView *alertView= [[UIAlertView alloc] initWithTitle:@"提示" message:@"请输入文字" delegate:nil cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    alertView.delegate = self;
    alertView.alertViewStyle = UIAlertViewStylePlainTextInput;
    [[alertView textFieldAtIndex:0] setPlaceholder:@""];
    [[alertView textFieldAtIndex:0] setText:@""];
    
    [alertView show];

}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        
        [_tool setRealText:[alertView textFieldAtIndex:0].text];
        
    }
}


ON_EVENT(onCancelPopup){
    
}

-(void)updateMaxMove{
    //不能大于某个值
    CGRect frame = _tool.frame;
     _minX =_frame.origin.x+frame.size.width/2 ;
     _maxX = _frame.origin.x + _frame.size.width-frame.size.width/2;
    _minY= _frame.origin.y+ frame.size.height/2;
    _maxY =_frame.origin.y + _frame.size.height - frame.size.height/2;
    
   
    _adjView.sh.minimumValue = _minX;
    _adjView.sh.maximumValue = _maxX;
     _adjView.sh.value = _tool.center.x;
    
    _adjView.sv.minimumValue = _minY;
    _adjView.sv.maximumValue = _maxY;
     _adjView.sv.value = _tool.center.y;
    
    
}

-(void)onChangeH:(UISlider*)sender{
    _tool.center = CGPointMake(_adjView.sh.value, _adjView.sv.value);
}

-(void)onChangeSize:(UISlider*)sender{
    
    [_tool setFontSize:sender.value];
    
    CGRect frame = _tool.frame;
    
    if(frame.origin.x + frame.size.width > _frame.origin.x + _frame.size.width){
        frame.origin.x = _frame.origin.x + _frame.size.width - frame.size.width;
    }
    
    if(frame.origin.y + frame.size.height > _frame.origin.y + _frame.size.height){
        frame.origin.y = _frame.origin.y + _frame.size.height - frame.size.height;
    }
    
    _tool.frame = frame;
    [self updateMaxMove];
}



//开始拖动
-(void)onStartDrag:(UIView*)view touch:(UITouch*)touch{
    CGPoint point = [touch locationInView:self.editor.view];
    CGPoint oldCenter = view.center;
    //记录offset
    _offsetX = point.x - oldCenter.x;
    _offsetY = point.y - oldCenter.y;
}

- (void)panTextView:(UIPanGestureRecognizer*)sender{
    CGPoint point=[ sender locationInView:self.editor.view];
    point = CGPointMake(point.x-_offsetX, point.y-_offsetY);
    //不能大于某个值
    CGRect frame = _tool.frame;
    if(point.x < _frame.origin.x +frame.size.width/2){
        point.x = _frame.origin.x+frame.size.width/2 ;
    }
    if(point.x > _frame.origin.x + _frame.size.width-frame.size.width/2){
        point.x = _frame.origin.x + _frame.size.width-frame.size.width/2;
    }
    if(point.y < _frame.origin.y + frame.size.height/2){
        point.y = _frame.origin.y+ frame.size.height/2;
    }
    if(point.y > _frame.origin.y + _frame.size.height - frame.size.height/2){
        point.y =_frame.origin.y + _frame.size.height - frame.size.height/2;
    }
    _tool.center = point;
    _adjView.sh.value = point.x;
    _adjView.sv.value = point.y;
}


- (void)sliderWithValue:( UISlider*) slider value:(CGFloat)value minimumValue:(CGFloat)min maximumValue:(CGFloat)max action:(SEL)action
{
    slider.continuous = YES;
    [slider addTarget:self action:action forControlEvents:UIControlEventValueChanged];
    
    slider.maximumValue = max;
    slider.minimumValue = min;
    slider.value = value;
}


-(void)cleanup{
    [_tool removeFromSuperview];
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         _adjView.transform = CGAffineTransformMakeTranslation(0, self.editor.view.height-_adjView.top);
                     }
                     completion:^(BOOL finished) {
                         [_adjView removeFromSuperview];
                         _adjView = NULL;
                     }];
}



-(void)executeWithCompletionBlock:(void (^)(UIImage *, NSError *, NSDictionary *))completionBlock{
    
    if(_tool.realText){
        
        [SVProgressHUD show];
        
        dispatch_async(GLOBAL_QUEUE, ^{
            
            UIImage* image;
            UIGraphicsBeginImageContext(self.editor.imageView.image.size);
            [self.editor.imageView.image drawAtPoint:CGPointMake(0, 0)];
            //设置实际大小
            //绘制的
            
            UILabel* label = [[UILabel alloc]init];
            label.text = _tool.realText;
            label.font = [UIFont fontWithName:@"HelveticaNeue" size:_adjView.sSize.value / self.editor.scrollView.zoomScale];
            label.numberOfLines = 0;
            label.backgroundColor = [UIColor clearColor];
            label.textColor = _tool.textColor;
            [label sizeToFit];
            
            CGRect frame = _tool.frame;
            //在_frame中德位置
            frame = CGRectMake(frame.origin.x - _frame.origin.x, frame.origin.y-_frame.origin.y, frame.size.width, frame.size.height);
            frame.origin.x /= self.editor.scrollView.zoomScale;
            frame.origin.y /= self.editor.scrollView.zoomScale;
            frame.size.width /= self.editor.scrollView.zoomScale;
            frame.size.height /= self.editor.scrollView.zoomScale;
            //绘制文字
            [label drawTextInRect:frame];
            
            image= UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();

            dispatch_async(dispatch_get_main_queue(), ^{
                [SVProgressHUD dismiss];
                completionBlock(image,NULL,NULL);
            });
        });
        
        
        
    }else{
        
        [SVProgressHUD showErrorWithStatus:@"请输入文字"];
        NSError* error = [[NSError alloc]init];
        completionBlock(NULL,error,NULL);
    }
    
}

@end
