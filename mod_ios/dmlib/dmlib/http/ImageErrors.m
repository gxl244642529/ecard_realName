//
//  ImageErrors.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ImageErrors.h"



@interface ImageErrors()
{
    NSMutableDictionary* _dic;
}

@end


@implementation ImageErrors

-(id)init{
    if(self = [super init]){
        //_dic = [[NSMutableDictionary alloc]init];
    }
    return self;
}

-(void)dealloc{
    _dic = nil;
}
-(void)add:(NSString*)url{
    /*@synchronized(self) {
       [_dic setValue:@"1" forKey:url];
    }*/
}
-(BOOL)contains:(NSString*)url{
  /*  @synchronized(self) {
        return [_dic objectForKey:url]!=nil;
    }*/
    return FALSE;
}
@end
