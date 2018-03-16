//
//  InsuredSelectButton.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "InsuredSelectButton.h"

@implementation InsuredSelectButton

-(id)initWithFrame:(CGRect)frame{
    if(self =[super initWithFrame:frame]){
        self.layer.borderColor = [UIColor lightGrayColor].CGColor;
        self.layer.borderWidth = 1;
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 4;
        
        self.titleEdgeInsets = UIEdgeInsetsMake(3, 5, 3, 5);
        [self.titleLabel setFont:[UIFont systemFontOfSize:13]];
        [self setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    }
    return self;
}

-(void)sizeToFit{
    
    [self.titleLabel sizeToFit];
    CGRect rect = self.titleLabel.frame;
    
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, rect.size.width+20+2, rect.size.height +10);
    
}


-(void)setSelected:(BOOL)selected{
    [super setSelected:selected];
    if(selected){
        self.layer.borderColor = [UIColor redColor].CGColor;
    }else{
        self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    }
    
}

@end
