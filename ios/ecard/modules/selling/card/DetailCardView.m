//
//  DetailCardView.m
//  ecard
//
//  Created by randy ren on 15-1-23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DetailCardView.h"
#import "SellingModel.h"

@implementation DetailCardView

-(id)initWithHeight:(float)height{
    if(self=[super initWithFrame:CGRectMake(0, 0, 320, height)]){
        float cardWidth = CARD_WIDTH * height / CARD_HEIGHT;
        self.image = [[UIImageView alloc]initWithFrame:CGRectMake((320-cardWidth)/2, 0, cardWidth, height)];
        [self addSubview:self.image];
        self.image.layer.masksToBounds = YES;
        self.image.layer.cornerRadius = 8;
        self.image.backgroundColor = [UIColor colorWithWhite:(float)0xee/255 alpha:0.8];
    }
    return self;
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
