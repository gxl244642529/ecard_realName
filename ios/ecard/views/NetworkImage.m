//
//  NetworkImage.m
//  ecard
//
//  Created by randy ren on 15/4/23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "NetworkImage.h"

@interface NetworkImage()


@end

@implementation NetworkImage

-(void)jobSuccess:(id)request{
    [super jobSuccess:request];
    if(_delegate){
         [_delegate networkImageDidLoaded:self];
    }
   
}

@end
