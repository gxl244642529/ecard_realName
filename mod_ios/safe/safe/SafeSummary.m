//
//  SafeSummary.m
//  ecard
//
//  Created by randy ren on 16/1/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeSummary.h"
#import "SafeUtil.h"
#import "SafeUtil.h"
#import "SafeVo.h"
@interface SafeSummary()
{
    __weak NSLayoutConstraint* _detailHeight;
    NSString* _guardUrl;
    __weak UIViewController* _currentController;
}

@end

@implementation SafeSummary

-(void)awakeFromNib{
    [super awakeFromNib];
    _detailHeight = [self findHeight];
     [self setTarget:self withAction:@selector(onDetail:)];
    _currentController = [self findViewController];
}

-(void)setVal:(id)data{
    
    if([data isKindOfClass:[SafeDetailVo class]]){
        SafeDetailVo* vo = (SafeDetailVo*)data;
         [self summary:vo.summary guardUrl:vo.guardUrl];
    }else{
        [self summary:[data valueForKey:@"summary"] guardUrl:[data valueForKey:@"guardUrl"]];
    }
    
    
   
 //   [self summary:data.sum guardUrl:<#(NSString *)#>]
    
}

-(void)val:(id)data{
    
}

ON_EVENT(onDetail){
   [_currentController.navigationController pushViewController:[[DMWebViewController alloc]initWithTitle:@"保险详情" url:_guardUrl] animated:YES];
    // [DMWebViewController openUrl:[SafeUtil getUrl:_guardUrl] parent:[ViewUtil getController:self] title:@"保险详情" requireLogin:NO];
}


-(void)dealloc{
    _detailHeight = nil;
    _guardUrl = nil;
}



-(void)summary:(NSString*)summary guardUrl:(NSString*)guardUrl{
    _guardUrl = guardUrl;
    [SafeUtil setSafeGuard:summary detailView:self detailHeight:_detailHeight];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
