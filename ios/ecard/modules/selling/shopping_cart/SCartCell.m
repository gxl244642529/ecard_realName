//
//  SCartCell.m
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SCartCell.h"
#import "SellingModel.h"
#import "CardModel.h"

@interface SCartCell()
{
    __weak CartVo* _cartVo;
}

@end

@implementation SCartCell

-(void)awakeFromNib
{
  [super awakeFromNib];
    self.image.layer.cornerRadius= 5;//(值越大，角就越圆)
    self.image.layer.masksToBounds= YES;
    Control_AddTarget(self.btnRecharge, onRecharge);
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}
ON_EVENT(onRecharge){
    [self.delegate onRequestRechage:_cartVo current:self];
}
- (IBAction)onSwitch:(id)sender {
    _cartVo.selected = !_cartVo.selected;
    self.btnSwitch.selected = _cartVo.selected ;
    [self.delegate onSelectChange:_cartVo];
}
- (IBAction)onSub:(id)sender {
    _cartVo.count--;
    _cartVo.selected = TRUE;
    if(_cartVo.count < 1)_cartVo.count=1;
   [self updatePrice];
}

- (IBAction)onAdd:(id)sender {
    _cartVo.count++;
    _cartVo.selected = TRUE;
   [self updatePrice];

}
-(void)setData:(CartVo*)data{
    _cartVo = data;
    [self updatePrice];
    self.title.text = data.title;
    self.store.text = ITOA(data.store);
    self.price.text = [data priceString];
    Button_SetTitle(self.btnRecharge,_cartVo.rechargeString);
    self.btnSwitch.selected = _cartVo.selected ;
    [JsonTaskManager setImageSrcDirect:self.image src:data.thumb];
}
-(void)updatePrice{
    
    [self.delegate onSelectChange:_cartVo];
    self.count.text = [NSString stringWithFormat:@"%ld",(long)_cartVo.count];
    self.totalPrice.text = [_cartVo totalPriceString];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
