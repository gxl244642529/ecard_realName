//
//  DMApiNetwork.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMApiNetwork.h"

@interface DMApiNetwork ()
{
    NSURLConnection* _connection;
    NSMutableData* _data;
    NSInteger _retryCount;
    NSMutableURLRequest* _request;
    NSError* _error;

}

@end

@implementation DMApiNetwork
-(NSError*)error{
    return _error;
}
-(id)init{
    if(self = [super init]){
        _data = [[NSMutableData alloc]init];
    }
    return self;
}


-(NSData*)execute:(NSInteger)timeoutSeconds url:(NSString*)url extraHeaders:(NSDictionary*)extraHeaders data:(NSData*)data{
    _retryCount = 0;
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc]initWithURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"POST"];
    if(extraHeaders){
        for (NSString* key in extraHeaders) {
            [request setValue:extraHeaders[key] forHTTPHeaderField:key];
        }
    }
    [request setHTTPBody:data];
    request.cachePolicy = NSURLRequestReloadIgnoringCacheData;
    _request = request;
    do{
        [self executeRequest : timeoutSeconds];
        if(!_error){
            return _data;
        }
        
        BOOL shound = [self shoundTry:_error.code];
        if( shound){
            _retryCount ++;
            timeoutSeconds *= 2;
        }else{
            _connection = nil;
            _request = nil;
            //网络错误
            _data.length = 0;
            return nil;
        }
        
    }while(false);
    return nil;
}

- (void)connectionDidFinishLoading:(NSURLConnection *)aConnection{
    CFRunLoopStop(CFRunLoopGetCurrent());
    _connection = nil;
    _request = nil;
    _error = nil;
    //得到结果
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)respons{
    _data.length = 0;
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data{
    [_data appendData:data];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
    CFRunLoopStop(CFRunLoopGetCurrent());
#ifdef DEBUG
    NSLog(@"connection error:%@ code:%ld",[error description],(long)error.code);
#endif
    _error = error;
}


-(BOOL)shoundTry:(NSInteger)code{
    return code == -1001;
}

-(void)executeRequest:(NSInteger)timeoutSecnods{
    _request.timeoutInterval = timeoutSecnods == 0 ? 8 : timeoutSecnods;
    _error = nil;
    _connection = [[NSURLConnection alloc]initWithRequest:_request delegate:self startImmediately:NO];
    [_connection start];
    CFRunLoopRun();
}
-(void)cancel{
    if(_connection){
        [_connection cancel];
        _connection = nil;
    }
    _request = nil;
}

@end
