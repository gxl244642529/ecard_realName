//
//  _CLImageEditorViewController.h
//
//  Created by sho yakushiji on 2013/11/05.
//  Copyright (c) 2013年 CALACULU. All rights reserved.
//

#import "../CLImageEditor.h"

#import "../Utils/UIDevice+SystemVersion.h"



@interface _CLImageEditorViewController : CLImageEditor
<UIScrollViewDelegate, UIBarPositioningDelegate>
{
    
}
@property (nonatomic, weak) IBOutlet UIScrollView *scrollView;
@property (nonatomic, retain) UIImageView  *imageView;
@property (nonatomic, retain) UIView *menuView;
@property (nonatomic,retain) NSString* editTitle;





- (id)initWithImage:(UIImage*)image title:(NSString*)title autoEdit:(BOOL)autoEdit;

- (void)resetImageViewFrame;
- (void)resetZoomScaleWithAnimate:(BOOL)animated;

@end
