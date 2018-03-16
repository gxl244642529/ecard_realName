//
//  ItemView.m
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ItemView.h"
#import "ColorUtil.h"

@interface ItemView()


@end


@implementation ItemView

-(id)initWithCoder:(NSCoder *)aDecoder{
    if(self=[super initWithCoder:aDecoder]){
        _itemNormalColor = self.backgroundColor;
        _highColor =[ColorUtil itemHighlightColor];
    }
    return self;
}
-(void)setHighColor:(UIColor*)highColor{
    _highColor = highColor;
}

-(id)init{
    if(self=[super init]){
        _itemNormalColor = [UIColor clearColor];
        _highColor =[ColorUtil itemHighlightColor];
    }
    return self;
}
-(void)setNormalColor:(UIColor*)normalColor{
    _itemNormalColor = normalColor;
}
-(id)initWithFrame:(CGRect)frame{
    if(self=[super initWithFrame:frame]){
        _itemNormalColor = [UIColor clearColor];
        _highColor =[ColorUtil itemHighlightColor];
    }
    return self;
}



-(void)dealloc{
    _itemNormalColor = NULL;
    _highColor = nil;
}



-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [super touchesBegan:touches withEvent:event];
    [self setBackgroundColor:_highColor];
}


-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    if(_isTouched)
    {
        [self setBackgroundColor:_itemNormalColor];
    }
    [super touchesEnded:touches withEvent:event];
}

-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    if(_isTouched)
    {
        [self setBackgroundColor:_itemNormalColor];
    }
    [super touchesCancelled:touches withEvent:event];
}
@end
