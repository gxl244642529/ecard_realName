//
//  ObjectJsonTask.m
//  ecard
//
//  Created by randy ren on 14-4-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ObjectJsonTask.h"


@interface ObjectJsonTask(){
    NSString* _dataID;
    __weak NSObject<IRequestResult>* _delegate;
}

@end

@implementation ObjectJsonTask

-(void)dealloc{
    
    _dataID = nil;
}

-(void)jobSuccess:(DMApiJob*)request{
    [_delegate task:self result:request.data];
    if(_successListener!=nil){
        _successListener(request.data);
    }
}




-(BOOL)isVisited{
    return NO;
}

-(void)setListener:(NSObject<IRequestResult>*)delegate{
    _delegate = delegate;
    [self setErrorDelegate:delegate];
}




-(void)setDataID:(id)dataID{
    _dataID = dataID;
    [self put:@"id" value:dataID];
}
-(void)setDataID:(NSString*)name value:(id)dataID{
    _dataID = dataID;
    [self put:name value:dataID];
}



@end
