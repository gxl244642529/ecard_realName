//
//  SafeInsuredWithRelationCell.h
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SafeInsuredCell.h"

@protocol SafeInsuredWithRelationCellDelegate <NSObject>

-(void)onRemove:(id)data;

@end

@interface SafeInsuredWithRelationCell : SafeInsuredCell


@property (nonatomic,weak) id<SafeInsuredWithRelationCellDelegate> delegate;


@end
