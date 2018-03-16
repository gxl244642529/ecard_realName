//
//  DMFixTableView.m
//  DMLib
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFixTableView.h"
#import "DMDataAdapter.h"
#import "DMViewUtil.h"
#import "UIView+ViewNamed.h"
#import "DataSetterUtil.h"
#import "DMCellDataSetter.h"

@interface DMFixTableView()
{
    NSMutableArray* _data;
    __weak NSObject<IDataAdapterListener>* _listener;
    DMCellDataSetter* _setter;
    __weak NSLayoutConstraint* _height;
}

@end


@implementation DMFixTableView

-(void)dealloc{
    _setter = nil;
    _data = nil;
}


-(CGFloat)expHeight{
    
    
    return _data.count * _rowHeight;
    
}

-(NSMutableArray*)array{
    return _data;
}
-(void)setListener:(NSObject<IDataAdapterListener>*)listener{
    if(_setter){
        [_setter setListener:listener];
    }else{
        _listener =listener;
    }
    
}
-(void)registerNib:(NSString*)nibName  bundleName:(NSString*)bundleName rowHeight:(CGFloat)rowHeight{
    _bundleName = bundleName;
    _nibName  =nibName;
    _rowHeight = rowHeight;
      _setter = [[DMCellDataSetter alloc]initWithCellName:nibName];
    _listener = _setter;
}

-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        _data = [[NSMutableArray alloc]init];
    }
    return self;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    _data = [[NSMutableArray alloc]init];
    if(_nibName){
        _setter = [[DMCellDataSetter alloc]initWithCellName:_nibName];
        _listener = _setter;
    }
    _height = [self findHeight];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger index = 0;
    for(UIView* view in self.subviews){
        view.frame = CGRectMake(0, _rowHeight * index, self.frame.size.width, _rowHeight);
        ++index;
    }
}
-(id)val{
    return _data;
}
-(void)setVal:(id)val{
    if([val isKindOfClass:[NSMutableArray class]]){
        _data = val;
    }else{
        _data = [[NSMutableArray alloc]initWithArray:val];
    }
    [self notifyDataChanged];
}

-(void)addItem:(id)data{
    [_data addObject:data];
    [self notifyDataChanged];
}


-(void)notifyDataChanged{
   
    NSInteger count = _data.count;
    NSArray* arr = self.subviews;
    NSInteger sCount = arr.count;
    for(NSInteger i = count; i < sCount; ++i){
        [arr[i] removeFromSuperview];
    }
    
    if(_height){
        _height.constant = count * _rowHeight;
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        for(NSInteger i=0; i < count ; ++i){
            UIView* v;
            if(i < sCount){
                v = arr[i];
            }else{
                v = [DMViewUtil createViewFormNibName:_nibName bundle:CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName) owner:self];
                [self addSubview:v];
            }
            [_listener onInitializeView:self cell:v data:_data[i] index:i];
        }

    });
    

}

@end
