//
//  SafeInsuredCell.h
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SafeContact.h"

@interface SafeInsuredCell : UIView
{
    UITextField* _name;
    UITextField* _idCard;
}

-(NSString*)validate:(SafeContact*)data;

@property (nonatomic,weak) SafeContact* data;

@end
