
//
//  NewsController.m
//  ecard
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "NewsController.h"


@interface NewsController()
{
    
}



@property (weak, nonatomic) IBOutlet DMTableView *tableView;

@end

@implementation NewsController


INIT_BUNDLE_CONTROLLER(NewsController, ecardlibbundle.bundle)


-(void)viewDidLoad{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    [self setTitle:@"新闻"];
    [_tableView put:@"last" value:@0];
    [_tableView execute];
    
}

@end
