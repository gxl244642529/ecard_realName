//
//  DMAdvVo.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMAdvVo.h"
#import "NSDictionary+Values.h"
#import "DMServers.h"


@implementation DMAdvVo
-(void)fromJson:(NSDictionary *)json{
    _img = [DMServers formatUrl:0 url:json[@"img"]];
    _url = json[@"url"];
    _enable = [json[@"enable"]intValue] == 0 ? false : true;
    _title = json[@"title"];
    NSString* imgBig = [json getString:@"imgBig"];
    if(imgBig != nil && imgBig.length > 0){
        _imgBig = [DMServers formatUrl:0 url:imgBig];
    }else{
        _imgBig = nil;
    }
}
@end
