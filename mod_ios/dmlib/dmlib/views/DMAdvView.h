//
//  ScrollableAdvView.h
//  eCard
//
//  Created by randy ren on 14-3-1.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IDataAdapterListener.h"
#import "DMJobDelegate.h"

@protocol DMAdvViewDelegate <NSObject>

@required
-(void)adv:(id)view didClick:(id)data;

@end


/**
 轮播广告页面
 *  默认图片tag为1
 */
IB_DESIGNABLE
@interface DMAdvView : UIView<UIScrollViewDelegate,DMJobDelegate>
{
    NSArray* _data;
}

//@property (nonatomic,retain) IBInspectable NSString* api;
//@property (nonatomic,retain) IBInspectable NSString* imageKey;
//@property (nonatomic,retain) IBInspectable NSString* imageLongKey;
@property (nonatomic,retain) IBInspectable UIImage* defaultImage;
//mokuai
@property (nonatomic,copy) IBInspectable NSString* module;
//解析url命令 ，制定一个Class，必须实现DMWebViewUrlParser
@property (nonatomic,copy) IBInspectable NSString* urlParser;
//@property (nonatomic,assign) IBInspectable NSInteger server;

//@property (nonatomic,retain) IBInspectable NSString* titleKey;
//@property (nonatomic,retain) IBInspectable NSString* urlKey;
//@property (nonatomic,retain) IBInspectable NSString* canClickKey;

@property (nonatomic,weak) id<DMAdvViewDelegate> advDelegate;
@property (nonatomic,weak) id<IDataAdapterListener> dataAdapterListener;



-(void)setData:(NSArray*)data;
-(void)start;
-(void)stop;


@end
