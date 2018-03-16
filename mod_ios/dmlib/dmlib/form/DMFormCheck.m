//
//  DMFormCheck.m
//  DMLib
//
//  Created by randy ren on 16/2/25.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormCheck.h"
#import "UIView+ViewNamed.h"

@implementation DMFormCheck


-(NSString*)validate:(NSMutableDictionary*)data{
    
    data[self.viewName]=[NSNumber numberWithBool:self.selected];
    return nil;
}
@end
