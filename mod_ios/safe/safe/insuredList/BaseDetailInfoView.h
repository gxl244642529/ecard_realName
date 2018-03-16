//
//  BaseDetailInfoView.h
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "SafeContact.h"

@interface BaseDetailInfoView : UIView
{
}

@property (nonatomic,weak) UITextField* idCard;
@property (nonatomic,weak) UITextField* name;

-(void)getInsured:(SafeContact*)dic;
-(NSString*)validate;
@end
