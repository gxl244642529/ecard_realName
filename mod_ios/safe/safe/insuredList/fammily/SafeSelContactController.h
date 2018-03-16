//
//  SafeSelContactController.h
//  ecard
//
//  Created by 任雪亮 on 16/3/17.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import "SafeContactCell.h"
#import "SafeVo.h"
#import "SafeContact.h"

@protocol SafeContactDelegate <NSObject>

-(void)onSelectContacts:(NSArray*)arr;

@end

@interface SafeSelContactController : DMViewController<IOnItemClickListener,UITableViewDelegate>


@property (nonatomic,retain) SafeVo* data;

-(void)setSelected:(NSArray*)selected;


@property (nonatomic,weak) id<SafeContactDelegate> delegate;

@end
