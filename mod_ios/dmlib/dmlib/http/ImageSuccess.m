//
//  ImageSuccess.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ImageSuccess.h"
#import <UIKit/UIKit.h>


@interface ImageSuccess()
{
    NSMutableArray* _temp;
    __weak id<DMJobFinish> _delegate;
}

@end

@implementation ImageSuccess

-(void)dealloc{
    _temp = nil;
}

-(id)initWithDelegate:(id<DMJobFinish>)delegate{
    if(self = [super init]){
        _delegate  =delegate;
         _temp = [[NSMutableArray alloc]init];
    }
    return self;
}

-(void)jobSuccess:(id)request{
     [self performSelectorOnMainThread:@selector(onImageSuccessMainThreadSetImage:) withObject:request waitUntilDone:NO];
}

-(void)onImageSuccessMainThreadSetImage:(DMHttpJob*)request{
    if(_temp.count==0){
        [self performSelector:@selector(onSetImage:) withObject:_temp afterDelay:0.2];
    }
    [_temp addObject:request];
}

-(void)onSetImage:(id)sender{
    for (DMHttpJob* request in _temp) {
        UIImageView* imageView = request.extra;
        [imageView setImage:request.data];
        //这里采用完,这里的task可以释放
        //释放
        [_delegate releaseJob:request];
    }
    [_temp removeAllObjects];
}

@end
