//
//  BaseImageTool.m
//  ecard
//
//  Created by randy ren on 15-2-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "BaseImageTool.h"

@implementation BaseImageTool

-(NSString*)title{
    return NULL;
}

//创建
-(void)create:(UIScrollView*)parentView imageView:(UIImageView*)imageView image:(UIImage*)image contentView:(UIView*)contentView{
    _parentView = parentView;
    _contentView = contentView;
    _image = image;
    _imageView = imageView;
    CGFloat minZoomScale = _parentView.minimumZoomScale;
    
    _parentView.maximumZoomScale = 0.95*minZoomScale;
    _parentView.minimumZoomScale = 0.95*minZoomScale;
    
    [_parentView setZoomScale:_parentView.minimumZoomScale animated:YES];
    [self createImpl];
}

//销毁
-(void)destroy{
    
}
-(void)createImpl{
    
}
- (void)executeWithCompletionBlock:(void (^)(UIImage*, NSError *, NSDictionary *))completionBlock{
    
}
@end
