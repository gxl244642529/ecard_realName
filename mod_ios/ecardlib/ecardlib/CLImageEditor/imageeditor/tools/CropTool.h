//
//  CropTool.h
//  ecard
//
//  Created by randy ren on 15-2-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "BaseImageTool.h"

@interface MyRatio : NSObject
@property (nonatomic, assign) BOOL isLandscape;
@property (nonatomic, readonly) CGFloat ratio;
- (id)initWithValue:(CGFloat)value;
@end


@interface MyClippingPanel : UIView
@property (nonatomic, assign) CGRect clippingRect;
@property (nonatomic, strong) MyRatio *clippingRatio;
@property (nonatomic) CGFloat rate;

-(CGSize)realSize;

- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame;
- (void)setBgColor:(UIColor*)bgColor;
- (void)setGridColor:(UIColor*)gridColor;
- (void)clippingRatioDidChange:(BOOL)animate;
@end



@interface MyRotatePanel : UIView
@property(nonatomic, strong) UIColor *bgColor;
@property(nonatomic, strong) UIColor *gridColor;
@property(nonatomic, assign) CGRect gridRect;
- (id)initWithSuperview:(UIView*)superview frame:(CGRect)frame;
@end

@interface CropTool : BaseImageTool

@end
