//
//  MyViewController.m
//  libs
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewController.h"
#import "DMJobManager.h"
#import "DMJob.h"
#import "DMNotifier.h"
#import "DMColorConfig.h"
#import "DMBarButtonItem.h"
#import "MobClick.h"
#import "DMConfigReader.h"
#import "DMColorUtil.h"

@interface DMViewController()
{
    //任务列表
    BOOL _inited;
}
@end

@implementation DMViewController

-(id)initWithCoder:(NSCoder *)aDecoder{
    if(self = [super initWithCoder:aDecoder]){
        if(!_inited){
            _inited = YES;
            [[DMNotifier notifier]addObserver:self];
        }
        
    }
    return self;
}

-(id)init{
    if(self = [super init]){
        if(!_inited){
            _inited = YES;
            [[DMNotifier notifier]addObserver:self];
        }
    }
    return self;
}

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    if(self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]){
        if(!_inited){
            _inited = YES;
            [[DMNotifier notifier]addObserver:self];
        }
    }
    return self;
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:NSStringFromClass(self.class)];
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:NSStringFromClass(self.class)];
}

-(void)dealloc{
    
    [[DMNotifier notifier]removeObserver:self];
    [[DMJobManager sharedInstance]onDestroy:self];
    _taskMap = nil;
    _data = nil;
    _taskSet = nil;
}
-(void)setTitle:(NSString *)title{
    [self.navigationItem setTitle:title];
}
-(void)viewDidLoad{
    [super viewDidLoad];
    self.taskMap = [[NSMutableDictionary alloc]init];
    self.taskSet = [[NSMutableSet alloc]init];
    [self createLeftButton];
}

-(void)setResult:(id)data{
    self.completion(data);
}

-(void)createLeftButton{
    NSString* str = [DMConfigReader getString:@"back" ];
    UIImage* img = [UIImage imageNamed:str ];
    UIBarButtonItem *leftBarButtonItem = [DMViewController createImageItem:img target:self action:@selector(finish)];
    leftBarButtonItem.tintColor =   [DMColorUtil colorWithString:[ DMConfigReader getString:@"colors" subkey:@"tint" ]];
    [self.navigationItem setLeftBarButtonItem:leftBarButtonItem];
}
+(UIBarButtonItem*)createImageItem:(UIImage*)image segueName:(NSString*)segueName parent:(UIViewController*)parent{
    DMBarButtonItem* item = [[DMBarButtonItem alloc]initWithImage:image];
    item.segueName =segueName;
    item.parent = parent;
    return item;
}

+(UIBarButtonItem*)createImageItem:(UIImage*)image controllerClass:(Class)controllerClass parent:(UIViewController*)parent{
    DMBarButtonItem* item = [[DMBarButtonItem alloc]initWithImage:image];
    item.controllerClass =controllerClass;
    item.parent = parent;
    return item;
}
+(UIBarButtonItem*)createTextItem:(NSString*)title controllerClass:(Class)controllerClass parent:(UIViewController*)parent{
    DMBarButtonItem* item = [[DMBarButtonItem alloc]initWithText:title];
   item.controllerClass =controllerClass;
    item.parent = parent;
    return item;
}
+(UIBarButtonItem*)createTextItem:(NSString*)title segueName:(NSString*)segueName parent:(UIViewController*)parent{
    DMBarButtonItem* item = [[DMBarButtonItem alloc]initWithText:title];
    item.segueName =segueName;
    item.parent = parent;
    return item;

}

+(UIBarButtonItem*)createImageItem:(UIImage*)image target:(id)target action:(SEL)selector{
    UIBarButtonItem *leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:image style:UIBarButtonItemStyleDone target:target action:selector];
   
    return leftBarButtonItem;
}

+(UIBarButtonItem*)createTextItem:(NSString*)title target:(id)target action:(SEL)selector{
    UIBarButtonItem *leftBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStyleDone target:target action:selector];
     leftBarButtonItem.tintColor =   [DMColorUtil colorWithString:[ DMConfigReader getString:@"colors" subkey:@"tint" ]];
    return leftBarButtonItem;
}
-(void)finish:(BOOL)animated{
    [_controllerDelegate controllerWillFinish:self];
    [self.navigationController popViewControllerAnimated:animated];
}
-(void)finish{
    [_controllerDelegate controllerWillFinish:self];
    [self.navigationController popViewControllerAnimated:YES];
}



-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    //这里
    DMViewController* target = segue.destinationViewController;
    target.data = sender;
    
}

@end
