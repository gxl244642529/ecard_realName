//
//  ScrollableAdvView.m
//  eCard
//
//  Created by randy ren on 14-3-1.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ScrollableAdvView.h"
#import <ecardlib/ecardlib.h>

#import "IImageVo.h"
#import "JsonTaskManager.h"
#import "NetworkImage.h"



//////////////////
////////////////
////////////////

@interface ScrollableAdvView()
{
    NSTimer* _timer;
    NSInteger _beginX;
    NSInteger _currentPos;
    NSInteger _totalPages;
    NSMutableArray* _views;
    BOOL _isStarted;
    
    UIScrollView* _scrollView;
    UIImageView* _imageView;
    

    
    NetworkImage* _singleView;
    id<IDataProvider> _dataProvider;
}
@end

@implementation ScrollableAdvView
/*
-(void)setDataProviderName:(NSString *)dataProviderName{
    Class clazz = NSClassFromString(dataProviderName);
    _dataProviderName = dataProviderName;
    [self setDataProvider:[[clazz alloc]init]];
}*/



-(void)dealloc{
    [self removeTimer];
    _singleView = nil;
    _scrollView = nil;
    _imageView = nil;
    _data = NULL;
    _views = nil;
    _dataProvider = nil;
   // self.dataProviderName = nil;
}


-(void)onError:(NSString *)error{
    
}

-(void)onReceiveData:(id)data{
    [self setData:data];
}


-(void)setDataProvider:(id<IDataProvider>)dataProvider{
    _dataProvider = dataProvider;
    [dataProvider setDataProviderListener:self];
    if([dataProvider conformsToProtocol:@protocol(IDataAdapterListener)]){
        id<IDataAdapterListener> lister = (id<IDataAdapterListener>)dataProvider;
        [self setDataListener:lister];
    }
    if([dataProvider conformsToProtocol:@protocol(ScrollableAdvViewDelegate)]){
        id<ScrollableAdvViewDelegate> listener = (id<ScrollableAdvViewDelegate>)dataProvider;
        self.advDelegate = listener;
    }
    [dataProvider load];
}
-(void)setData:(NSArray*)data{
    
    if(_imageView){
        _imageView.hidden = YES;
    }
    
    if(data.count >= 2){
        _data = data;
        if(data.count!=_totalPages){
            _totalPages = data.count;
            _currentPos = 0;
        }
        if(_isStarted){
            [self addTimer];
        }
        //  _currentPos = 0;
        // yichu
        
        
        [_views makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [_views removeAllObjects];

        
        for (int i=-1; i <=1; ++i) {
            
            NSInteger realIndex = (_currentPos + i) % _totalPages;
            if(realIndex < 0 )realIndex = _totalPages + realIndex;
            
            NetworkImage* imageView = [[NetworkImage alloc]initWithFrame:CGRectMake((i+1) * self.frame.size.width, 0, self.frame.size.width, self.frame.size.height)];
            UITapGestureRecognizer* ges = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTapView:)];
            imageView.userInteractionEnabled = YES;
            [imageView addGestureRecognizer:ges];
            [_views addObject:imageView];
            [_scrollView addSubview:imageView];
            
            [_dataListener onInitializeView:self cell:imageView data:data[realIndex] index:realIndex];
            
        }
        
        
        self.userInteractionEnabled = YES;

    }else if(data.count == 1){
        
        _totalPages = data.count;
        _data = data;
        [_views makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [_views removeAllObjects];
        
        
        _singleView =[[NetworkImage alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.width, self.frame.size.height)];
        [self addSubview:_singleView];
        UITapGestureRecognizer* ges = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTapSingleView:)];
        _singleView.userInteractionEnabled = YES;
        [_singleView addGestureRecognizer:ges];
        [_views addObject:_singleView];
        _currentPos=0;
        [_dataListener onInitializeView:self cell:_singleView data:data[0] index:0];
        
        
        self.userInteractionEnabled = YES;
    }
    

    
}

-(void)onTapSingleView:(UITapGestureRecognizer*)ges{
    if(_data && _data.count==1){
        if(self.advDelegate){
            [self.advDelegate adv:self didClick:_data[0]];
        }
    }
    
}

-(void)onTapView:(UITapGestureRecognizer*)ges{
    
    UIView* view =ges.view;
    NSInteger index = [_views indexOfObject:view] - 1;
    
    
    NSInteger realIndex = (_currentPos + index) % _totalPages;
    if(realIndex < 0 )realIndex = _totalPages + realIndex;
    
    
    
    if(self.advDelegate){
        [self.advDelegate adv:self didClick:_data[realIndex]];
    }
    
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       
        [self createSelf];
    }
    return self;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    [self createSelf];
    //如果有默认图片的画
    //默认图片id为1
    _imageView = (UIImageView*)[self viewWithTag:1];
}

-(void)createSelf{
     _views = [[NSMutableArray alloc]init];
    _scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0,0, self.frame.size.width, self.frame.size.height)];
    [self addSubview:_scrollView];
    _scrollView.pagingEnabled = YES;
    _scrollView.contentSize = CGSizeMake(self.frame.size.width*3, 0);
    _scrollView.contentOffset = CGPointMake(self.frame.size.width, 0);
    _scrollView.showsHorizontalScrollIndicator = NO;
    _scrollView.delegate = self;
    [self setBackgroundColor:[[UIColor lightGrayColor]colorWithAlphaComponent:0.3]];
    self.userInteractionEnabled = NO;

}


-(void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    [self addTimer];
}

-(void)nextImage{
    _beginX =_scrollView.contentOffset.x;
    [_scrollView setContentOffset:CGPointMake(self.frame.size.width * 2, 0) animated:YES];
}

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
    _beginX = _scrollView.contentOffset.x;
    [self removeTimer];
}

-(void)removeTimer{
    if(_timer){
        [_timer invalidate];
        _timer = nil;
    }
    
}

-(void)handlerScroll:(BOOL)sync{
    int currentX = (int)_scrollView.contentOffset.x;
    if(currentX == _beginX){
        return;
    }
    NSInteger initIndex;
    if(_totalPages>=2){
        
        NetworkImage* tmp ;
        //向右
        if(currentX > _beginX){
            //如果是向右边划，将view的位置向前调整
            
            tmp = _views[0];
            _views[0] = _views[1];
            _views[1] = _views[2];
            _views[2] = tmp;
            
            _currentPos++;
            
            initIndex = _currentPos + 1;
            
        }else{
            //向后调整
            tmp = _views[2];
            _views[2] = _views[1];
            _views[1] = _views[0];
            _views[0] = tmp;
            
            _currentPos--;
            
            initIndex = _currentPos  -1;
        }

        NSInteger index = initIndex % _totalPages;
        if(index < 0 )index = _totalPages + index;
        [_dataListener onInitializeView:self cell:tmp data:_data[index] index:index];
    }
    
    _scrollView.contentOffset =CGPointMake(self.frame.size.width, 0);
    
    
    
    if(sync){
        _beginX = currentX;
    }
    
    //设置位置
    [self setViewPos];
    
}

-(void)loadImage:(NetworkImage*)image data:(NSObject<IImageVo>*)value{
    
    if([ViewUtil isLongScreen]){
        
        [image load:value.imgLong];
        
    }else{
        [image load:value.img];
    }

}

-(void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView{
    [self handlerScroll:NO];
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    
    [self handlerScroll:YES];
}

-(void)setViewPos{
    int i=0;
    for(UIView* view in _views){
         view.frame = CGRectMake(i*self.frame.size.width, 0, view.frame.size.width, view.frame.size.height);
        ++i;
    }
}



- (NSInteger)validPageValue:(NSInteger)value {
    if(value < 0) value = _totalPages - 1;
    if(value >= _totalPages) value = 0;
    
    return value;
    
}


-(void)addTimer{
    if(!_timer){
         _timer = [NSTimer scheduledTimerWithTimeInterval:8 target:self selector:@selector(nextImage) userInfo:nil repeats:YES];
    }
}

-(void)start
{
    _isStarted = YES;
    if(_data){
        [self addTimer];
    }
   
}
-(void)stop
{
    _isStarted = NO;
    [self removeTimer];
}


@end
