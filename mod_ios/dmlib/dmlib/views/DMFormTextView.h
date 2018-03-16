//
//  DMFormTextView.h
//  DMLib
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMFormElement.h"

IB_DESIGNABLE
@interface DMFormTextView : UITextView<DMFormElement>

@property (nonatomic,retain) IBInspectable NSString* fieldName;
@property (nonatomic,retain) IBInspectable NSString* validate;
@property (nonatomic,assign) IBInspectable BOOL required;
@end
