//
//  LostCardInfo.h
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LostCardInfo : UIView
@property (weak, nonatomic) IBOutlet UILabel *txtInfo;
@property (weak, nonatomic) IBOutlet UILabel *txtSex;

@property (nonatomic,retain) NSString* phone;
@end
