//
//  SafeDetailInfoView.h
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseDetailInfoView.h"
#import "SafeRelationView.h"

@interface SafeDetailInfoView : BaseDetailInfoView
@property (weak, nonatomic) IBOutlet SafeRelationView *relatonView;

@end
