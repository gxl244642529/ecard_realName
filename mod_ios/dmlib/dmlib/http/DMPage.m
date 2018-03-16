//
//  Page.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPage.h"

@implementation DMPage
-(BOOL)isLast{
    return _page >= _total;
}
-(BOOL)isFirst{
    return _page <=1;
}
-(NSDictionary*)toJson{
  return @{
           @"page":[NSNumber numberWithInteger: self.page],
           @"total":[NSNumber numberWithInteger: self.total],
           @"result":self.list,
           @"extra":self.extra ? self.extra : [NSNull null]
           };
}
@end
