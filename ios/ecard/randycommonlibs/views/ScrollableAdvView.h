//
//  ScrollableAdvView.h
//  eCard
//
//  Created by randy ren on 14-3-1.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AbsDataProvider.h"
#import <DMLib/dmlib.h>

@protocol ScrollableAdvViewDelegate <NSObject>

@required
-(void)adv:(id)view didClick:(id)data;

@end


/**
 *  默认图片tag为1
 */
//IB_DESIGNABLE
@interface ScrollableAdvView : UIView<UIScrollViewDelegate,IDataProviderListener>
{
    NSArray* _data;
}

//@property (nonatomic,retain,setter=setDataProviderName:,getter=getDataProviderName) NSString* dataProviderName;

@property (nonatomic,weak) id<ScrollableAdvViewDelegate> advDelegate;
@property (nonatomic,weak) id<IDataAdapterListener> dataListener;

-(void)setData:(NSArray*)data;
-(void)start;
-(void)stop;


-(void)setDataProvider:(id<IDataProvider>)dataProvider;
@end
