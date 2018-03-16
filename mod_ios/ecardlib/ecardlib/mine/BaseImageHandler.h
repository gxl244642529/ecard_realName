//
//  BaseImageHandler.h
//  ecard
//
//  Created by randy ren on 15/9/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>



@interface BaseImageHandler : NSObject
{
    NSString* _defaultImage;
    NSString* _localImage;
    int _type;
    BOOL _cornor;
}

-(id)initWithDefaultImage:(NSString*)defaultImage localImage:(NSString*)localImage type:(int)type  cornor:(BOOL)cornor;

-(void)handle:(id)view;
-(void)onSaveImage:(UIImage*)image;



-(NSString*)imagePath;





+(BaseImageHandler*)create:(NSString*)defaultImage localImage:(NSString*)localImage key:(NSString*)key  type:(int)type cornor:(BOOL)cornor;


@end
