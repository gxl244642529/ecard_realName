//
//  WebModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTWebModule.h"

#import "RCTWebViewController.h"

@implementation RCTWebModule
RCT_EXPORT_MODULE();
RCT_EXPORT_METHOD(open:(NSString*)url title:(NSString*)title){
dispatch_async(dispatch_get_main_queue(), ^{
  [[[DMJobManager sharedInstance]topController].navigationController pushViewController:[[RCTWebViewController alloc]initWithTitle:title url:url] animated:YES];

});
  
  
}



@end
