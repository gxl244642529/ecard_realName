//
//  DMCheck.m
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMCheck.h"

@implementation DMCheck

-(void)awakeFromNib{
    [super awakeFromNib];
    if(_initChecked){
        self.selected = YES;
    }
    [self addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
}

-(void)onClick:(id)sender{
    self.selected = ! self.selected;
    if(_checkDelegate){
         [_checkDelegate check:self didChangeSelected:self.selected];
    }
   
}

-(id)val{
    return [NSNumber numberWithBool:self.selected];
}
-(void)setVal:(id)val{
    self.selected = [val boolValue];
}

@end
