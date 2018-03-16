//
//  DMBarButtonItem.h
//  DMLib
//
//  Created by randy ren on 16/2/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMAccount.h"

@interface DMBarButtonItem : UIBarButtonItem<DMLoginDelegate>
-(id)initWithImage:(UIImage*)image;
-(id)initWithText:(NSString*)text;
//跳转名称
@property (nonatomic,copy) NSString* segueName;
@property (nonatomic,weak) UIViewController* parent;
@property (nonatomic) Class controllerClass;

@end
