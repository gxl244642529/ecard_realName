//
//  BaseImageTool.h
//  ecard
//
//  Created by randy ren on 15-2-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UIView+Frame.h"


@interface BaseImageTool : NSObject

{
    __weak UIImage* _image;
    __weak UIImageView* _imageView;
    __weak UIScrollView* _parentView;
    __weak UIView* _contentView;
}

//创建
-(void)create:(UIScrollView*)parentView imageView:(UIImageView*)imageView image:(UIImage*)image contentView:(UIView*)contentView;

//销毁
-(void)destroy;

-(NSString*)title;


-(void)createImpl;
- (void)executeWithCompletionBlock:(void (^)(UIImage*, NSError *, NSDictionary *))completionBlock;
@end
