//
//  DMPay.h
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMPayHeader.h"
#import "DMPayModel.h"
#import "DMApiJob.h"
#import "DMModel.h"

@protocol DMPayResultDelegate <NSObject>


-(void)onPayResult:(id)result;

@end


@class DMPayModel;

@interface DMPay : NSObject<DMJobDelegate>
{
}

@property (nonatomic,assign) DMPayType payType;
@property (nonatomic,weak) DMPayModel* model;

-(BOOL)handleOpenUrl:(NSURL*)url;
-(UIImage*)icon;
-(NSString*)title;



@end
