//
//  SafeCarCell.h
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import <DMLib/DMLib.h>


@interface SafeCarCell : UITableViewCell<DMFormElement>
@property (weak, nonatomic) IBOutlet UILabel *carNo;
@property (weak, nonatomic) IBOutlet UITextField *carFrame;

@end
