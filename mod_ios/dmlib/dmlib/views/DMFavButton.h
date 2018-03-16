//
//  DMFavButton.h
//  ecard
//
//  Created by randy ren on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMLoginController.h"
#import "DMValue.h"
#import "DMApiDelegate.h"



IB_DESIGNABLE
@interface DMFavButton : UIView<DMValue,DMApiDelegate,DMLoginDelegate>

-(void)setId:(id)_id;

@property (nonatomic,copy) IBInspectable NSString* isFav;
@property (nonatomic,copy) IBInspectable NSString* fav;

@end
