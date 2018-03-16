//
//  DMBorderView.h
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


IB_DESIGNABLE
@interface DMBorderView : UIView

@property (nonatomic,retain) IBInspectable UIColor* borderColor;
@property (nonatomic,assign) IBInspectable NSInteger cornerRadius;
@end
