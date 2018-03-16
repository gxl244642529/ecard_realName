//
//  PullDownMenu.h
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "TreeMenuView.h"




typedef enum _PullDownType{
    PullDownType_Normal,        //普通
    PullDownType_Tree           //tree
}PullDownType;

@interface MenuGroupData : NSObject

@property (nonatomic,retain) NSMutableArray* menuDatas;
@property (nonatomic) PullDownType type;

@end


#define SELF_HEIGHT 36
//tree
//

@interface PullDownMenu : NSObject<MenuViewDelegate>


-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent count:(int)count;


-(void)setData:(NSInteger)index data:(NSArray*)data type:(PullDownType)type;

-(void)setDelegate:(NSObject<MenuViewDelegate>*)delegate;

@end
// CALayerCategory
@interface CALayer (MXAddAnimationAndValue)

- (void)addAnimation:(CAAnimation *)anim andValue:(NSValue *)value forKeyPath:(NSString *)keyPath;

@end