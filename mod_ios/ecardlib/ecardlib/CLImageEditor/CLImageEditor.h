//
//  CLImageEditor.h
//
//  Created by sho yakushiji on 2013/10/17.
//  Copyright (c) 2013年 CALACULU. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <dmlib/dmlib.h>

#import "EditorUtil.h"

@interface ImageEditInfo : NSObject

@property (nonatomic) int minWidth;
@property (nonatomic) int minHeight;
@property (nonatomic) double rate;
@property (nonatomic) NSString* title;
@property (nonatomic) BOOL forceEdit;

@end




@interface CLImageEditor :DMViewController
{
    
}
@property (nonatomic, weak) id<CLImageEditorDelegate> delegate;

- (id)initWithImage:(UIImage *)image title:(NSString*)title  autoEdit:(BOOL)autoEdit;

@end



