//
//  GridTableViewCell.h
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GridTableViewCell : UITableViewCell


-(BOOL)hasCreated;
-(void)createView:(NSString*)nibName bundle:(NSBundle*)bundle owner:(id)owner count:(NSInteger)count;

@end
