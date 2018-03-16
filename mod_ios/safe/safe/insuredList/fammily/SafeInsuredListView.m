//
//  SafeInsuredListView.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredListView.h"
#import "SafeInsuredContainer.h"
#import "SafeContact.h"
#import "SafeInsuredCell.h"

@interface SafeInsuredListView()
{
    DMCheck* _check;
    UIButton* _addInsured;
    DMFixTableView* _tableView;
    SafeInsuredContainer* _container;
    
    //投保人列表
    NSMutableArray<SafeContact*>* _array;
}

@end

@implementation SafeInsuredListView
-(NSArray*)getInsured{
    return _array;
}

-(NSString*)validate:(NSMutableDictionary*)data{
    if(!_check.selected){
        NSInteger index = 0;
        NSMutableArray* arr = [[NSMutableArray alloc]init];
        for (SafeInsuredCell* cell in _container.subviews[1].subviews) {
            
            SafeContact* c = _array[index];
            
            NSString* error = [cell validate:c];
            if(error){
                
                return error;
            }
            
            [arr addObject:[c toJson]];
            
            index ++;
            
        }
        data[@"insured"] =arr;
    }
    
    
    
    return nil;
    
}





-(void)awakeFromNib{
    _check = [self viewWithTag:999];
    _check.checkDelegate = self;
    _addInsured = [self viewWithTag:1000];
    Control_AddTarget(_addInsured, onClick);
    _tableView = [self viewWithTag:1001];
    
    _container = [self viewWithTag:1002];
    
   
    dispatch_async(dispatch_get_main_queue(), ^{
        _container.isMutil = _isMutil;
        [_container setHidden:YES];
        if(_isMutil){
            [_tableView registerNib:@"SafeInsuredWithRelationCell"  bundleName:@"safebundle.bundle" rowHeight:180];
        }else{
            [_tableView registerNib:@"SafeInsuredCell"  bundleName:@"safebundle.bundle" rowHeight:130];
        }
        [_tableView setListener:self];
        
    });
    
    
}
-(void)onInitializeView:(UIView*)parent cell:(SafeInsuredCell*)cell data:(SafeContact*)data index:(NSInteger)index{
    
    cell.data = data;
    if([cell isKindOfClass:[SafeInsuredWithRelationCell class]]){
        [((SafeInsuredWithRelationCell*)cell) setDelegate:self];
    }
}
-(void)onRemove:(id)data{
    [_array removeObject:data];
    

    if(_array.count==0){
        [_container setHidden:YES];
        _check.selected = YES;
    }else{
        DMTableView* tableView = _container.subviews[1];
        [tableView setVal:_array];
        [_container setHidden:YES];
        [_container setHidden:NO];
    }
}
-(void)onClick:(id)sender{
    
    if(_array.count >= 5){
        
        [Alert toast:@"最多只能添加五个被保险人"];
        return;
    }
    
    [self addNew];
    DMTableView* tableView = _container.subviews[1];
    [tableView setVal:_array];
    [_container setHidden:YES];
    [_container setHidden:NO];
    
    
}

-(void)addNew{
    SafeContact* contact = [[SafeContact alloc]init];
    contact.index = _array.count+1;
    [_array addObject:contact];
}

-(void)onSelect:(NSArray*)arr{
    _check.selected = NO;
    NSInteger index = 1;
    if(_isMutil){
        for (SafeContact* data in arr) {
            if(_array.count < 5){
                if(![self contains:data.idCard]){
                    [_array addObject:data];
                }
            }
        }
        
        if(_array.count==0){
            _array = [[NSMutableArray alloc]initWithArray:arr];
        }
        
        for (SafeContact* data in _array) {
             data.index = index++;
        }
       
    }else{
        _array = [[NSMutableArray alloc]initWithArray:arr];
    }
    
    
    
    [_tableView setVal:_array];
    [_container setHidden:NO];
}

-(BOOL)contains:(NSString*)idCard{
    
    
    for (SafeContact* data in _array) {
        if([idCard isEqualToString:data.idCard]){
            return TRUE;
        }
    }
    return NO;
}

-(void)check:(id)check didChangeSelected:(BOOL)selected{
    if(!selected ){
        if(!_array){
            //如果是第一次
            _array = [[NSMutableArray alloc]init];
            [self addNew];
        }else{
            if(_array.count == 0){
                [self addNew];
            }
        }
          [_tableView setVal:_array];
    }

    
    [_container setHidden:selected];
    
}
@end
