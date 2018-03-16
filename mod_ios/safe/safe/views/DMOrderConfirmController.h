//
//  DMOrderConfirmController.h
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>


//订单确认
//本controller应该做,
//1\提交订单
//2\提交订单后确认应该对服务器数据进行解析并决定调用哪个controller(收银台)进行付款
//3\付款成功以后,关闭本controller并返回展示成功页面(付款结果)
@interface DMOrderConfirmController : DMViewController<DMPayModelDelegate>
{
    DMPayModel* _payModel;
}

-(void)createPayModel;
@property (nonatomic) Class payResultController;
@end
