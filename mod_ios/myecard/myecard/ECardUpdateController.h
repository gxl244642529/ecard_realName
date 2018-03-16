//
//  ECardUpdateController.h
//  ecard
//
//  Created by randy ren on 16/2/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>


#import <ecardlib/ecardlib.h>

@interface ECardUpdateController : DMViewController<DMSubmitDelegate,UIAlertViewDelegate>

@property (nonatomic,retain) ECard* data;

@end
