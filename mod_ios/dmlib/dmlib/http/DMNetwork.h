//
//  Network.h
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJobDelegate.h"

@interface DMNetwork : NSObject

-(NSData*)execute:(NSMutableURLRequest*)request delegate:(id<DMJobDelegate>)delegate error:(NSError**)error;
-(void)cancel;



@end
