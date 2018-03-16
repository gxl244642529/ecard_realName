//
//  DetailGroup.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JsonTask.h"
#import "PullToRefreshViewWrap.h"
#import "ObjectJsonTask.h"


#import "IRequestResult.h"
#import "IDetailListener.h"
@interface DetailGroup : NSObject<IRequestResult,IPullToRefreshViewDelegate>
{
    
}

@property (nonatomic,retain) NSObject* data;

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent contentView:(UIView*)contentView task:(ObjectJsonTask*)task;
-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent contentViewNibName:(NSString*)contentViewNibName task:(ObjectJsonTask*)task;

-(void)setListener:(NSObject<IDetailListener>*)listener;

-(void)refreshSize;
-(id)contentView;
@end
