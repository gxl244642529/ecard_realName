//
//  DMCheck.h
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMValue.h"


@protocol DMCheckDelegate <NSObject>

@required
-(void)check:(id)check didChangeSelected:(BOOL)selected;

@end

IB_DESIGNABLE
@interface DMCheck : UIButton<DMValue>

/**
 是否开始选中
 */
@property (nonatomic,assign) IBInspectable BOOL initChecked;


@property (nonatomic,weak) id<DMCheckDelegate> checkDelegate;

@end
