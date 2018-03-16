//
//  FormScrollView.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-23.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "FormScrollView.h"
#import "CommonMacro.h"
#import <ecardlib/ecardlib.h>

@interface FormScrollView()
{
    UIView* _contentView;
}

@end

@implementation FormScrollView

-(void)dealloc{
    _contentView = NULL;
}
-(id)contentView{
    return _contentView;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    Keyboard_AddObserver_show(onShow);
    Keyboard_AddObserver_change(onChange);
}

-(id)initWithFrame:(CGRect)frame contentNibName:(NSString*)contentNibName{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        Keyboard_AddObserver_show(onShow);
        Keyboard_AddObserver_change(onChange);
        _contentView = [ViewUtil createViewFormNibName:contentNibName owner:self];
        _contentView.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, _contentView.frame.size.height);
        [self addSubview:_contentView];
        self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame));
    }
    return self;

}
-(void)removeFromSuperview{
    [super removeFromSuperview];
    RemoveObserver(self);
}

ON_EVENT(onShow){
    Keyboard_RemoveObserver_show;
    Keyboard_AddObserver_hide(onHide);
    
}

ON_EVENT_TYPE(onHide,NSNotification*){
    Keyboard_AddObserver_show(onShow);
    Keyboard_RemoveObserver_hide;
    self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame));
    
}

ON_EVENT_TYPE(onChange,NSNotification*){
    NSValue *keyboardBoundsValue = [[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardEndRect = [keyboardBoundsValue CGRectValue];
    float kbheight = keyboardEndRect.size.height;
    self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame)+kbheight);
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
