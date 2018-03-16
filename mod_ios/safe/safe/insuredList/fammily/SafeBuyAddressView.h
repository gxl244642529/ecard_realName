//
//  SafeBuyAddressView.h
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import <DMLib/DMLib.h>


@interface SafeBuyAddressView : UIView<DMFormElement>
@property (weak, nonatomic) IBOutlet UITextField *addr;
@property (weak, nonatomic) IBOutlet ECardAreaSelector *area;
@end