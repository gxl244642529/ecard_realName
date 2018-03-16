//
//  AbsPay.m
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AbsPay.h"

@interface AbsPay()
{
   
   
}

@end

@implementation AbsPay


-(id)initWithModel:(ECardRPayModel*)model controller:(UIViewController*)parent{
    if(self = [super init]){
        _model = model;
        _parent = parent;
    }
    return self;
}

-(BOOL)handleOpenUrl:(NSURL*)url{
    return NO;
}

-(PayType)payType{
    return _payType;
}

/**
 *  预支付成功
 *
 *  @param serverInfo <#serverInfo description#>
 */
-(void)onPrePaySuccess:(NSString*)serverInfo{
    
    
    
}
/**
 *  通知服务器成功
 *
 *  @param result <#result description#>
 */
-(void)onNotifyServerSuccess:(id)result{
    
}
@end
