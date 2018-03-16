//
//  CommonMacro.h
//  ecard
//
//  Created by randy ren on 15/3/12.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#ifndef ecard_CommonMacro_h
#define ecard_CommonMacro_h



#if __IPHONE_6_0 // iOS6 and later

#   define kTextAlignmentCenter    NSTextAlignmentCenter
#   define kTextAlignmentLeft      NSTextAlignmentLeft
#   define kTextAlignmentRight     NSTextAlignmentRight

#   define kTextLineBreakByWordWrapping      NSLineBreakByWordWrapping
#   define kTextLineBreakByCharWrapping      NSLineBreakByCharWrapping
#   define kTextLineBreakByClipping          NSLineBreakByClipping
#   define kTextLineBreakByTruncatingHead    NSLineBreakByTruncatingHead
#   define kTextLineBreakByTruncatingTail    NSLineBreakByTruncatingTail
#   define kTextLineBreakByTruncatingMiddle  NSLineBreakByTruncatingMiddle

#else // older versions

#   define kTextAlignmentCenter    UITextAlignmentCenter
#   define kTextAlignmentLeft      UITextAlignmentLeft
#   define kTextAlignmentRight     UITextAlignmentRight

#   define kTextLineBreakByWordWrapping       UILineBreakModeWordWrap
#   define kTextLineBreakByCharWrapping       UILineBreakModeCharacterWrap
#   define kTextLineBreakByClipping           UILineBreakModeClip
#   define kTextLineBreakByTruncatingHead     UILineBreakModeHeadTruncation
#   define kTextLineBreakByTruncatingTail     UILineBreakModeTailTruncation
#   define kTextLineBreakByTruncatingMiddle   UILineBreakModeMiddleTruncation

#endif





#define IOS7 [[[UIDevice currentDevice] systemVersion]floatValue]>=7


#define StateBarHeight ((IOS7)?20:0)

#define NavBarHeight (65)

#define BottomHeight ((IOS7)?49:0)

#define ScreenHeight ((IOS7)?([UIScreen mainScreen].bounds.size.height):([UIScreen mainScreen].bounds.size.height - 20))

#define ConentViewWidth  [UIScreen mainScreen].bounds.size.width

#define ConentViewHeight ((IOS7)?([UIScreen mainScreen].bounds.size.height - NavBarHeight):([UIScreen mainScreen].bounds.size.height - NavBarHeight))

#define ConentViewFrame CGRectMake(0,(IOS7)?NavBarHeight:0,ConentViewWidth,ConentViewHeight)
#define ThridContentViewFrame CGRectMake(0,NavBarHeight,ConentViewWidth,ConentViewHeight)
#define MaskViewDefaultFrame CGRectMake(0,NavBarHeight,ConentViewWidth,ConentViewHeight)

#define MaskViewFullFrame CGRectMake(0,0,ConentViewWidth,[UIScreen mainScreen].bounds.size.height-20)

#define NewContentFrame CGRectMake(0,(IOS7)?NavBarHeight:0,ConentViewWidth,ConentViewHeight)


#endif
