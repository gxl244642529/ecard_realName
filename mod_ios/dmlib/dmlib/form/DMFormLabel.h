//
//  FormLabel.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "DMFormElement.h"
IB_DESIGNABLE
@interface DMFormLabel : UILabel<DMFormElement>


@property (nonatomic,copy) IBInspectable NSString* hint;
@property (nonatomic,assign) IBInspectable BOOL required;




@end
