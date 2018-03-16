//
//  NewsDetailController.m
//  ecard
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "NewsDetailController.h"

@implementation NewsDetailController
-(void)viewDidLoad{
    [super viewDidLoad];
    [self setTitle:@"新闻详情"];
    
    
    [self load:[DMServers formatUrl:0 url:[NSString stringWithFormat:@"/app_news/news/%@",[self.data valueForKey:@"ID"]]]];
    
}
@end
