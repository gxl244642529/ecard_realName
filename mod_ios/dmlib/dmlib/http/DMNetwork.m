//
//  Network.m
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import "DMNetwork.h"

@interface DMNetwork()
{
    NSURLConnection* _connection;
    NSMutableData* _data;
    NSInteger _retryCount;
    NSMutableURLRequest* _request;
    NSError* _error;
    NSInteger _total;
    __weak id<DMJobDelegate> _delegate;
    
}

@end

@implementation DMNetwork

-(void)dealloc{
    _error = nil;
    _data = nil;
    _connection = nil;
    _request = nil;
}


-(void)cancel{
    if(_connection){
        [_connection cancel];
        _connection = nil;
    }
    _request = nil;
}

-(id)init{
    if(self = [super init]){
        _data = [[NSMutableData alloc]init];
    }
    return self;
}

-(void)executeRequest{
    _connection = [[NSURLConnection alloc]initWithRequest:_request delegate:self startImmediately:NO];
    [_connection start];
    CFRunLoopRun();
}

-(NSData*)execute:(NSMutableURLRequest*)request delegate:(id<DMJobDelegate>)delegate error:(NSError**)error{
    request.cachePolicy = NSURLRequestReloadIgnoringCacheData;
    request.timeoutInterval = 10;
    _request = request;
    _retryCount = 0;
    _delegate = delegate;
    [self executeRequest];
    if(error!=NULL){
        *error = _error;
    }
    
    return _data;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)respons{
    _data.length = 0;
    _total = (NSInteger)respons.expectedContentLength;
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data{
    [_data appendData:data];
    if(_delegate && [_delegate respondsToSelector:@selector(jobProgress:total:progress:)]){
        [self performSelectorOnMainThread:@selector(onProgress:) withObject:self waitUntilDone:NO];
    }
}

-(void)onProgress:(id)sender{
    [_delegate jobProgress:nil total:_total progress:_data.length];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
    CFRunLoopStop(CFRunLoopGetCurrent());
#ifdef DEBUG
    NSLog(@"connection error:%@ code:%ld",[error description],(long)error.code);
#endif
    BOOL shound = [self shoundTry:error.code];
    
    if( shound && _retryCount < 2){
        _retryCount ++;
        [self executeRequest];
    }else{
        _connection = nil;
        _request = nil;
        //网络错误
        _data.length = 0;
        _error = error;
    }
}

-(BOOL)shoundTry:(NSInteger)code{
    return code == -1001;
}


- (void)connectionDidFinishLoading:(NSURLConnection *)aConnection{
    CFRunLoopStop(CFRunLoopGetCurrent());
    _connection = nil;
    _request = nil;
    _error = nil;
    //得到结果
}


@end
