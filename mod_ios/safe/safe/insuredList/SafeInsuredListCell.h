//
//  SafeInsuredListCell.h
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/DMLib.h>

@interface SafeInsuredListCell : UITableViewCell<DMFormElement>
@property (weak, nonatomic) IBOutlet DMFixTableView *tableView;

-(NSMutableArray*)getInsuredList;
@end
