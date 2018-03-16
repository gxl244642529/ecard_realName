//
//  MainMenuItem.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "MainMenuItem.h"
#import "CommonMacro.h"
@interface MainMenuItem(){
    __weak MenuData* _data;
}

@end

@implementation MainMenuItem

- (void)awakeFromNib
{
  [super awakeFromNib];
    // Initialization code
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}
-(void)setData:(MenuData*)data{
    _data = data;
    
    
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    _data.selected = selected;
    if(selected){
        dispatch_async(dispatch_get_main_queue(), ^{
            
            [self setBackgroundColor:[UIColor colorWithWhite:(float)0xee/255 alpha:1]];
        });
    }else{
       /* UIImage *backgroundImage = [UIImage imageNamed:@"main_menu_bg.png"];
        backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(1, 1,1, 1)];
        UIImageView* svRect = [[UIImageView alloc] initWithImage:backgroundImage];
        [self setBackgroundView:svRect];*/
        //[self setBackgroundColor:[UIColor colorWithWhite:(float)0xf7/255 alpha:1]];
        [self setBackgroundColor:[UIColor clearColor]];
    }
    
    // Configure the view for the selected state
}

@end
