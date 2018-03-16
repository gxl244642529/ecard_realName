//
//  DMFavButton.m
//  ecard
//
//  Created by randy ren on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMFavButton.h"


#import "DMJobManager.h"
#import "Alert.h"

#import "DMAccount.h"

@interface DMFavButton ()
{
    UIButton* _button;
    DMApiJob* _task;
    UIActivityIndicatorView* _indicatorView;
    BOOL _goodsIsFav;
    BOOL _modeIsFav;
    id _id;
}

@end

@implementation DMFavButton


-(void)dealloc{
    _id = nil;
    [[DMNotifier notifier]removeObserver:self];
}
-(void)setId:(id)__id{
    _id = __id;
    if([DMAccount isLogin]){
        [_indicatorView startAnimating];
        _button.hidden = YES;
        Control_AddTarget(_button, onFav);
        [self getFav];
    }else{
        _indicatorView.hidden = YES;
        //去等卢
        Control_AddTarget(_button, onLogin)
    }
    [[DMNotifier notifier]addObserver:self];
}
-(void)awakeFromNib{
    [super awakeFromNib];
    
    _button = self.subviews[0];
    _indicatorView = self.subviews[1];
    
    [_indicatorView startAnimating];
    _button.hidden = YES;
    
}

-(void)getFav{
    _modeIsFav = YES;
    _task = [[DMJobManager sharedInstance]createJsonTask:_isFav cachePolicy:DMCachePolicy_NoCache server:1];
    _task.delegate = self;
    [_task put:@"id" value:_id];
    [_task execute];
}

ON_NOTIFICATION(loginSuccess, id){
    [self getFav];
}



-(void)onLogin:(id)sender{
    [DMAccount callLoginController:nil];
}


-(void)onFav:(UIButton*)sender{
    if([DMAccount isLogin]){
        [_indicatorView startAnimating];
        self.userInteractionEnabled = NO;
        if(!_task){
            _task = [[DMJobManager sharedInstance]createJsonTask:_fav cachePolicy:DMCachePolicy_NoCache server:1];
            _task.delegate = self;
        }
        [_task put:@"id" value:_id];
        if(_goodsIsFav){
            [_task put:@"type" value:@0];
        }else{
            [_task put:@"type" value:@1];
        }
        [_task execute];
    }else{
        [DMAccount callLoginController:nil];
    }
    
    
    
}

-(BOOL)jobMessage:(id)request{
    [_indicatorView stopAnimating];
    self.userInteractionEnabled = YES;
    _indicatorView.hidden = YES;
    _button.hidden = NO;
    return NO;
}

-(void)jobSuccess:(DMApiJob*)request{
    [_indicatorView stopAnimating];
    _indicatorView.hidden = YES;
    _button.hidden = NO;
    self.userInteractionEnabled = YES;
    BOOL fav = [request.data boolValue];
    _button.selected = fav;
   _goodsIsFav = fav;
    if(_modeIsFav){
        
        _modeIsFav = NO;
        _task = nil;
        Control_RemoveTarget(_button, onLogin);
        Control_AddTarget(_button, onFav);
    }else{
        [Alert toast:fav ? @"添加成功" : @"移除成功"];
    }
  
}

-(BOOL)jobError:(id)request{
    [_indicatorView stopAnimating];
    _indicatorView.hidden = YES;
    _button.hidden = NO;
    self.userInteractionEnabled = YES;
    return NO;
}

-(id)val{
    
    return WRAP_BOOL(_button.selected);
}
-(void)setVal:(id)val{
    _button.selected = [val boolValue];
    [_indicatorView stopAnimating];
    _indicatorView.hidden = YES;
    _button.hidden = NO;
}


@end
