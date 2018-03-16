//
//  SafeWebViewUrlParser.m
//  ecard
//
//  Created by 任雪亮 on 16/3/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeWebViewUrlParser.h"
#import "SafeVo.h"
#import "SafeCardDetailController.h"

@implementation SafeWebViewUrlParser

-(BOOL)shouldHandleFunction:(NSString*)key params:(NSArray*)params parent:(UIViewController *)parent{
    if([key isEqualToString:@"safe_ticket"]){
        NSString* ticket = params[2];
        /*    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
         dic[@"insurance_id"] = @"5701";
         dic[@"ticket"] = ticket;
         */
        SafeVo* data = [[SafeVo alloc]init];
        data.title = @"卡保险";
        data.type = 1;
        data.typeId = @"1";
        data.inId = @"5701";
        data.price = @"免费";
        data.ticket = ticket;
        SafeCardDetailController* c = [[SafeCardDetailController alloc]init];
        c.data = data;
        
        //这里跳转
        [parent.navigationController pushViewController:c  animated:YES];
        
        return TRUE;
    }

    return NO;
}
@end
