//
//  SOrderCardView.m
//  ecard
//
//  Created by randy ren on 15-1-23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderCardView.h"

@implementation SOrderCardView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)setData:(CartVo*)data{
    self.count.text = [NSString stringWithFormat:@"%ld",(long)data.count];
    self.price.text = data.priceString;
    self.title.text = data.title;
    self.recharge.text = data.rechargeString;
    if([data isKindOfClass:[SCardListVo class]]){
        SCardListVo* cardListVo = (SCardListVo*)data;
        [JsonTaskManager setImageSrcDirect:self.image src:cardListVo.thumb];
    }else if([data isKindOfClass:[DiyVo class]]){
        DiyVo* diyVo = (DiyVo*)data;
        [JsonTaskManager setImagePath:diyVo.image image:self.image];
    }

}
-(void)awakeFromNib{
  [super awakeFromNib];
    self.image.layer.masksToBounds = YES;
    self.image.layer.cornerRadius = 5;
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
