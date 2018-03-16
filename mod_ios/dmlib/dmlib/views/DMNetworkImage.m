//
//  UINetworkImage.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMNetworkImage.h"
#import "DACircularProgressView.h"
#import "DMJobManager.h"
#import "UIView+ViewNamed.h"

@interface DMNetworkImage()
{
    DACircularProgressView* _progress;
    UIViewContentMode _oldMode;

}

@end

@implementation DMNetworkImage
-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        _progress = [[DACircularProgressView alloc]initWithFrame:CGRectMake(0, 0, 30, 30)];
        _progress.hidden = YES;
        [self addSubview:_progress];
    }
    return self;
}
-(void)layoutSubviews{
    [super layoutSubviews];
    _progress.center = CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
}
-(CGSize)imageSize{
    return self.image.size;
}
-(void)dealloc{
    self.url = nil;
    _progress = nil;
}
-(void)load:(NSString*)url{
    if([url compare:_url]!=0){
        _url = url;
        _progress.hidden = NO;
        [[DMJobManager sharedInstance]loadImage:_url delegate:self];
    }
}
-(id)val{
    return _url;
}
-(void)setVal:(id)val{
    [self load:val];
}
-(void)jobSuccess:(DMHttpJob*)request{
    _progress.hidden = YES;
    self.contentMode = UIViewContentModeScaleToFill;
    [self setImage:request.data];
    if(_keepRate){
        //设置高度
        UIImage* image = request.data;
        
        double rate = image.size.height / image.size.width;
        
        [self findHeight].constant = self.frame.size.width * rate;
    }
    
}
-(void)jobProgress:(id)request total:(NSInteger)total progress:(NSInteger)progress{
     [_progress setProgress:(float)progress / total];
}
-(BOOL)jobError:(id)request{
     _progress.hidden = YES;
    return YES;
}

-(void)awakeFromNib{
    [super awakeFromNib];
    _oldMode = self.contentMode;
    _progress = [[DACircularProgressView alloc]initWithFrame:CGRectMake(0, 0, 30, 30)];
    _progress.hidden = YES;
    [self addSubview:_progress];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
