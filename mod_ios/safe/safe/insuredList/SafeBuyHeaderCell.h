//
//  SafeBuyHeaderCell.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/dmlib.h>



@interface SafeBuyHeaderCell : UITableViewCell
@property (weak, nonatomic) IBOutlet DMCheck *check;
@property (weak, nonatomic) IBOutlet UITextField *idCard;

@property (weak, nonatomic) IBOutlet UITextField *name;
@property (weak, nonatomic) IBOutlet UITextField *phone;


-(NSString*)validate:(NSMutableDictionary*)data;



@end
