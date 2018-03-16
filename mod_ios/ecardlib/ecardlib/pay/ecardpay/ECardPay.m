//
//  ECardPay.m
//  ecardlib
//
//  Created by 任雪亮 on 16/10/18.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "ECardPay.h"
#import "ECardPayController.h"

@interface ECardPay()
{
    NSString* _platId;
}

@end

@implementation ECardPay
-(id)init{
    if(self =[super init]){
        self.payType = DMPAY_ETONGKA;
    }
    return self;
}

-(void)dealloc{
    _platId = nil;
}

-(NSString*)title{
    return @"e通卡后台账户支付";
}


-(UIImage*)icon{
    return [UIImage imageNamed:@"ecardlibbundle.bundle/s_pay_3"];
}



-(void)onPayResult:(id)result{
    /*
    Map<String, Object> data = new HashMap<String, Object>();
				data.put("platId", platId);
				getModel().getOrderInfo(data, true);
    */
    
    NSMutableDictionary* data = [[NSMutableDictionary alloc]init];
    [data setValue:_platId forKey:@"platId"];
  
    [self.model notifyClientPaySuccess:self data:data];
    
    //[self.model getOrderInfo:data];
    //[self.model notifyPaySuccess:result];
}



-(void)jobSuccess:(DMApiJob*)request{
    
  
    NSDictionary *resultMap =request.data;
    
    _platId = resultMap[@"order_id"];
    
    NSMutableDictionary* data = [[NSMutableDictionary alloc]initWithDictionary:resultMap];
    
    [data setValue:_platId forKey:@"platId"];
    
    ECardPayController* controller = [[ECardPayController alloc]init];
    controller.payDelegate = self;
    controller.data = data;
    
    
    
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
    
    
    
    
}

@end
