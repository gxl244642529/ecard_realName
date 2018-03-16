//
//  GoodInfoView.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GoodInfoView.h"

@interface GoodInfoView()

@property (weak, nonatomic) IBOutlet UIButton *sex0;
@property (weak, nonatomic) IBOutlet UIButton *sex1;
@property (weak, nonatomic) IBOutlet UIButton *sex2;

@end

@implementation GoodInfoView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)awakeFromNib{
  
  [super awakeFromNib];
  
    self.sex = [[RadioButtonGroup alloc]init];
    [_sex addButton:_sex0];
    [_sex addButton:_sex1];
    [_sex addButton:_sex2];
    [_sex setSelected:_sex0];
    
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
