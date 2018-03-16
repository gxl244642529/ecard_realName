//
//  SafeOneInsuredCell.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SafeContact.h"
//获取投保人



//只有一个投保人
@interface SafeOneInsuredCell : UITableViewCell<DMFormElement>
@property (weak, nonatomic) IBOutlet UITextField *name;
@property (weak, nonatomic) IBOutlet UITextField *idCard;


-(void)setVal:(SafeContact*)data;


@end
