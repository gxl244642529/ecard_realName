//
//  DMLoginController.h
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger{
    DMPopBySelf,
    DMPopByDelegate
}DMPopType;

@protocol DMLoginDelegate <NSObject>

//登录成功
@optional
-(DMPopType)onLoginSuccess;

@optional
-(void)onLoginCancel;

@end

@protocol DMLoginController <NSObject>


@property (nonatomic,weak) id<DMLoginDelegate> delegate;

@end


