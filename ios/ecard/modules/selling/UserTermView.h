//
//  UserTermView.h
//  ecard
//
//  Created by randy ren on 15/5/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CheckBox.h"
@interface UserTermView : UIView
@property (weak, nonatomic) IBOutlet CheckBox *notShowNextTime;
@property (weak, nonatomic) IBOutlet UITextView *textView;

@end
