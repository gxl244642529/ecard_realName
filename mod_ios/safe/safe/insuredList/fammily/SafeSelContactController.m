//
//  SafeSelContactController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/17.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeSelContactController.h"

#import "SafeUtil.h"

@interface SafeSelContactController ()
{
    NSMutableArray* _selectedArray;
    BOOL _isMutil;
}
@property (weak, nonatomic) IBOutlet UILabel *txtLabel;
@property (weak, nonatomic) IBOutlet DMTableView *tableView;
@property (weak, nonatomic) IBOutlet UIView *bottomView;
@property (weak, nonatomic) IBOutlet PageButton *btnOk;

@end

@implementation SafeSelContactController


@synthesize data=_data;

-(void)dealloc{
    _selectedArray =nil;
}




-(id)init{
    if(self = [super initWithNibName:@"SafeSelContactController" bundle:CREATE_BUNDLE(safebundle.bundle)]){
        _selectedArray = [[NSMutableArray alloc]init];
    }
    return self;
}



- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"选择联系人"];
    _btnOk.enabled = FALSE;
    
    [_tableView setOnItemClickListener:self];
   
    //duoxuan
    if([SafeUtil isQuanjiabao:_data]){
        _isMutil = YES;
        _txtLabel.text = @"您最多可以选择五个被保险人";
    }else{
        _txtLabel.text = @"您可以选择一个被保险人";
        [DMViewUtil hideView:_bottomView];
    }
    
}


-(void)setSelected:(NSArray*)selected{
    
}

API_JOB_SUCCESS(i_contact, list, NSArray*){
    if(_isMutil){
        _tableView.delegate = self;
    }
}

-(NSInteger)getIndex:(NSArray*)arr data:(SafeContact*)data{
    NSInteger index = 0;
    for(SafeContact* d in arr){
        if([d.idCard isEqualToString:data.idCard]){
            return index;
        }
        index ++;
    }
    return -1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    id data = [_tableView.adapter getItem:indexPath.row];
    if(![_selectedArray containsObject:data]){
        [_selectedArray addObject:data];
    }
    _btnOk.enabled = _selectedArray.count > 0  && _selectedArray.count <=5;
}
- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    id data = [_tableView.adapter getItem:indexPath.row];
    if([_selectedArray containsObject:data]){
        [_selectedArray removeObject:data];
    }
    _btnOk.enabled = _selectedArray.count > 0  && _selectedArray.count <=5;
}

- (IBAction)onOk:(id)sender {
    
    [_delegate onSelectContacts:_selectedArray];
    [self finish];
}

-(void)onItemClick:(UIView*)parent data:(SafeContact*)data index:(NSInteger)index{
    //直接返回
    [_delegate onSelectContacts:@[data]];
    [self finish];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
