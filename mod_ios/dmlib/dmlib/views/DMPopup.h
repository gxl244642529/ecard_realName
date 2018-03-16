//
//  DMPopup.h
//  libs
//
//  Created by randy ren on 16/1/12.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMMacro.h"



/**
 
使用
 
 */
@interface DMPopup : UIView

//使得contentView消失的状态
BLOCK_PROPERTY(hideState,__kindof UIView* contentView);
//使得contentView出现的状态
BLOCK_PROPERTY(showState,__kindof UIView* contentView);
BLOCK_PROPERTY(didRemove,__kindof UIView* contentView);
BLOCK_PROPERTY(listener,__kindof UIView* contentView,BOOL isOk);


//如果contentView有按钮tag为888,则进行检测
@property (nonatomic,retain) __kindof UIView* contentView;

-(void)show;
-(void)dismiss;


/**
 
 @property (nonatomic,copy) NSString* title;

 */

+(DMPopup*)bottom:(__kindof UIView*)contentView title:(NSString*)title listener:BLOCK_PARAM(listener,__kindof UIView*,BOOL);


+(void)select:(NSArray<NSString*>*)titles selectedIndex:(NSInteger)selectedIndex title:(NSString*)title listener:BLOCK_PARAM(listener,NSInteger index,NSString* title);


+(DMPopup*)bottom:(__kindof UIView*)contentView listener:BLOCK_PARAM(listener,__kindof UIView*,BOOL);

+(void)dismiss;

@end
