//
//  SafeContactListView.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeContactListView.h"
#import "InsuredContactView.h"
#import "SafeContact.h"

@interface SafeContactListView ()
{
    
    DMApiJob* _task;
    InsuredContactView* _contactView;
    NSLayoutConstraint* _containerHeight;
    DMFixTableView* _tableView;
    
    NSMutableArray* _insuredList;
    
    UIButton* _addButton;
    
}

@end

@implementation SafeContactListView
-(void)dealloc{
    _insuredList = nil;
    _contactView = nil;
    _containerHeight = nil;
    _task = nil;
    _tableView = nil;
    _addButton = nil;
    [Alert cancelWait];
}

-(void)awakeFromNib{
    [super awakeFromNib];
    _containerHeight = [self findHeight];
}
-(void)onSelect:(UIButton*)button{
    
}

-(void)hide{
    _containerHeight.constant = 1;
    [_contactView removeFromSuperview];
    _contactView = nil;
    
    [_tableView removeFromSuperview];
    _tableView=nil;
    
    [_addButton removeFromSuperview];
    _addButton = nil;
}

-(void)jobSuccess:(DMApiJob*)request{
    [_contactView setData:request.data];
    
}

-(void)layoutSubviews{
    [super layoutSubviews];
    
    if(_tableView){
        _tableView.frame = CGRectMake(0, _contactView.frame.size.height, self.frame.size.width, 130 * _insuredList.count);
        _containerHeight.constant = _contactView.frame.size.height+_tableView.frame.size.height;
    }
    
    if(_addButton){
        _addButton.frame = CGRectMake(0, _containerHeight.constant,  _addButton.frame.size.width, 30);
        _containerHeight.constant = _contactView.frame.size.height+_tableView.frame.size.height + 30;
    }
   
}

-(void)tab:(id)tab didSelectIndex:(NSInteger)index{
    NSArray* arr = _task.data;
    SafeContact* data = arr[index];
    //如果arr(任务列表)的元素在data _insuredList中存在,则替换掉,如果不存在,则替换掉当前新建的第一个
    if([_insuredList containsObject:data]){
        //无需处理
        
    }else{
        //替换第一个
        index = 0;
        BOOL replace = NO;
        for (SafeContact* vo in _insuredList) {
            if(vo.id == 0){
                //替换这个
                _insuredList[index] = data;
                replace = YES;
                break;
            }
            ++index;
        }
        if(!replace){
            //如果是多个,并且小于最大值,则增加一个
            
            if(_maxInsured > 1 && _insuredList.count < _maxInsured){
                //增加一个
                [_insuredList addObject:data];
            }else{
                //如果没有这个,替换第一个
                _insuredList[0] = data;
            }
            
        }
        
       [_tableView setVal:_insuredList];
        [_tableView setFrame:CGRectMake(_tableView.frame.origin.x,_tableView.frame.origin.y, _tableView.frame.size.width, _insuredList.count * 130)];
        [self setNeedsLayout];
    }
   
}

-(void)show{
    if(!_task){
        _task = [[DMJobManager sharedInstance]createArrayJsonTask:@"i_contact/list" cachePolicy:DMCachePolicy_NoCache server:1];
        _task.clazz = [SafeContact class];
        _task.delegate=self;
        [_task execute];
    }
    
    
    _contactView = [[InsuredContactView alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.width, 20)];
    _contactView.tabDelegate = self;
    [self addSubview:_contactView];
    
    //这里增加一个
    _tableView = [[DMFixTableView alloc]initWithFrame:CGRectMake(0, 20, self.frame.size.width, 130)];
    [self addSubview:_tableView];
    [_tableView registerNib:@"SafeDetailInfoView" bundleName:@"safebundle.bundle" rowHeight:130];
    
    
    //这里使用
    if(_maxInsured>1){
        _addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        [_addButton setTitle:@"增加被投保人" forState:UIControlStateNormal];
        [_addButton setImage:[UIImage imageNamed:@"ic_add_insured.png"] forState:UIControlStateNormal];
        [_addButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
        [_addButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        [_addButton sizeToFit];
        [self addSubview:_addButton];
    }
    
    
    //这里直接设置一个
    if(!_insuredList){
        _insuredList = [[NSMutableArray alloc]init];
        [_insuredList addObject:[[SafeContact alloc]init]];
    }
    [_tableView setVal:_insuredList];
    
    if(_task.data){
        [_contactView setData:_task.data];
    }else{
        //如果只是单个的
        _containerHeight.constant = 20;
    }
    
    

}

@end
