//
//  FileDownloader.m
//  randycommonlib
//
//  Created by randy ren on 14-9-29.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "FileDownloader.h"
#import <DMLib/dmlib.h>
#import <ecardlib/ecardlib.h>

@interface FileDownloader()
{
    NSInteger _length;//文件长度
    __weak NSObject<IFileDownloaderDelegate>* _delegate;
    NSInteger _received;
    NSString* _path;
}

@end

@implementation FileDownloader

-(void)dealloc{
    _path = NULL;
}


//取消下载
-(void)cancelFileDownload{
    
  
}
-(void)setDelegate:(NSObject<IFileDownloaderDelegate>*)delegate{
    _delegate = delegate;
}
-(void)download:(NSString*)url{
    
    _length = 0;
    _received=0;
      url = [url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    
    NSString* path = [CacheUtil getPath:@"images"];
    
    NSString* file = [path stringByAppendingPathComponent:[CommonUtil md5:url]];
    _path = file;
    if([[NSFileManager defaultManager]fileExistsAtPath:file]){
        
        [_delegate fileDidComplete:self path:file];
        
    }else{
     /*   [_request setDownloadDestinationPath:file];
        NSString *tempPath = [NSString stringWithFormat:@"%@.tmp",file];
        //设置临时文件路径
        [_request setTemporaryFileDownloadPath:tempPath];
        //设置进度条的代理,
        [_request setDownloadProgressDelegate:self];
        _request.delegate = self;
        [_request setDidFailSelector:@selector(didFailedDownload:)];
        [_request setDidFinishSelector:@selector(didFinishDownoad:)];
        [_request setDidReceiveResponseHeadersSelector:@selector(request:didReceiveResponseHeaders:)];
        //设置是是否支持断点下载
        [_request setAllowResumeForFileDownloads:YES];
        [_request startAsynchronous];*/

    }
    
   }

-(void)request:(id)sender didReceiveBytes:(unsigned long long)value{
    _received += (int)value;
    if(_length>0){
        [_delegate fileProgress:self progress: (double)_received * 100 / (double)_length];
    }
}

- (void)didFinishDownoad:(id)response{
    
  /*  [_request clearDelegatesAndCancel];
    _request = NULL;
    [_delegate fileDidComplete:self path:_path];*/
}
- (void)didFailedDownload:(id)response{
    
    /*[_request clearDelegatesAndCancel];
    _request = NULL;
    [_delegate fileDidFailed:self];*/
}

-(void)request:(id)request didReceiveResponseHeaders:(NSDictionary *)responseHeaders {
  //  _length = [[responseHeaders valueForKey:@"Content-Length"]integerValue];
}
@end
