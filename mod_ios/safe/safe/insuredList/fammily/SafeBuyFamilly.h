//
//  SafeBuyFamilly.h
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "SafeVo.h"
#import "SafeSelContactController.h"
@interface SafeBuyFamilly : DMViewController<DMSubmitDelegate,SafeContactDelegate>

@property (nonatomic,retain) SafeVo* data;
@end
