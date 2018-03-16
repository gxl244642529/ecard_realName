//
//  MyViewController.h
//  libs
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DMJob.h"
#import "DMApiJob.h"
#import "DMMacro.h"
#import "DMControllerDelegate.h"

@interface DMViewController : UIViewController<DMControllerDelegateContainer>

/**
 *数据
 */
@property (nonatomic,retain) id data;

@property (nonatomic,weak) NSObject<DMControllerDelegate>* controllerDelegate;

@property (nonatomic,retain)  NSMutableDictionary<NSString*,DMApiJob*>* taskMap;

@property (nonatomic,retain) NSMutableSet<DMJob*>* taskSet;

-(void)setResult:(id)data;

BLOCK_PROPERTY(completion,id data);


-(void)setTitle:(NSString *)title;
-(void)createLeftButton;

+(UIBarButtonItem*)createTextItem:(NSString*)title target:(id)target action:(SEL)selector;
+(UIBarButtonItem*)createImageItem:(UIImage*)image target:(id)target action:(SEL)selector;
+(UIBarButtonItem*)createImageItem:(UIImage*)image segueName:(NSString*)segueName parent:(UIViewController*)parent;
+(UIBarButtonItem*)createTextItem:(NSString*)title segueName:(NSString*)segueName parent:(UIViewController*)parent;
+(UIBarButtonItem*)createImageItem:(UIImage*)image controllerClass:(Class)controllerClass parent:(UIViewController*)parent;
+(UIBarButtonItem*)createTextItem:(NSString*)title controllerClass:(Class)controllerClass parent:(UIViewController*)parent;
//关闭本controller
-(void)finish;
-(void)finish:(BOOL)animated;

@end
