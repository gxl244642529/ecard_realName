//
//  MenuData.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MenuData : NSObject

//
@property (nonatomic,retain) NSString* title;
@property (nonatomic,retain) NSString* realTitle;

//id
@property (nonatomic) NSInteger ID;

@property (nonatomic,retain) NSString* path;


@property (nonatomic) NSInteger count;
@property (nonatomic) BOOL selected;



@end