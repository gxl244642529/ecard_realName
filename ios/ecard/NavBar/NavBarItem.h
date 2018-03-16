//
//  NavBarItem.h
//  eCard
//
//  Created by randy ren on 14-1-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


/**
 * @brief 自定义barbuttonitem
 *
 * @param
 * @return
 */

#define TitleFont 18


/*
#define BackItemImage @"left_menu.png"
#define ItemImage @"right_menu.png"
#define BackItemSelectedImage @"left_menu.png"
#define ItemSelectedImage @"left_menu.png"

 */
 
 
#define BackItemOffset UIEdgeInsetsMake(0, 5, 0, 0)
#define ItemLeftMargin 10
#define ItemWidth 27
#define ItemHeight 22
#define ItemTextFont 10
#define ItemTextNormalColot [UIColor whiteColor]
#define ItemTextSelectedColot [UIColor colorWithWhite:0.7 alpha:1]

#define RightMenuButtonWidth 27
#define RightMenuButtonHeight 22


typedef enum {
    
    NavBarButtonItemTypeDefault = 0,
    NavBarButtonItemTypeBack = 1
    
}NavBarButtonItemType;


@interface NavBarButtonItem : NSObject






@property (nonatomic,assign)NavBarButtonItemType itemType;
@property (nonatomic,strong)UIButton *button;
@property (nonatomic,strong)NSString *title;
@property (nonatomic,strong)NSString *image;
@property (nonatomic,strong)UIFont *font;
@property (nonatomic,strong)UIColor *normalColor;
@property (nonatomic,strong)UIColor *selectedColor;
@property (nonatomic,weak)id target;
@property (nonatomic,assign)SEL selector;
@property (nonatomic,assign)BOOL highlightedWhileSwitch;


- (id)initWithType:(NavBarButtonItemType)itemType width:(NSInteger)width height:(NSInteger)height;
+(void)setTitleTextColor:(UIColor*)color;
- (id)initWithType:(NavBarButtonItemType)itemType;
+ (id)defauleItemWithTarget:(id)target action:(SEL)action image:(NSString *)image;
+ (id)defauleItemWithTarget:(id)target action:(SEL)action title:(NSString *)title;
+ (id)backItemWithTarget:(id)target action:(SEL)action title:(NSString *)title;
+ (id)backItemWithTarget:(id)target action:(SEL)action image:(NSString *)image;
- (void)setTarget:(id)target withAction:(SEL)action;


@end


@interface UINavigationItem (CustomBarButtonItem)



- (void)setNewTitle:(NSString *)title;
- (void)setNewTitleImage:(UIImage *)image;

- (void)setLeftItemWithTarget:(id)target action:(SEL)action title:(NSString *)title;
- (void)setLeftItemWithTarget:(id)target action:(SEL)action image:(NSString *)image;
- (void)setLeftItemWithButtonItem:(NavBarButtonItem *)item;

- (void)setRightItemWithTarget:(id)target action:(SEL)action title:(NSString *)title;
- (void)setRightItemWithTarget:(id)target action:(SEL)action image:(NSString*)image;
- (void)setRightItemWithButtonItem:(NavBarButtonItem *)item;



- (void)setBackItemWithTarget:(id)target action:(SEL)action;
- (void)setBackItemWithTarget:(id)target action:(SEL)action title:(NSString *)title;

@end
