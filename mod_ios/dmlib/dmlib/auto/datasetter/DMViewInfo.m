//
//  DMViewInfo.m
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewInfo.h"

@implementation DMViewInfo

-(void)dealloc{
    _name = nil;
}

-(id<DMDataSetter>)createSetter:(UIView *)parentView{
    return nil;
}
-(BOOL)shouldHandle:(NSDictionary*)properties{
    return [properties objectForKey:_name]!=nil;
}
@end