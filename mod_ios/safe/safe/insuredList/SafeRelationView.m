//
//  SafeRelationView.m
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeRelationView.h"


@interface SafeRelationView()
{
    NSInteger relation;
}

@end

@implementation SafeRelationView

-(void)setVal:(id)val{
     UITextField* text = self.subviews[2];
    relation = [val integerValue];
    switch (relation) {
        case 1:
            text.text = @"父母";
            break;
        case 2:
            text.text = @"配偶";
        case 3:
            text.text = @"子女";
            break;
        default:
            text.text = nil;
            break;
    }
}

-(id)val{
    if(relation>0){
        return [NSNumber numberWithInteger:relation];
    }
    return nil;
}

-(void)awakeFromNib{
    [super awakeFromNib];
    [self setTarget:self withAction:@selector(onClick:)];
    relation = 0;
}

-(void)onClick:(id)sender{
    __weak typeof (self) __self = self;
    [DMPopup select:@[@"父母",@"配偶",@"子女"] selectedIndex:relation-1 title:@"选择与投保人关系" listener:^(NSInteger index,NSString* title) {
        [__self onSelect:index title:title];
    }];
    
}

-(void)onSelect:(NSInteger)index title:(NSString*)title{
    UITextField* text = self.subviews[2];
    text.text = title;
    relation = index + 1;
    _data.relation = relation;
}


@end
