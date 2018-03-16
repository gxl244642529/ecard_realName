//
//  DMPayTableView.m
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPayTableView.h"
#import "DMPayModel.h"
#import "TableDataAdapter.h"

@interface DMPayTableView(){
    TableDataAdapter* _adapter;
}

@end

@implementation DMPayTableView

-(void)dealloc{
    _adapter = nil;
}
-(void)setListener:(id<IDataAdapterListener>)listener{
    [_adapter setListener:listener];
}
-(void)awakeFromNib{
    [super awakeFromNib];
    self.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    self.tableFooterView = [[UIView alloc]init];
    _adapter = [[TableDataAdapter alloc]init];
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
         DMPayModel* model = [DMPayModel runningInstance];
        if(!model){
            NSLog(@"DMPayModel has not inited!");
        }
        model.module = _businessId;
        NSArray* arr = [_support componentsSeparatedByString:@","];
        NSMutableArray* types = [[NSMutableArray alloc]init];
        for (NSString* key in arr) {
            if([key isEqualToString:@"alipay"]){
                [types addObject:[NSNumber numberWithInteger:DMPAY_ALIPAY]];
            }else if([key isEqualToString:@"wxpay"]){
                [types addObject:[NSNumber numberWithInteger:DMPAY_WEIXIN]];
            }else if([key isEqualToString:@"ecard"]){
                [types addObject:[NSNumber numberWithInteger:DMPAY_ETONGKA]];
            }else if([key isEqualToString:@"cmb"]){
                 [types addObject:[NSNumber numberWithInteger:DMPAY_CMB]];
            }
        }
        
        [_adapter setScrollView:self];
        if(_cellName){
            [_adapter registerCell:_cellName bundle:CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName)];
        }
        
        [_adapter setOnItemClickListener:self];
        [_adapter setData:[model getPayTypes]];
        
        [self selectRowAtIndexPath:[NSIndexPath indexPathForRow:model.currentIndex inSection:0] animated:YES scrollPosition:UITableViewScrollPositionMiddle];

    });
    
 
    
   // [[DMPayModel runningInstance]supportPayTyptes:types];
    
}


-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    [DMPayModel runningInstance].currentIndex = index;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
