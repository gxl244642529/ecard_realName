//
//  EditorUtil.h
//  ecard
//
//  Created by randy ren on 15/3/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol CLImageEditorDelegate <NSObject>



- (void)imageEditorDidFinishEdittingWithImage:(UIImage*)image;
-(void)imageEditorWillFinishEditingWidthImage:(UIImage*)image;

@end

@interface EditorUtil : NSObject

//高度宽度比例
+(void)configEditor:(CGFloat)rate minWidth:(NSInteger)minWidth minHeight:(NSInteger)minHeight;

+(NSInteger)minWidth;
+(NSInteger)minHeight;
+(CGFloat)rate;

+(void)editImage:(UIImage*)image parent:(UIViewController*)parent delegate:(id<CLImageEditorDelegate>)delegate title:(NSString*)title autoEdit:(BOOL)autoEdit;


@end
