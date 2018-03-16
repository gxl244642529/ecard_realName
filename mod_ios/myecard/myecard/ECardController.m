//
//  ECardController.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardController.h"

#import "ScanViewController.h"
#import "ECardModel.h"
#import "ECardBindController.h"
#import "ECardDetailController.h"
#import "ECardCell.h"
#import "MyECardVo.h"

@interface ECardController()<IDataAdapterListener>
{
    
    __weak IBOutlet DMTableView *_tableView;
}

@end

@implementation ECardController

INIT_BUNDLE_CONTROLLER(ECardController,myecardbundle.bundle)

+(void)selectECard:(UIViewController*)parent delegate:(id<ECardSelectDelegate>)delegate{
    
    ECardController* c = [[ECardController alloc]init];
    c.delegate = delegate;
    [parent.navigationController pushViewController:c animated:YES];
    
}



-(void)viewDidLoad{
    [super viewDidLoad];
    if(_delegate){
        [self setTitle:@"选择e通卡"];
    }else{
        [self setTitle:@"我的e通卡"];
    }
    _tableView.useCache = NO;
    [_tableView.apiJob put:@"moneyCard" value:[NSNumber numberWithBool:TRUE] ];
    [_tableView setOnItemClickListener:self];
    [_tableView setListener:self];
}



-(void)onItemClick:(UIView *)parent data:(ECard *)data index:(NSInteger)index{
    if(_delegate){
        [_delegate didSelectECard:data];
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        //这里需要进入详情
        ECardDetailController* detail = [[ECardDetailController alloc]init];
        detail.data = data;
        [self.navigationController pushViewController:detail animated:YES];
    }
}

-(void)onScan:(ScanViewController*)controller data:(NSString*)data{
    [ECardModel bindBarcode:data];
}


API_JOB_SUCCESS(ecard, bind, id){
    //半丁成功
    [_tableView reloadWithStatus];
}

API_JOB_SUCCESS(ecard, unbind, id){
    
    [_tableView reloadWithStatus];
    
}
API_JOB_SUCCESS(ecard, update, id){
    
   [_tableView reloadWithStatus];
}

API_JOB_SUCCESS(ecard, bindBarcode, id){
    
    //半丁成功
    [_tableView reloadWithStatus];
    
    
}


-(void)onInitializeView:(UIView*)parent cell:(ECardCell*)cell data:(MyECardVo*)data index:(NSInteger)index{
    
    if(data.createDate){
        [cell.icon setImage:[UIImage imageNamed:@"myecardbundle.bundle/isreal.png"]];
        cell.isReal.hidden = NO;
    }else{
        [cell.icon setImage:[UIImage imageNamed:@"myecardbundle.bundle/card_noreal.png"]];
         cell.isReal.hidden = YES;
    }
    
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    switch (buttonIndex) {
        case 0:
            Push_ViewController(ECardBindController);
            break;
        case 1:
            //扫码绑定
            [self.navigationController pushViewController:[[ScanViewController alloc]initWithListener:self] animated:YES];
            break;
    }
}


- (IBAction)onBindECard:(id)sender {
    UIActionSheet* actionSheet = [[UIActionSheet alloc]initWithTitle:@"选择绑定方式" delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"输入卡号绑定" otherButtonTitles:@"扫码绑定", nil];
    actionSheet.actionSheetStyle = UIActionSheetStyleBlackOpaque;
    [actionSheet showInView:self.view];
}
@end
