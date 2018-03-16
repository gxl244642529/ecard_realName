//
//  IDrag.h
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IDrag <NSObject>

//开始拖动
-(void)onStartDrag:(UIView*)view touch:(UITouch*)touch;

@end
