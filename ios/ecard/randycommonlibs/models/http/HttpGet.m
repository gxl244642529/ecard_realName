//
//  HttpGet.m
//  ecard
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "HttpGet.h"
#import "JsonTaskManager.h"

@protocol IHttpGetParser <NSObject>

-(void)parseData:(NSData*)data;
-(void)onNetworkError:(NSString*)error;

@end




@interface HttpGetJsonParser : NSObject<IHttpGetParser,IJsonTask>
{
    __weak NSObject<IRequestResult>* _listener;
}
-(id)initWithListener:(NSObject<IRequestResult>*)listener;
@end

@implementation HttpGetJsonParser

-(id)initWithListener:(NSObject<IRequestResult> *)listener{
    if(self=[super init]){
         _listener  =listener;
    }
    return self;
}
-(void)parseData:(NSData *)data{
    
     NSError* error;
     NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
     // NSLog(@"%@", [[NSString alloc] initWithData:response.responseData encoding:NSUTF8StringEncoding]);
     if(error){
         [_listener task:self error:[NSString stringWithFormat:@"数据访问错误 Code: %d",(int)[error code]] isNetworkError:NO];
     }else{
         [_listener task:self result:dic];
     }
}
-(void)onNetworkError:(NSString *)error{
    [_listener task:self error:error isNetworkError:YES];
}
@end

//////////////////////////////////////////////
@interface HttpGetXmlParser : NSObject<IHttpGetParser,IJsonTask>
{
    __weak NSObject<IRequestResult>* _listener;
    __weak NSObject<NSXMLParserDelegate>* _parser;
}
@end

@implementation HttpGetXmlParser

-(id)initWithListener:(NSObject<IRequestResult> *)listener parser:(NSObject<NSXMLParserDelegate>*)parser{
    if(self=[super init]){
        _listener  =listener;
        _parser = parser;
    }
    return self;
}
-(void)parseData:(NSData *)data{
    //开始解析xml
    NSXMLParser *xmlParser = [[NSXMLParser alloc] initWithData:data];
    [xmlParser setDelegate:_parser];
    BOOL result = [xmlParser parse];
    if(!result){
        [_listener task:self error:@"解析xml失败" isNetworkError:NO];
    }else{
        [_listener task:self result:self];
    }
}
-(void)onNetworkError:(NSString *)error{
    [_listener task:self error:error isNetworkError:YES];
}
@end

//////////////////////////////////////////////

@interface HttpGet()
{
    NSObject<IHttpGetParser>* _parser;
    DMHttpJob* _job;
}

@end

@implementation HttpGet

-(void)dealloc{
    [_job class];
    _job = nil;
    _parser = NULL;
}

-(void)get:(NSString*)url listener:(NSObject<IRequestResult>*)listener{
    [self config:url];
    _parser =[[HttpGetJsonParser alloc]initWithListener:listener];
   
}
-(void)get:(NSString*)url listener:(NSObject<IRequestResult>*)listener parser:(NSObject<NSXMLParserDelegate>*)parser{
    [self config:url];
    _parser =[[HttpGetXmlParser alloc]initWithListener:listener parser:parser];

}
-(void)config:(NSString*)url{
    url = [url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    [[DMJobManager sharedInstance]get:url delegate:self];
    
}


-(void)jobSuccess:(DMHttpJob*)request{
    
    [_parser parseData:request.data];
    
}


-(BOOL)jobError:(DMHttpJob*)request{
    
    [_parser onNetworkError:request.error.reason];
    return YES;
}
-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [_parser onNetworkError:errorMessage];
}



@end
