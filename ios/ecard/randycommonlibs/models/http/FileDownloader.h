//
//  FileDownloader.h
//  randycommonlib
//
//  Created by randy ren on 14-9-29.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol IFileDownloader <NSObject>
//取消下载
-(void)cancelFileDownload;

@end



@protocol IFileDownloaderDelegate <NSObject>
-(void)fileDidComplete:(NSObject<IFileDownloader>*)downloader path:(NSString*)path;
-(void)fileProgress:(NSObject<IFileDownloader>*)downloader progress:(NSInteger)progress;
-(void)fileDidFailed:(NSObject<IFileDownloader>*)downloader;
@end



@interface FileDownloader : NSObject<IFileDownloader>

-(void)download:(NSString*)url;

//
-(void)setDelegate:(NSObject<IFileDownloaderDelegate>*)delegate;
@end
