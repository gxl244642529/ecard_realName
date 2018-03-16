//
//  AddToCartView.m
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AddToCartView.h"
#import "PopupWindow.h"
#import <ecardlib/ecardlib.h>

#import "JsonTaskManager.h"
#import "SellingModel.h"
#import "CardModel.h"
BOOL _isShow;
 __weak PopupWindow* _popupWindow;

@interface AddToCartView()
{
    NSInteger _count;
    __weak NSObject* _target;
    SEL _selector;
   
    
    
}

@end

@implementation AddToCartView

-(void)dealloc{
    _isShow = NO;
    _selector = nil;
    
    if(_popupWindow){
        [_popupWindow remove];
        _popupWindow = nil;
    }
}



-(void)onBuy:(id)sender{
    if(self.store <=0){
        [SVProgressHUD showErrorWithStatus:@"本商品已经没有库存了,请选择其他商品或联系客服"];
        return;
    }
    if([self getRecharge]>=0){
        if(_popupWindow){
            [_popupWindow remove];
            _popupWindow = nil;
        }
        PerformSelector(_target, _selector, self);
    }else{
        [SVProgressHUD showErrorWithStatus:@"请选择预充值"];
    }
    
    
}

-(void)setParam:(NSObject*)target selector:(SEL)selector{
    _target = target;
    _selector = selector;
}

+(void)show:(UIViewController*)parent count:(NSInteger)count recharge:(NSInteger)recharge target:(NSObject*)target selector:(SEL)selector title:(NSString*)title data:(id)data store:(NSInteger)store{
    if(_isShow)return;
    _isShow = YES;
    AddToCartView* view = [ViewUtil createViewFormNibName:@"AddToCartView" owner:self];
    CGRect rect = [UIScreen mainScreen].bounds;
    view.store = store;
    view.txtStore.text = ITOA(view.store);
    if([data isKindOfClass:[DiyVo class]]){
        //diy数据
        
        DiyVo* rd = data;
        view.title.text = rd.title;
        
        
        [view.image setImage:[UIImage imageWithContentsOfFile:rd.thumb]];
        
       // [[JsonTaskManager sharedInstance]setImageSrcDirect:view.image src:rd.thumb];
        view.txtPrice.text = [rd priceString];
        
    }else if([data isKindOfClass:[CartVo class]]){
        //
        CartVo* rd = data;
        view.title.text = rd.title;
        [[JsonTaskManager sharedInstance]setImageSrcDirect:view.image src:rd.thumb];
         view.txtPrice.text = [rd priceString];
    }else if([data isKindOfClass:[SCardListVo class]]){
        SCardListVo* rd = data;
        view.title.text = rd.title;
        [[JsonTaskManager sharedInstance]setImageSrcDirect:view.image src:rd.thumb];
         view.txtPrice.text = [rd priceString];
    }
    
    
    
    
    [view setParam:target selector:selector];
    if(count>0){
        [view setCount:count];
    }
    if(recharge>=0){
        [view setRecharge:recharge];
    }
    view.frame = CGRectMake(0, rect.size.height-view.frame.size.height, rect.size.width, view.frame.size.height);
    [view.btnBuy setTitle:title forState:UIControlStateNormal];
    view.transform = CGAffineTransformMakeTranslation(0, rect.size.height);
    _popupWindow=[PopupWindow show:view beforeState:^(UIView *contentView) {
        contentView.transform = CGAffineTransformMakeTranslation(0, rect.size.height);
    } endState:^(UIView *contentView) {
        contentView.transform = CGAffineTransformIdentity;
    } frame:rect];
}

-(NSInteger)getCount{
    return _count;
}
-(void)setCount:(NSInteger)count{
    _count = count;
     self.txtCount.text = [NSString stringWithFormat:@"%ld",(long)_count];
    
}
-(void)setRecharge:(NSInteger)recharge{
    NSInteger tag = 0;
    switch (recharge) {
        case 0:
            tag = 1000;
            break;
        case 10:
            tag = 1001;
            break;
        case 20:
            tag = 1002;
            break;
        case 30:
             tag = 1003;
            break;
        case 50:
             tag = 1004;
            break;
        case 100:
             tag = 1005;
            break;
        default:
            break;
    }
    [self.container setSelectTag:tag];
}
-(NSInteger)getRecharge{
    switch ([self.container getSelectTag]) {
        case 1000:
            return 0;
            break;
        case 1001:
            return 10;
        case 1002:
            return 20;
        case 1003:
            return 30;
        case 1004:
            return 50;
        case 1005:
            return 100;
        default:
            break;
    }
    return -1;
}
-(void)awakeFromNib{
    [super awakeFromNib];
    _count = 1;
    [self.container setTextColor:[UIColor darkGrayColor] highlighted:[UIColor redColor]];
    Control_AddTarget(self.btnBuy, onBuy);
}
- (IBAction)onSub:(id)sender {
    --_count;
    if(_count < 1)_count=1;
    self.txtCount.text = [NSString stringWithFormat:@"%ld",(long)_count];
}
- (IBAction)onAdd:(id)sender {
     ++_count;
    self.txtCount.text = [NSString stringWithFormat:@"%ld",(long)_count];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
