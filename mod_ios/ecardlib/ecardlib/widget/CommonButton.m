
//
//  CommonButton.m
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "CommonButton.h"


@implementation PageButton

-(void)awakeFromNib{
    [super awakeFromNib];
    [CommonButton createBackground:self];

    
    if(_initDisabled){
        self.enabled = NO;
    }
}

@end

@implementation CommonButton


+(void)createBackground:(UIButton*)view{
    UIImage *backgroundImage = [UIImage imageNamed:@"ic_btn_1"];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
    [view setBackgroundImage:backgroundImage forState:UIControlStateNormal];
    backgroundImage = [UIImage imageNamed:@"ic_btn_2"];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
    [view setBackgroundImage:backgroundImage forState:UIControlStateHighlighted];
    
    view.layer.masksToBounds = YES;
    view.layer.cornerRadius = 5;

}


-(void)awakeFromNib{
    [super awakeFromNib];
   
    [CommonButton createBackground:self];
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
