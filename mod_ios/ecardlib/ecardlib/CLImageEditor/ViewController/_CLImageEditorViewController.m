//
//  _CLImageEditorViewController.m
//
//  Created by sho yakushiji on 2013/11/05.
//  Copyright (c) 2013年 CALACULU. All rights reserved.
//

#import "_CLImageEditorViewController.h"


#import "CLImageTools.h"
#import "UIView+Frame.h"
#import <DMLib/DMLib.h>
#import "TabItemView.h"
#import "ColorUtil.h"
#import "CLAdjustmentTool.h"
#import "CLRotateTool.h"
#import "TextTool.h"
#import "FilterTool.h"

#import <ecardlib/ecardlib.h>

#define BOTTOM_HEIGHT 58

@interface CLMenuPanel : UIView
{
    
}
@property (nonatomic, strong) Class toolClass;
@property (nonatomic, strong) NSString *title;

@end

@implementation CLMenuPanel

@end






@interface _CLImageEditorViewController ()
@property (nonatomic, strong) CLImageToolBase *currentTool;
@end

@implementation _CLImageEditorViewController
{
    UIImage *_originalImage;
    NSMutableArray* _tools;
    UINavigationBar *_navigationBar;
    
    BOOL _autoEdit;
}
INIT_BUNDLE_CONTROLLER(_CLImageEditorViewController, ecardlibbundle.bundle)
-(void)dealloc{
    self.imageView = nil;
    self.scrollView = nil;
    self.menuView = NULL;
    self.editTitle = nil;
    _originalImage = NULL;
    _navigationBar = NULL;
    _tools = NULL;
}

- (id)initWithImage:(UIImage *)image title:(NSString*)title autoEdit:(BOOL)autoEdit
{
    self = [self init];
    if (self){
        _autoEdit = autoEdit;
        _originalImage = image;//[image deepCopy];
        self.editTitle = title;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizesSubviews = YES;
    
    if([self respondsToSelector:@selector(automaticallyAdjustsScrollViewInsets)]){
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    if([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]){
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }
    
    CGRect frame = [UIScreen mainScreen].bounds;
    _navigationBar = [[UINavigationBar alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, 65)];
    UINavigationItem *titleItem = [[UINavigationItem alloc]init];
    [titleItem setTitle:self.editTitle];
    
    
    UIButton* okButton = [self createTitleTextbutton:@"保存"];
    Control_AddTarget(okButton, pushedFinishBtn);
    UIButton* backButton = [self createBackButton];
    Control_AddTarget(backButton, finish);
    titleItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:backButton];
    titleItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:okButton];
    [_navigationBar pushNavigationItem:titleItem animated:NO];
    
    [self.view addSubview:_navigationBar];
   
    self.imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 200, 200)];
    [self.scrollView addSubview:self.imageView];
    
    [self setMenuView];
    [self refreshImageView:_autoEdit];
    
}


-(UIButton*)createBackButton{
    
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 44,22)];
    [addButton setImage:[UIImage imageNamed:@"ecardlibbundle.bundle/ic_back"] forState:UIControlStateNormal];
    return addButton;
    
}
-(UIButton*)createTitleTextbutton:(NSString*)title{
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 50,22)];
    [addButton setTitle:title forState:UIControlStateNormal];
    [addButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [addButton setTitleColor:[ColorUtil titleTextColor] forState:UIControlStateNormal];
    return addButton;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setMenuView
{
    _tools = [[NSMutableArray alloc]init];
    
    [_tools addObject:[CLRotateTool class ]];
    [_tools addObject:[CLAdjustmentTool class]];
    [_tools addObject:[FilterTool class]];
    [_tools addObject:[TextTool class]];
    
    CGRect frame =[UIScreen mainScreen].bounds;
    _menuView = [[UIView alloc]initWithFrame:CGRectMake(0,frame.origin.y+ frame.size.height-BOTTOM_HEIGHT, frame.size.width, BOTTOM_HEIGHT)];
    [self.view addSubview:_menuView];
    _menuView.backgroundColor = [UIColor colorWithWhite:1 alpha:0.8];
    
    
    TabItemView* item;
    
    item = [self createItem:@"编辑" icon:@"s_ic_edit1" index: 0];
    [_menuView addSubview:item];
    [item setTarget:self withAction:@selector(tappedMenuView:)];
    
    item = [self createItem:@"调整" icon:@"s_ic_edit5" index: 1 ];
    [_menuView addSubview:item];
    [item setTarget:self withAction:@selector(tappedMenuView:)];
    
    item = [self createItem:@"滤镜" icon:@"s_ic_edit2" index: 2];
    [_menuView addSubview:item];
    [item setTarget:self withAction:@selector(tappedMenuView:)];
    
    item = [self createItem:@"文字" icon:@"s_ic_edit4" index: 3];
    [_menuView addSubview:item];
    [item setTarget:self withAction:@selector(tappedMenuView:)];
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

-(void)viewWillDisappear:(BOOL)animated{
    
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

-(TabItemView*)createItem:(NSString*)text icon:(NSString*)icon index:(int)index{
    TabItemView* item = [[TabItemView alloc]initWithFrame:CGRectMake(index*BOTTOM_HEIGHT, 0, BOTTOM_HEIGHT, BOTTOM_HEIGHT)];
    [item setText:text];
    [item setIcon:icon highlighted:icon];
    [item setPadding:3];
    [item setTextColor:[UIColor blackColor] highlighted:[ColorUtil titleColor]];
    item.tag = index;
    return item;
}

- (void)resetImageViewFrame
{
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect rct = _imageView.frame;
        rct.size = CGSizeMake(_scrollView.zoomScale*_imageView.image.size.width, _scrollView.zoomScale*_imageView.image.size.height);
        _imageView.frame = rct;
    });
}

- (void)resetZoomScaleWithAnimate:(BOOL)animated
{
    dispatch_async(dispatch_get_main_queue(), ^{
        CGFloat Rw = _scrollView.frame.size.width/_imageView.image.size.width;
        CGFloat Rh = _scrollView.frame.size.height/_imageView.image.size.height;
        CGFloat ratio = MIN(Rw, Rh);
        
        _scrollView.contentSize = _imageView.frame.size;
        _scrollView.minimumZoomScale = ratio;
        _scrollView.maximumZoomScale = MAX(ratio/240, 1/ratio);
        
        [_scrollView setZoomScale:_scrollView.minimumZoomScale animated:animated];
    });
}

- (void)refreshImageView:(BOOL)startEdit
{
    dispatch_async(dispatch_get_main_queue(), ^{
        _imageView.image = _originalImage;
        
        [self resetImageViewFrame];
        [self resetZoomScaleWithAnimate:NO];
        
        if(startEdit){
            [self performSelector:@selector(onEdit:) withObject:self afterDelay:0.2];
            
        }
        
    });
}

-(void)onEdit:(id)sender{
    [self setupToolWithToolClass:[CLRotateTool class]];
}

- (UIBarPosition)positionForBar:(id <UIBarPositioning>)bar
{
    return UIBarPositionTopAttached;
}

- (BOOL)shouldAutorotate
{
    return NO;
}

- (NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

#pragma mark- Tool actions

- (void)setCurrentTool:(CLImageToolBase *)currentTool
{
    if(currentTool != _currentTool){
        [_currentTool cleanup];
        _currentTool = currentTool;
        [_currentTool setup];
        
        [self swapToolBarWithEditting:(_currentTool!=nil)];
    }
}

#pragma mark- Menu actions

- (void)swapMenuViewWithEditting:(BOOL)editting
{
    [UIView animateWithDuration:kCLImageToolAnimationDuration
                     animations:^{
                         if(editting){
                             _menuView.transform = CGAffineTransformMakeTranslation(0, self.view.height-_menuView.top);
                         }
                         else{
                             _menuView.transform = CGAffineTransformIdentity;
                         }
                     }
     ];
}



- (void)swapToolBarWithEditting:(BOOL)editting
{
    [self swapMenuViewWithEditting:editting];
    
    if(self.currentTool){
        
        UINavigationItem *titleItem = [[UINavigationItem alloc]init];
        [titleItem setTitle:[[self.currentTool class] title]];
        UIButton* okButton = [self createTitleTextbutton:@"确定"];
        UIButton* backButton = [self createTitleTextbutton:@"取消"];
        
        Control_AddTarget(okButton, pushedDoneBtn);
        Control_AddTarget(backButton, pushedCancelBtn);
        
        titleItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:okButton];
        titleItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:backButton];
        [_navigationBar pushNavigationItem:titleItem animated:YES];

    }
    else{
        [_navigationBar popNavigationItemAnimated:(self.navigationController==nil)];
    }
}





-(void)editImage:(UIImage*)image{
    _originalImage = image;
    [self refreshImageView:YES];
}

- (void)tappedMenuView:(TabItemView*)sender
{
    NSInteger index = sender.tag;
    [self setupToolWithToolClass:_tools[index]];
}

- (IBAction)pushedCancelBtn:(id)sender
{
    if([self.currentTool isKindOfClass:[CLRotateTool class]]){
        if(_autoEdit){
            [self finish];
            return;
        }
    }
    _imageView.image = _originalImage;
    [self resetImageViewFrame];
    
    self.currentTool = nil;
}

- (IBAction)pushedDoneBtn:(id)sender
{
    self.view.userInteractionEnabled = NO;
    
    [self.currentTool executeWithCompletionBlock:^(UIImage *image, NSError *error, NSDictionary *userInfo) {
        if(error){
            self.view.userInteractionEnabled = YES;
            return;
        }
        
        if(image){
            _originalImage = image;
            _imageView.image = image;
            [self resetImageViewFrame];
        }
        self.currentTool = nil;
        self.view.userInteractionEnabled = YES;
    }];
}

- (void)setupToolWithToolClass:(Class)toolClass
{
    if(self.currentTool){ return; }
    
    if(toolClass){
        id instance = [toolClass alloc];
        if(instance!=nil && [instance isKindOfClass:[CLImageToolBase class]]){
            instance = [instance initWithImageEditor:self];
            self.currentTool = instance;
        }
    }
}



- (void)pushedFinishBtn:(id)sender
{
    
    if([self.delegate respondsToSelector:@selector(imageEditorWillFinishEditingWidthImage:)]){
        [SVProgressHUD showWithStatus:@"正在保存..."];
        dispatch_async(GLOBAL_QUEUE, ^{
            [self.delegate imageEditorWillFinishEditingWidthImage:_originalImage];
            dispatch_async(dispatch_get_main_queue(), ^{
                [SVProgressHUD dismiss];
                if([self.delegate respondsToSelector:@selector(imageEditorDidFinishEdittingWithImage:)]){
                    [self.delegate imageEditorDidFinishEdittingWithImage:_originalImage];
                }
                [self.navigationController popViewControllerAnimated:YES];
            });
        });
        
    }else{
        if([self.delegate respondsToSelector:@selector(imageEditorDidFinishEdittingWithImage:)]){
            [self.delegate imageEditorDidFinishEdittingWithImage:_originalImage];
        }
        [self.navigationController popViewControllerAnimated:YES];
    }
    
    
    
}

#pragma mark- ScrollView delegate

- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
{
    return _imageView;
}

- (void)scrollViewDidZoom:(UIScrollView *)scrollView
{
    CGFloat Ws = _scrollView.frame.size.width;
    CGFloat Hs = _scrollView.frame.size.height - _scrollView.contentInset.top - _scrollView.contentInset.bottom;
    CGFloat W = _originalImage.size.width*_scrollView.zoomScale;
    CGFloat H = _originalImage.size.height*_scrollView.zoomScale;
    
    CGRect rct = _imageView.frame;
    rct.origin.x = MAX((Ws-W)/2, 0);
    rct.origin.y = MAX((Hs-H)/2, 0);
    _imageView.frame = rct;
}

@end
