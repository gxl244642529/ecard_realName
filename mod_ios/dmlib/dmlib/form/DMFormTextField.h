//
//  FormTextField.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "DMFormElement.h"
IB_DESIGNABLE
@interface DMFormTextField : UITextField<DMFormElement>


@property (nonatomic,retain) IBInspectable NSString* fieldName;
@property (nonatomic,retain) IBInspectable NSString* validate;
@property (nonatomic,assign) IBInspectable BOOL required;


@end
