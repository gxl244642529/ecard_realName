//
//  DMPopup.m
//  libs
//
//  Created by randy ren on 16/1/12.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPopup.h"
#import "DMMacro.h"


__weak DMPopup* g_popup;

@interface UIListPicker : UIPickerView<UIPickerViewDataSource,UIPickerViewDelegate>

-(id)initWithArray:(NSArray<NSString*>*)dataList selectedIndex:(NSInteger)selectedIndex;
-(NSArray<NSString*>*)array;
@end


@interface UIListPicker(){
    NSArray* _dataList;
}

@end

@implementation UIListPicker
-(NSArray<NSString*>*)array{
    return _dataList;
}
-(id)initWithArray:(NSArray<NSString*>*)dataList selectedIndex:(NSInteger)selectedIndex{
    if(self = [super init]){
        _dataList = dataList;
        self.delegate = self;
        self.dataSource = self;
        if(selectedIndex>0){
            dispatch_async(dispatch_get_main_queue(), ^{
                [self selectRow:selectedIndex inComponent:0 animated:NO];
            });
            
        }
    }
    return self;
}

-(void)dealloc{
    _dataList = nil;
}

// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return _dataList.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return _dataList[row];
}


@end



@interface DMBottomPopupView : UIView{
    __weak DMPopup* _popup;
    UILabel* _label;
    UIButton* _btn;
}
-(id)initWithContentView:(UIView*)contentView title:(NSString*)title popup:(DMPopup*)popup;
@property (nonatomic,retain) __kindof UIView* contentView;
BLOCK_PROPERTY(listener,__kindof UIView* contentView,BOOL ok);
@end



@interface DMPopup()
{
    UIView* _background;
    __kindof UIView* _contentView;
    
}

@end


@implementation DMPopup

+(DMPopup*)createBottomPopup:(UIView*)view{
    view.frame = CGRectMake(0, SCREEN_HEIGHT, SCREEN_WIDTH, view.frame.size.height);
    DMPopup* popup = [[DMPopup alloc]init];
    popup.contentView = view;
    popup.hideState = ^(UIView* view){
        view.transform = CGAffineTransformIdentity;
    };
    popup.showState = ^(UIView* view){
        view.transform = CGAffineTransformMakeTranslation(0, -view.frame.size.height);
    };
    return popup;
}

-(void)dealloc{
    _background = nil;
    _contentView = nil;
}

-(void)setContentView:(__kindof UIView *)contentView{
    _contentView= contentView;
    
    UIButton* button = [contentView viewWithTag:888];
    if(button){
        [button addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
    }
}

-(void)onClick:(id)sender{
    if(_listener!=NULL){
        _listener(_contentView,TRUE);
    }
    [self dismiss];
}


-(void)show
{
    // adding some styles to background view (behind table alert)
    self.frame = [[UIScreen mainScreen] bounds];
    //self.backgroundColor = [UIColor clearColor];
    
    _background = [[UIView alloc]initWithFrame:self.frame];
    [self addSubview:_background];
    
    AddTapGestureRecognizer(_background, onTapBackground);
    
    _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0];
    _background.opaque = NO;
    // adding it as subview of app's UIWindow
    UIWindow *appWindow = [[UIApplication sharedApplication] keyWindow];
    [appWindow addSubview:self];
    
    //_contentView的状态为hideState,frame
    [self addSubview:_contentView];
    
    // get background color darker
    [UIView animateWithDuration:0.2 animations:^{
        _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.25];
        self.showState(_contentView);
    }];
}

-(void)dismiss{
    [UIView animateWithDuration:0.2 animations:^{
        _background.alpha = 0;
        self.hideState(_contentView);
    } completion:^(BOOL finished) {
        if(finished){
            if(_didRemove!=nil){
                _didRemove(_contentView);
            }
            [self removeFromSuperview];
        }
    }];
}

//取消
-(void)onTapBackground{
    //这里cancel
    if(_listener!=nil){
        _listener(_contentView,FALSE);
    }
    [self dismiss];
}
/*
-(void)onOk{
    if(_listener!=nil)_listener(_contentView,TRUE);
    [self fadeBackround];
    [_popup dismiss];
}*/
+(DMPopup*)bottom:(__kindof UIView*)view listener:BLOCK_PARAM(listener,__kindof UIView*,BOOL){
    view.frame = CGRectMake(0, SCREEN_HEIGHT, SCREEN_WIDTH, view.frame.size.height);
    DMPopup* popup = [[DMPopup alloc]init];
    popup.contentView = view;
    popup.hideState = ^(UIView* view){
        view.transform = CGAffineTransformIdentity;
    };
    
    popup.showState = ^(UIView* view){
        view.transform = CGAffineTransformMakeTranslation(0, -view.frame.size.height);
    };
    popup.listener = listener;
    [popup show];
    g_popup  = popup;
    return popup;
}

+(void)dismiss{
    if(g_popup){
        [g_popup dismiss];
        g_popup = nil;
    }
    
}
+(DMPopup*)bottom:(__kindof UIView*)contentView title:(NSString*)title listener:BLOCK_PARAM(listener,__kindof UIView*,BOOL){
    DMPopup* popup = [[DMPopup alloc]init];
    DMBottomPopupView* popupView = [[DMBottomPopupView alloc]initWithContentView:contentView title:title popup:popup];
    popupView.listener = listener;
    popup.contentView = popupView;
    popup.listener = ^(DMBottomPopupView* contentView,BOOL isOk){
        contentView.listener(contentView.contentView,isOk);
    };
    popup.hideState = ^(DMBottomPopupView* contentView){
        contentView.transform = CGAffineTransformIdentity;
    };
    popup.showState = ^(DMBottomPopupView* contentView){
        contentView.transform = CGAffineTransformTranslate(contentView.transform,0, -contentView.frame.size.height);
    };
    [popup show];
     g_popup  = popup;
    return popup;
}
+(void)select:(NSArray<NSString*>*)titles selectedIndex:(NSInteger)selectedIndex  title:(NSString*)title listener:BLOCK_PARAM(listener,NSInteger index,NSString* title){
    
    UIListPicker* picker = [[UIListPicker alloc]initWithArray:titles selectedIndex:selectedIndex];
    [DMPopup bottom:picker title:title listener:^(UIListPicker* picker, BOOL isOk) {
        if(isOk){
            NSInteger index  = [picker selectedRowInComponent:0];
            listener(index,picker.array[index]);
        }
    }];
    
}

@end


@implementation DMBottomPopupView

-(id)initWithContentView:(UIView*)contentView title:(NSString*)title popup:(DMPopup*)popup{
    
    
    
    CGRect rect = [UIScreen mainScreen].bounds;
    //边距20 上面 20 下面20
    CGFloat height =contentView.frame.size.height;
    int self_height = height + 60;
    
    if(self = [super initWithFrame:CGRectMake(0, rect.size.height, rect.size.width, self_height)]){
        self.backgroundColor = [UIColor whiteColor];
        _label = [[UILabel alloc]initWithFrame:CGRectMake(10 , 10,rect.size.width-20, 20)];
        _label.backgroundColor = [UIColor clearColor];
        _label.textColor = [UIColor blackColor];
        _label.font = [UIFont boldSystemFontOfSize:14.0];
        _label.textAlignment = NSTextAlignmentCenter;
        _label.text = title;
        [self addSubview:_label];
        _popup  =popup;
        contentView.frame = CGRectMake(10, 30, rect.size.width-40, height);
        [self addSubview:contentView];
        
        _btn = [[UIButton alloc]initWithFrame:CGRectMake(10, self_height-30, rect.size.width-20, 20)];
        [_btn setTitle:@"确定" forState:UIControlStateNormal];
        [_btn addTarget:self action:@selector(onOk:) forControlEvents:UIControlEventTouchUpInside];
        [_btn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        [self addSubview:_btn];

        _contentView = contentView;
    }
    return self;
}

-(void)dealloc{
    _contentView = nil;
}

-(void)onOk:(id)sender{
    if(_listener!=nil)_listener(_contentView,YES);
    [_popup dismiss];
    _popup = nil;
}

@end
