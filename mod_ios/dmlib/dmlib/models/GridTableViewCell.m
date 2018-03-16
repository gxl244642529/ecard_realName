//
//  GridTableViewCell.m
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GridTableViewCell.h"
#import "DMViewUtil.h"
@implementation GridTableViewCell
-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    if(self=[super initWithStyle:style reuseIdentifier:reuseIdentifier]){
        self.autoresizesSubviews = NO;
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.contentView.clipsToBounds = YES;
    }
    return self;
}

-(void)layoutSubviews{
    [super layoutSubviews];
    CGFloat w = self.frame.size.width/self.contentView.subviews.count;
    NSInteger i=0;
    for(UIView* view in self.contentView.subviews){
        view.frame = CGRectMake(w * i, 0, w, self.frame.size.height);
        ++i;
    }
}
-(BOOL)hasCreated{
    return self.contentView.subviews.count > 0;
}
-(void)createView:(NSString*)nibName  bundle:(NSBundle*)bundle owner:(id)owner count:(NSInteger)count{
    
    for(NSInteger i =0 ; i < count; ++i){
        
        UIView* view = [DMViewUtil createViewFormNibName:nibName bundle:bundle owner:owner];
        [self.contentView addSubview:view];
    }
    
    [self setNeedsLayout];
    
}
@end
