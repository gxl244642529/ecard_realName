//
//  NewLoginController.h
//  ecard
//
//  Created by 任雪亮 on 17/4/24.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <dmlib/dmlib.h>

@interface NewLoginController : DMViewController
@property (nonatomic,weak) id<DMLoginDelegate> delegate;
@end
