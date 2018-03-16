//
//  NavBarItem.m
//  eCard
//
//  Created by randy ren on 14-1-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "NavBarItem.h"
#import "BaseMacro.h"
static UIColor* _titleTextColor;
@implementation NavBarButtonItem
@synthesize itemType = _itemType;
@synthesize button = _button;
@synthesize title = _title;
@synthesize image = _image;
@synthesize font = _font;
@synthesize normalColor = _normalColor;
@synthesize selectedColor = _selectedColor;
@synthesize selector = _selector;
@synthesize target = _target;
@synthesize highlightedWhileSwitch = _highlightedWhileSwitch;
- (void)dealloc {
    
    self.target = nil;
    self.selector = nil;
    self.normalColor=NULL;
    self.font=NULL;
    self.button=NULL;
    self.title=NULL;
    self.image=NULL;
    self.selectedColor=NULL;
    self.selector=NULL;
}

- (id)initWithType:(NavBarButtonItemType)itemType width:(NSInteger)width height:(NSInteger)height{
    if(self=[super init]){
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [self setButton:button];
        [self setItemType:itemType];
        button.titleLabel.font  = [UIFont systemFontOfSize:10];
        [button setTitleColor:ItemTextNormalColot forState:UIControlStateNormal];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateHighlighted];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateSelected];
        button.frame =CGRectMake(0, 0, width, height);
    }
    return self;
}

- (id)initWithType:(NavBarButtonItemType)itemType
{
    self = [super init];
    if (self) {
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [self setButton:button];
        [self setItemType:itemType];
        button.titleLabel.font  = [UIFont systemFontOfSize:ItemTextFont];
        [button setTitleColor:ItemTextNormalColot forState:UIControlStateNormal];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateHighlighted];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateSelected];
        switch (itemType) {
            case NavBarButtonItemTypeBack:
                button.frame =CGRectMake(0, 0, ItemWidth, ItemHeight);
                break;
            case NavBarButtonItemTypeDefault:
                button.frame =CGRectMake(0, 0, RightMenuButtonWidth, RightMenuButtonHeight);
                break;
        }
        
    }
    
    return self;
}

-(id)initWithType:(NavBarButtonItemType)itemType image:(UIImage*)image
{
    self = [super init];
    if (self) {
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [self setButton:button];
        [self setItemType:itemType image:image];
        button.titleLabel.font  = [UIFont systemFontOfSize:ItemTextFont];
        [button setTitleColor:ItemTextNormalColot forState:UIControlStateNormal];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateHighlighted];
        [button setTitleColor:ItemTextSelectedColot forState:UIControlStateSelected];
        
        switch (itemType) {
            case NavBarButtonItemTypeBack:
                button.frame =CGRectMake(0, 0, ItemWidth, ItemHeight);
                break;
            case NavBarButtonItemTypeDefault:
                button.frame =CGRectMake(0, 0, RightMenuButtonWidth, RightMenuButtonHeight);
                break;
        }
        
    }
    
    return self;
    
}

+ (id)defauleItemWithTarget:(id)target
                     action:(SEL)action
                      title:(NSString *)title
{
    NavBarButtonItem *item = [[NavBarButtonItem alloc]initWithType:NavBarButtonItemTypeDefault];
    [item setTitle: title];
    [item setTarget:target withAction:action];
    return item;
}

+ (id)defauleItemWithTarget:(id)target action:(SEL)action image:(NSString *)image
{
    NavBarButtonItem *item = [[NavBarButtonItem alloc]initWithType:NavBarButtonItemTypeDefault image:[UIImage imageNamed:image]];
    item.image = image;
    [item setTarget:target withAction:action];
    return item;
}
+ (id)backItemWithTarget:(id)target action:(SEL)action image:(NSString *)image{
    NavBarButtonItem *item = [[NavBarButtonItem alloc]initWithType:NavBarButtonItemTypeBack image:[UIImage imageNamed:image]];
    item.image = image;
    [item setTarget:target withAction:action];
    return item;
}
+ (id)backItemWithTarget:(id)target
                  action:(SEL)action
                   title:(NSString *)title
{
    NavBarButtonItem *item = [[NavBarButtonItem alloc]initWithType:NavBarButtonItemTypeBack];
    item.title = title;
    [item setTarget:target withAction:action];
    return item;
}

- (void)setItemType:(NavBarButtonItemType)itemType image:(UIImage*)bgImg
{
    _itemType = itemType;
   // [_button setBackgroundColor:[UIColor grayColor]];
    [_button setBackgroundImage:bgImg forState:UIControlStateNormal];
    [self  titleOffsetWithType];
}

- (void)setItemType:(NavBarButtonItemType)itemType
{
    _itemType = itemType;
    [self  titleOffsetWithType];
}

- (void)setTitle:(NSString *)title
{
    _title = title;
    [_button setTitle:title forState:UIControlStateNormal];
    [_button setTitle:title forState:UIControlStateHighlighted];
    [_button setTitle:title forState:UIControlStateSelected];
    
    [self  titleOffsetWithType];
}

- (void)setImage:(NSString *)image
{
    _image = image;
   /* UIImage *image_ = [UIImage imageNamed:image];
    [_button setImage:image_  forState:UIControlStateNormal];
    [_button setImage:image_ forState:UIControlStateHighlighted];
    [_button setImage:image_ forState:UIControlStateSelected];*/
}

- (void)setFont:(UIFont *)font
{
    _font = font;
    [_button.titleLabel setFont:font];
}

- (void)setNormalColor:(UIColor *)normalColor
{
    _normalColor = normalColor;
    [_button setTitleColor:normalColor forState:UIControlStateNormal];
}

- (void)setSelectedColor:(UIColor *)selectedColor
{
    _selectedColor = selectedColor;
    [_button setTitleColor:selectedColor forState:UIControlStateHighlighted];
    [_button setTitleColor:selectedColor forState:UIControlStateSelected];
}

- (void)setTarget:(id)target withAction:(SEL)action
{
    [_button addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
}


- (void)titleOffsetWithType
{
    switch (_itemType) {
        case NavBarButtonItemTypeBack:
        {
            [_button setContentEdgeInsets:BackItemOffset];
        }
            break;
        case NavBarButtonItemTypeDefault:
        {
            [_button setContentEdgeInsets:UIEdgeInsetsMake(0, 0, 0, 0)];
        }
            break;
        default:
            break;
    }
}

- (void)setHighlightedWhileSwitch:(BOOL)highlightedWhileSwitch
{
}
+(void)setTitleTextColor:(UIColor*)color{
    _titleTextColor = color;
}
@end




@implementation UINavigationItem(CustomBarButtonItem)

- (void)setNewTitle:(NSString *)title
{
    
    UILabel *label = [[UILabel alloc] init];
    label.frame = CGRectMake(0, 0, 180, 20);
    label.backgroundColor = [UIColor clearColor];
    label.tag = 99901;
    label.font = [UIFont systemFontOfSize:TitleFont];
    label.textColor = _titleTextColor;
    label.textAlignment = kTextAlignmentCenter;
    label.text = title;
    self.titleView = label;
    
}

- (void)setNewTitleImage:(UIImage *)image
{
    UIImageView *imageView = [[UIImageView alloc] init];
    CGRect bounds = imageView.bounds;
    imageView.tag = 99902;
    bounds.size  =  image.size;
    imageView.bounds = bounds;
    self.titleView = imageView;
}





- (void)setLeftItemWithTarget:(id)target  action:(SEL)action  title:(NSString *)title
{
    NavBarButtonItem *buttonItem = [NavBarButtonItem defauleItemWithTarget:target  action:action title:title];
    self.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}

- (void)setLeftItemWithTarget:(id)target action:(SEL)action image:(NSString *)image
{
    NavBarButtonItem *buttonItem = [NavBarButtonItem backItemWithTarget:target action:action image:image];
    self.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}


- (void)setLeftItemWithButtonItem:(NavBarButtonItem *)item
{
    self.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:item.button];
}


- (void)setRightItemWithTarget:(id)target action:(SEL)action image:(NSString*)image
{
    NavBarButtonItem *buttonItem = [NavBarButtonItem defauleItemWithTarget:target action:action image:image];
    self.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}


- (void)setRightItemWithTarget:(id)target  action:(SEL)action  title:(NSString *)title
{
    NavBarButtonItem *buttonItem = [NavBarButtonItem defauleItemWithTarget:target
                                                                    action:action
                                                                     title:title];
    self.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}



- (void)setRightItemWithButtonItem:(NavBarButtonItem *)item
{
    self.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:item.button];
}




- (void)setBackItemWithTarget:(id)target action:(SEL)action

{
    NavBarButtonItem *buttonItem = [NavBarButtonItem backItemWithTarget:target
                                                                 action:action
                                                                  title:@""];
    self.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}

- (void)setBackItemWithTarget:(id)target
                       action:(SEL)action
                        title:(NSString *)title
{
    NavBarButtonItem *buttonItem = [NavBarButtonItem backItemWithTarget:target
                                                                 action:action
                                                                  title:title];
    self.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:buttonItem.button];
}

@end



