//
//  ScrollableAdvView.m
//  eCard
//
//  Created by randy ren on 14-3-1.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DMAdvView.h"
#import "DMNetworkImage.h"
#import "DMJobManager.h"
#import "DMServers.h"
#import "DMWebViewController.h"
#import "DMMacro.h"
#import "DMAdvVo.h"
#import "CommonUtil.h"


//////////////////
////////////////
////////////////

@interface DMAdvView()
{
    NSTimer* _timer;
    NSInteger _beginX;
    NSInteger _currentPos;
    NSInteger _totalPages;
    NSMutableArray* _views;
    BOOL _isStarted;
    
    UIScrollView* _scrollView;
    UIImageView* _imageView;
    
    DMApiJob* task;
    id _parser;
    DMNetworkImage* _singleView;
}
@end

@implementation DMAdvView


-(void)dealloc{
    _parser = nil;
    [self removeTimer];
    self.defaultImage = nil;
    _singleView = nil;
    _scrollView = nil;
    _imageView = nil;
    _data = NULL;
    _views = nil;
    [task cancel];
    task = nil;
}
/*
-(void)setDataProviderName:(NSString *)dataProviderName{
    Class clazz = NSClassFromString(dataProviderName);
    _dataProviderName = dataProviderName;
    [self setDataProvider:[[clazz alloc]init]];
}*/

-(void)awakeFromNib{
    [super awakeFromNib];
    [self createSelf];
    //如果有默认图片的画
    //默认图片id为1
    _imageView = (UIImageView*)[self viewWithTag:1];
    if(_defaultImage){
        if(!_imageView){
            _imageView = [[UIImageView alloc]initWithFrame:self.bounds];
            [self addSubview:_imageView];
        }
        _imageView.image = _defaultImage;
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        //开始加载数据
        task = [[DMJobManager sharedInstance]createArrayJsonTask:@"adv/list" cachePolicy:DMCachePolicy_NoCache server:1];
        task.clazz = [DMAdvVo class];
        [task put:@"id" value:_module];
        [task setDelegate:self];
        [task execute];
    });
    
    

    
}
-(void)initView:(DMNetworkImage*)image data:(DMAdvVo*)data index : (NSInteger) index{
    //这里
    if(data.imgBig!=nil && [CommonUtil isLongScreen]){
         [image load:data.imgBig];
    }else{
         [image load:data.img];
    }
  
    
    if([_dataAdapterListener respondsToSelector:@selector(onInitializeView:cell:data:index:)]){
        [_dataAdapterListener onInitializeView:self cell:image data:data index:index];
    }
    
}

-(void)onTapView:(UITapGestureRecognizer*)ges{
    
    UIView* view =ges.view;
    NSInteger index = [_views indexOfObject:view] - 1;
    
    
    NSInteger realIndex = (_currentPos + index) % _totalPages;
    if(realIndex < 0 )realIndex = _totalPages + realIndex;
    
    
    [self onClickView:_data[realIndex]];
}

-(void)onClickView:(DMAdvVo*)data {
    if(_advDelegate){
        [_advDelegate adv:self didClick:data];
    }else{
        //查看
        
        if(!data.enable){
            return;
        }
       
        DMWebViewController* web = [[DMWebViewController alloc]initWithTitle:data.title url:data.url];
        if(_urlParser){
            if(!_parser){
                Class clazz = NSClassFromString(_urlParser);
                _parser =[[clazz alloc]init];
            }
            web.delegate = _parser;
        }
        [[DMJobManager sharedInstance].topController.navigationController pushViewController:web animated:YES];
    }
    

}

-(void)onTapSingleView:(UITapGestureRecognizer*)ges{
    [self onClickView:_data[0]];

}
-(void)jobSuccess:(DMApiJob*)request{
    
    [self setData:request.data];
    
}



-(void)createSelf{
    _views = [[NSMutableArray alloc]init];
       [self setBackgroundColor:[[UIColor lightGrayColor]colorWithAlphaComponent:0.3]];
    self.userInteractionEnabled = NO;
    
}




-(void)onError:(NSString *)error{
    
}

-(void)onReceiveData:(id)data{
    [self setData:data];
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
        
        if(!_scrollView){
            _scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, self.frame.size.height)];
            [self addSubview:_scrollView];
            _scrollView.pagingEnabled = YES;
            _scrollView.contentSize = CGSizeMake(SCREEN_WIDTH*3, 0);
            _scrollView.contentOffset = CGPointMake(SCREEN_WIDTH, 0);
            _scrollView.showsHorizontalScrollIndicator = NO;
            _scrollView.delegate = self;

        }
        
        [_views makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [_views removeAllObjects];

        
        for (int i=-1; i <=1; ++i) {
            
            NSInteger realIndex = (_currentPos + i) % _totalPages;
            if(realIndex < 0 )realIndex = _totalPages + realIndex;
            
            DMNetworkImage* imageView = [[DMNetworkImage alloc]initWithFrame:CGRectMake((i+1) * SCREEN_WIDTH, 0, SCREEN_WIDTH, self.frame.size.height)];
            UITapGestureRecognizer* ges = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTapView:)];
            imageView.userInteractionEnabled = YES;
            [imageView addGestureRecognizer:ges];
            [_views addObject:imageView];
            [_scrollView addSubview:imageView];
            [self initView:imageView data:data[realIndex] index:realIndex];
        }
        self.userInteractionEnabled = YES;

    }else if(data.count == 1){
        
        _totalPages = data.count;
        _data = data;
        [_views makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [_views removeAllObjects];
        
        
        _singleView =[[DMNetworkImage alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, self.frame.size.height)];
        [self addSubview:_singleView];
        UITapGestureRecognizer* ges = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTapSingleView:)];
        _singleView.userInteractionEnabled = YES;
        [_singleView addGestureRecognizer:ges];
        [_views addObject:_singleView];
        _currentPos=0;
        
        [self initView:_singleView data:data[0] index:0];
       
        
        
        self.userInteractionEnabled = YES;
    }
}



-(void)layoutSubviews{
    [super layoutSubviews];
  
}



- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       
        [self createSelf];
    }
    return self;
}





-(void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    [self addTimer];
}

-(void)nextImage{
    _beginX =_scrollView.contentOffset.x;
    [_scrollView setContentOffset:CGPointMake(SCREEN_WIDTH * 2, 0) animated:YES];
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
        
        DMNetworkImage* tmp ;
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
        [self initView:tmp data:_data[index] index:index];
    }
    
    _scrollView.contentOffset =CGPointMake(SCREEN_WIDTH, 0);
    
    
    
    if(sync){
        _beginX = currentX;
    }
    
    //设置位置
    [self setViewPos];
    
}
/*
-(void)loadImage:(UINetworkImage*)image data:(NSObject<IImageVo>*)value{
    
    if([ViewUtil isLongScreen]){
        
        [image load:value.imgLong];
        
    }else{
        [image load:value.img];
    }
}*/

-(void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView{
    [self handlerScroll:NO];
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    
    [self handlerScroll:YES];
}



-(void)setViewPos{
    int i=0;
    for(UIView* view in _views){
         view.frame = CGRectMake(i*SCREEN_WIDTH, 0, SCREEN_WIDTH, view.frame.size.height);
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
