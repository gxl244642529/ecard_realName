//
//  SafeHeaderView.h
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/DMLib.h>

@interface SafeHeaderView : UIView
@property (weak, nonatomic) IBOutlet DMButtonGroup *buttonGroup;
@property (weak, nonatomic) IBOutlet DMAdvView *advView;

@end
