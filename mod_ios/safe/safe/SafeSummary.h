//
//  SafeSummary.h
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>
#import <ecardlib/ecardlib.h>

@interface SafeSummary : DMItem<DMMutilValue>
-(void)summary:(NSString*)summary guardUrl:(NSString*)guardUrl;
@end
