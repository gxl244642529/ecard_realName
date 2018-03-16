//
//  SafeBuyAddressCell.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import <DMLib/DMLib.h>


@interface SafeBuyAddressCell : UITableViewCell<DMFormElement>
@property (weak, nonatomic) IBOutlet UITextField *addr;
@property (weak, nonatomic) IBOutlet ECardAreaSelector *area;

@end
