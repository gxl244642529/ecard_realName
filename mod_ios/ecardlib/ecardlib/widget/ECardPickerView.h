//
//  ECardPickerView.h
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>

#import "BusinessCaller.h"

@interface ECardPickerView : UIView<ECardSelectDelegate>


@property (nonatomic,weak) id<ECardSelectDelegate> delegate;


@end
