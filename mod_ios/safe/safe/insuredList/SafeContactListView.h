//
//  SafeContactListView.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/dmlib.h>

#import "InsuredContactView.h"

@interface SafeContactListView : UIView<DMTabDelegate,ContactSelectDelegate>


@property (nonatomic,assign) NSInteger maxInsured;

-(void)show;
-(void)hide;
@end
