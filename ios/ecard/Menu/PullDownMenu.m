//
//  PullDownMenu.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "PullDownMenu.h"
#import "TreeMenuView.h"
#import <ecardlib/ecardlib.h>
#import "BaseMacro.h"

////////////////////////////////////////////////////////



@implementation MenuGroupData

-(void)dealloc{
    self.menuDatas = NULL;
}

@end
@interface NSString(extended)
-(NSMutableAttributedString *)attributedStringFromStingWithFont:(UIFont *)font
                                                withLineSpacing:(CGFloat)lineSpacing;

@end
@implementation NSString(extended)
-(NSMutableAttributedString *)attributedStringFromStingWithFont:(UIFont *)font
                                                withLineSpacing:(CGFloat)lineSpacing
{
    NSMutableAttributedString *attributedStr = [[NSMutableAttributedString alloc] initWithString:self attributes:@{NSFontAttributeName:font}];
    
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineSpacing:lineSpacing];
    
    [attributedStr addAttribute:NSParagraphStyleAttributeName
                          value:paragraphStyle
                          range:NSMakeRange(0, [self length])];
    return attributedStr;
}

@end
//////////////////////////////////////////////////////////////////////
#define SELF_WIDTH 320


@interface PullDownMenu()
{
    UIColor *_menuColor;
    
    __weak UIView* _parent;
    __weak NSObject<MenuViewDelegate>* _delegate;
    
    
    TouchableView *_backGroundView;
    
    NSMutableArray *_titles;
    NSMutableArray *_indicators;
    
    NSInteger _currentSelectedMenudIndex;
    bool _show;
    NSInteger _numOfMenu;
    UIColor* _highlightColor;
    
    UIView* _headView;
    
    UIView* _menuView;
    
    CGRect _frame;
    CGFloat _menuHeight;
    
    //数据
    NSMutableArray* _arr;
    
}

@end

@implementation PullDownMenu

-(void)dealloc{
    _menuView = NULL;
    _headView = NULL;
    _arr =NULL;
    
    _highlightColor = NULL;
    
    _indicators = NULL;
    _titles = NULL;
    
    _backGroundView = NULL;
    
}


-(void)setData:(NSInteger)index data:(NSArray*)data type:(PullDownType)type{
    MenuGroupData* groupData = _arr[index];
    groupData.menuDatas = [[NSMutableArray alloc]initWithArray: data];
    groupData.type = type;
    [self setCurrentMenu:groupData.menuDatas[0] index:index];
}

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent  count:(int)count{
    if(self = [super init]){
        
        _arr = [[NSMutableArray alloc]initWithCapacity:count];
        for(int i=0; i < count; ++i){
            MenuGroupData* groupData = [[MenuGroupData alloc]init];
            [_arr addObject:groupData];
        }
        
    
        
        _frame = frame;
        _menuHeight = frame.size.height * 0.8;
        _parent = parent;
        
        _headView = [[UIView alloc]initWithFrame:CGRectMake(0, frame.origin.y, 320, 36)];
        [_parent addSubview:_headView];
        [_headView setBackgroundColor:[UIColor redColor]];
        
        _menuColor = [UIColor colorWithRed:164.0/255.0 green:166.0/255.0 blue:169.0/255.0 alpha:1.0];
        
         _numOfMenu = count;
        _highlightColor = [UIColor redColor];
        
        
        CGFloat textLayerInterval = SELF_WIDTH / ( _numOfMenu * 2);
        CGFloat separatorLineInterval = SELF_WIDTH / _numOfMenu;
        
        
        _titles = [[NSMutableArray alloc] initWithCapacity:_numOfMenu];
        _indicators = [[NSMutableArray alloc] initWithCapacity:_numOfMenu];
        
        
        for (int i = 0; i < _numOfMenu; i++) {
            
            CGPoint position = CGPointMake( (i * 2 + 1) * textLayerInterval , SELF_HEIGHT / 2);
            CATextLayer *title = [self creatTextLayerWithNSString:@"" withColor:_menuColor andPosition:position];
            [_headView.layer addSublayer:title];
            [_titles addObject:title];
            
            
            CAShapeLayer *indicator = [self creatIndicatorWithColor:_menuColor andPosition:CGPointMake(position.x + title.bounds.size.width / 2 + 8, SELF_HEIGHT / 2)];
            [_headView.layer addSublayer:indicator];
            [_indicators addObject:indicator];
            
            if (i != _numOfMenu - 1) {
                CGPoint separatorPosition = CGPointMake((i + 1) * separatorLineInterval, SELF_HEIGHT / 2);
                CAShapeLayer *separator = [self creatSeparatorLineWithColor:[UIColor colorWithRed:239.0/255.0 green:239.0/255.0 blue:243.0/255.0 alpha:1.0] andPosition:separatorPosition];
                [_headView.layer addSublayer:separator];
            }
            
        }
        
        
        CAShapeLayer *separator = [self createBottomLine:[UIColor colorWithWhite:(float)0xe1/255 alpha:1] andPosition:CGPointMake(SELF_WIDTH/2, SELF_HEIGHT)];
        [_headView.layer addSublayer:separator];
        // 设置menu, 并添加手势
        _headView.backgroundColor = [UIColor whiteColor];
        UIGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapMenu:)];
        [_headView addGestureRecognizer:tapGesture];
        
        
        // 创建背景
        _backGroundView = [[TouchableView alloc] initWithFrame:CGRectMake(0, _frame.origin.y + _headView.frame.size.height + 1, _frame.size.width, _frame.size.height - _headView.frame.size.height - 1)];
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.0];
        _backGroundView.opaque = NO;
        [_backGroundView setTarget:self withAction:@selector(tapBackGround:)];
        
        _currentSelectedMenudIndex = -1;
        _show = NO;

    }
    return  self;
}


// 处理菜单点击事件.
- (void)tapMenu:(UITapGestureRecognizer *)paramSender
{
    CGPoint touchPoint = [paramSender locationInView:_headView];
    // 得到tapIndex
    NSInteger tapIndex = touchPoint.x / (SELF_WIDTH / _numOfMenu);
    
    
    if (tapIndex == _currentSelectedMenudIndex && _show) {
        [self highlightTitle:tapIndex highlight:NO];
        _show = NO;
        _currentSelectedMenudIndex = -1;
        [self hideBackground];
        //隐藏上次的数据窗口
        [self removeMenuView];
    } else {
        [self highlightTitle:tapIndex highlight:YES];
        
        if(_currentSelectedMenudIndex>=0){
            [self highlightTitle:_currentSelectedMenudIndex highlight:NO];
            //隐藏上次的数据窗口
            [self removeMenuView];
        }else{
            [self showBackground];
            
        }
        
        MenuGroupData* data = _arr[tapIndex];
        PullDownType type = data.type;
        CGRect rect =CGRectMake(0, _frame.origin.y + 37, 320, 320);
        MenuView* menuView ;
        if(type==PullDownType_Normal){
            menuView = [[MenuView alloc]initWithFrame:rect];
        }else{
            menuView = [[TreeMenuView alloc]initWithFrame:rect];
        }
        [menuView setDelegate:self];
        [menuView setData:data.menuDatas];
        [_parent addSubview:menuView];
        _menuView = menuView;

        _show = YES;
        _currentSelectedMenudIndex = tapIndex;
    }
    
}

-(void)setCurrentMenu:(MenuData*)menuData index:(NSInteger)index{
    
    CATextLayer *title = (CATextLayer *)_titles[index];
    
    NSString* string = menuData.realTitle!=NULL ?  menuData.realTitle : menuData.title;
    CGSize size = [self calculateTitleSizeWithString:string];
    
    CAShapeLayer *indicator = (CAShapeLayer *)_indicators[index];
    indicator.position = CGPointMake(title.position.x + size.width / 2 + 8, indicator.position.y);
    
    [self setTextForLayout:title string:string];
    
}


- (void)confiMenuWithSelectRow:(MenuData *)menuData
{
    
    CATextLayer *title = (CATextLayer *)_titles[_currentSelectedMenudIndex];
    title.string = menuData.realTitle!=NULL ?  menuData.realTitle : menuData.title;
    CGSize size = [self calculateTitleSizeWithString:title.string];
    
    CAShapeLayer *indicator = (CAShapeLayer *)_indicators[_currentSelectedMenudIndex];
    indicator.position = CGPointMake(title.position.x + size.width / 2 + 8, indicator.position.y);
}

-(void)onClickMenu:(MenuData *)menuData index:(NSInteger)index{
    [self confiMenuWithSelectRow:menuData];
    [_delegate onClickMenu:menuData index:_currentSelectedMenudIndex];
    [self tapBackGround:NULL];
}

-(void)removeMenuView{
    //[_menuView removeFromSuperview];
    //_menuView = NULL;
    __weak UIView* view = _menuView;
    
    [UIView animateWithDuration:0.2 animations:^{
        view.frame = CGRectMake(0, _frame.origin.y + SELF_HEIGHT+1, SELF_WIDTH, 0);
    } completion:^(BOOL finished) {
        [view removeFromSuperview];
        if(view==_menuView){
            _menuView = NULL;
        }
    }];

}
-(void)setDelegate:(NSObject<MenuViewDelegate>*)delegate{
    _delegate = delegate;
}
- (void)tapBackGround:(id)sender
{
    
    [self highlightTitle:_currentSelectedMenudIndex highlight:NO];
    [self hideBackground];
    [self removeMenuView];
    _show =NO;
    _currentSelectedMenudIndex = -1;
}


#pragma animation


- (void)animateIndicator:(CAShapeLayer *)indicator Forward:(BOOL)forward
{
    [CATransaction begin];
    [CATransaction setAnimationDuration:0.25];
    [CATransaction setAnimationTimingFunction:[CAMediaTimingFunction functionWithControlPoints:0.4 :0.0 :0.2 :1.0]];
    
    CAKeyframeAnimation *anim = [CAKeyframeAnimation animationWithKeyPath:@"transform.rotation"];
    anim.values = forward ? @[ @0, @(M_PI) ] : @[ @(M_PI), @0 ];
    
    if (!anim.removedOnCompletion) {
        [indicator addAnimation:anim forKey:anim.keyPath];
    } else {
        [indicator addAnimation:anim andValue:anim.values.lastObject forKeyPath:anim.keyPath];
    }
    
    [CATransaction commit];
    indicator.fillColor = forward ? _highlightColor.CGColor : _menuColor.CGColor;
  
}
-(void)showBackground{
    [_parent addSubview:_backGroundView];
    [UIView animateWithDuration:0.2 animations:^{
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.3];
    }];
}

-(void)hideBackground{
    [UIView animateWithDuration:0.2 animations:^{
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.0];
    } completion:^(BOOL finished) {
        [_backGroundView removeFromSuperview];
    }];
}

-(void)highlightTitle:(NSInteger)index highlight:(BOOL)highlight{
   
    
    [self animateTitle:_titles[index] show:highlight];
    [self animateIndicator: _indicators[index] Forward:highlight];
}




- (void)animateTitle:(CATextLayer *)title show:(BOOL)show
{
    if (show) {
        title.foregroundColor = _highlightColor.CGColor;
    } else {
        title.foregroundColor = _menuColor.CGColor;
    }
    CGSize size = [self calculateTitleSizeWithString:title.string];
    CGFloat sizeWidth = (size.width < (SELF_WIDTH / _numOfMenu) - 25) ? size.width : SELF_WIDTH / _numOfMenu - 25;
    title.bounds = CGRectMake(0, 0, sizeWidth, size.height);
    
}




#pragma mark - drawing
- (CAShapeLayer *)creatSeparatorLineWithColor:(UIColor *)color andPosition:(CGPoint)point
{
    CAShapeLayer *layer = [CAShapeLayer new];
    
    UIBezierPath *path = [UIBezierPath new];
    [path moveToPoint:CGPointMake(160,0)];
    [path addLineToPoint:CGPointMake(160, 20)];
    
    layer.path = path.CGPath;
    layer.lineWidth = 1.0;
    layer.strokeColor = color.CGColor;
    
    CGPathRef bound = CGPathCreateCopyByStrokingPath(layer.path, nil, layer.lineWidth, kCGLineCapButt, kCGLineJoinMiter, layer.miterLimit);
    layer.bounds = CGPathGetBoundingBox(bound);
    
    layer.position = point;
    
    return layer;
}

-(CAShapeLayer*)createBottomLine:(UIColor *)color andPosition:(CGPoint)point{
    CAShapeLayer *layer = [CAShapeLayer new];
    
    UIBezierPath *path = [UIBezierPath new];
    [path moveToPoint:CGPointMake(0,0)];
    [path addLineToPoint:CGPointMake(320, 0)];
    
    layer.path = path.CGPath;
    layer.lineWidth = 1;
    layer.strokeColor = color.CGColor;
    
    CGPathRef bound = CGPathCreateCopyByStrokingPath(layer.path, nil, layer.lineWidth, kCGLineCapButt, kCGLineJoinMiter, layer.miterLimit);
    layer.bounds = CGPathGetBoundingBox(bound);
    
    layer.position = point;
    
    return layer;

}

- (CAShapeLayer *)creatIndicatorWithColor:(UIColor *)color andPosition:(CGPoint)point
{
    CAShapeLayer *layer = [CAShapeLayer new];
    
    UIBezierPath *path = [UIBezierPath new];
    [path moveToPoint:CGPointMake(0, 0)];
    [path addLineToPoint:CGPointMake(8, 0)];
    [path addLineToPoint:CGPointMake(4, 5)];
    [path closePath];
    
    layer.path = path.CGPath;
    layer.lineWidth = 1.0;
    layer.fillColor = color.CGColor;
    
    CGPathRef bound = CGPathCreateCopyByStrokingPath(layer.path, nil, layer.lineWidth, kCGLineCapButt, kCGLineJoinMiter, layer.miterLimit);
    layer.bounds = CGPathGetBoundingBox(bound);
    
    layer.position = point;
    
    return layer;
}

-(void)setTextForLayout:(CATextLayer*)layer string:(NSString*)string{
    CGSize size = [self calculateTitleSizeWithString:string];
    CGFloat sizeWidth = (size.width < (SELF_WIDTH / _numOfMenu) - 25) ? size.width : SELF_WIDTH / _numOfMenu - 25;
    layer.bounds = CGRectMake(0, 0, sizeWidth, size.height);
    layer.string = string;
    
}

- (CATextLayer *)creatTextLayerWithNSString:(NSString *)string withColor:(UIColor *)color andPosition:(CGPoint)point
{
    
    CGSize size = [self calculateTitleSizeWithString:string];
    
    CATextLayer *layer = [CATextLayer new];
    CGFloat sizeWidth = (size.width < (SELF_WIDTH / _numOfMenu) - 25) ? size.width : SELF_WIDTH / _numOfMenu - 25;
    layer.bounds = CGRectMake(0, 0, sizeWidth, size.height);
    layer.string = string;
    layer.fontSize = 13.0;
    layer.alignmentMode = kCAAlignmentCenter;
    layer.foregroundColor = color.CGColor;
    
    layer.contentsScale = [[UIScreen mainScreen] scale];
    
    layer.position = point;
    

    
    return layer;
}





- (CGSize)calculateTitleSizeWithString:(NSString *)string
{
    CGFloat fontSize = 13.0;
    UIFont* font = [UIFont systemFontOfSize:fontSize];
   
    if(IOS7){
        NSDictionary *dic = @{NSFontAttributeName: font};
        CGSize size = [string boundingRectWithSize:CGSizeMake(280, 0) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:dic context:nil].size;
        return size;

    }else{
        NSAttributedString* attr = [string attributedStringFromStingWithFont:font withLineSpacing:0];
        return [attr boundingRectWithSize:CGSizeMake(280, 0) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading  context:NULL].size;
    }
  
    
    
   }

@end



#pragma mark - CALayer Category

@implementation CALayer (MXAddAnimationAndValue)

- (void)addAnimation:(CAAnimation *)anim andValue:(NSValue *)value forKeyPath:(NSString *)keyPath
{
    [self addAnimation:anim forKey:keyPath];
    [self setValue:value forKeyPath:keyPath];
}


@end
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

