//
//  TabItemView.h
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ItemView.h"
#import "Enums.h"
#import "ISelectable.h"



IB_DESIGNABLE
@interface TabItemView : ItemView<ISelectable>



@property (nonatomic) IBInspectable NSInteger padding;
@property (nonatomic) IBInspectable NSInteger iconDir;
@property (nonatomic,retain) IBInspectable NSString* text;
@property (nonatomic,retain) IBInspectable NSString* icon;
@property (nonatomic,retain) IBInspectable NSString* iconHigh;
@property (nonatomic) IBInspectable NSInteger iconSize;

/**
 *  设置文字颜色
 *
 *  @param color       <#color description#>
 *  @param highlighted <#highlighted description#>
 */
-(void)setTextColor:(UIColor*)color highlighted:(UIColor*)highlighted;


/**
 *  设置图标
 *
 *  @param name <#name description#>
 */
-(void)setIcon:(NSString*)name highlighted:(NSString*)highlighted;


@end
