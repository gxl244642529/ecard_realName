//
//  ImageLoadingView.m
//  eCard
//
//  Created by randy ren on 14-2-17.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ImageLoadingView.h"

#import "JsonTaskManager.h"
@interface ImageLoadingView()
{
    UIImageView* _image;
    UIActivityIndicatorView* _indicator;
}


@end

@implementation ImageLoadingView

-(void)dealloc{
    _image = NULL;
    _indicator=NULL;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        _image= [[UIImageView alloc]initWithFrame:self.bounds];
        _image.contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:_image];
        /*
        UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake((self.bounds.size.width-32)/2, (self.bounds.size.height-32)/2, 32, 32)];
        view.center=self.center;
        [self addSubview:view];
        [view setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [view startAnimating];

        _indicator= view;
        */
        
    }
    return self;
}


-(void)setUrl:(NSString*)url
{
    [[JsonTaskManager sharedInstance]setImageSrcDirect:_image src:url];
    /*
    _operation=[[SDWebImageManager sharedManager] downloadWithURL:[NSURL URLWithString:url] options:SDWebImageLowPriority progress:^(NSUInteger receivedSize, long long expectedSize) {
    } completed:^(UIImage *aImage, NSError *error, SDImageCacheType cacheType, BOOL finished) {
        [_image setImage:aImage];
        [_indicator removeFromSuperview];
        _indicator = NULL;
        _operation = NULL;
    }];
     */
}

-(void)removeFromSuperview{
  /*  if(_operation)
    {
        [_operation cancel];
    }*/
    [super removeFromSuperview];
}



@end
