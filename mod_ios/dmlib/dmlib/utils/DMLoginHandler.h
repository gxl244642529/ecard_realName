//
//  DMLoginHandler.h
//  DMLib
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMAccount.h"
@interface DMLoginHandler : NSObject<DMLoginDelegate>

//跳转名称
@property (nonatomic,copy) NSString* segueName;
@property (nonatomic) Class controllerClass;
-(id)initWithButton:(UIButton*)button parent:(UIViewController*)parent;
@end
