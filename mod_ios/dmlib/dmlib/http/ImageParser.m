//
//  ImageParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ImageParser.h"
#import <UIKit/UIKit.h>
#import "DMHttpJob.h"

@interface ImageParser()
{
    __weak NSObject* _lock;
    __weak ImageErrors* _imageErrors;
}

@end

@implementation ImageParser


-(id)initWithLock:(NSObject *)lock  error:(ImageErrors*)error{
    if(self = [super init]){
        _lock = lock;
        _imageErrors = error;
    }
    return self;
}

-(id)parseData:(DMHttpJob*)task data:(NSData*)data error:(NSError**)error{
    UIImage* image = nil;
    @synchronized(_lock) {
        image = [UIImage imageWithData:data];
    }
    if(image){
        //这里将图片数据写入缓存
        return image;
    }
    if(error!=NULL){
         *error = [NSError errorWithDomain:@"非图片数据" code:0 userInfo:nil];
    }
   
    [_imageErrors add:task.url];
    return nil;
}
@end
