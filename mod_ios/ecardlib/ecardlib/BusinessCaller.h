//
//  BusinessCaller.h
//  ecardlib
//
//  Created by 任雪亮 on 16/9/24.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ECard.h"

@protocol ECardSelectDelegate <NSObject>

-(void)didSelectECard:(ECard*)ecard;

@end


@protocol ECardPicker <NSObject>

@property (nonatomic,weak) id<ECardSelectDelegate> delegate;

@end


/**
 业务流程
 */
@interface BusinessCaller : NSObject

+(void)selectECard:(UIViewController*)parent delegate:(id<ECardSelectDelegate>)delegate;

+(void)callCashier:(UIViewController*)parent;

@end
