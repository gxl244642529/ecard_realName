

//
//  AddressController.m
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "AddressController.h"
#import "AddressEditController.h"
#import "AddressModel.h"
#import "AddressCell.h"

@interface AddressController ()
@property (weak, nonatomic) IBOutlet DMTableView *tableView;

@end

@implementation AddressController

INIT_BUNDLE_CONTROLLER(AddressController, ecardlibbundle.bundle)

-(void)dealloc{
  _delegate = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    if(_delegate){
        [self setTitle:@"选择收货地址"];
    }else{
        [self setTitle:@"收货地址管理"];
    }
    
    UIBarButtonItem* item = [DMViewController createTextItem:@"新增地址" controllerClass:[AddressEditController class] parent:self];
    item.tintColor = [UIColor redColor];
    self.navigationItem.rightBarButtonItem = item;
    [_tableView.adapter setOnItemClickListener:self];
    [_tableView setListener:self];
}


-(void)onInitializeView:(UIView*)parent cell:(AddressCell*)cell data:(AddressVo*)data index:(NSInteger)index{
    cell.def.hidden = !data.def;
    cell.defWidth.constant = data.def ? 32 : 0;
}


-(void)onItemClick:(UIView*)parent data:(id)data index:(NSInteger)index{
    if(_delegate){
        [_delegate onSelectAddress:data];
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        Push_ViewController_Data(AddressEditController, data);
    }
}

+(void)selectAddress:(UIViewController*)parent delegate:(NSObject<AddressSelectDelegate>*)delegate{
    AddressController* controller = [[AddressController alloc]init];
    controller.delegate = delegate;
    [parent.navigationController pushViewController:controller animated:YES];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


ON_ITEM_EVENT(btnEdit,AddressVo*){
    Push_ViewController_Data(AddressEditController, data);
}
ON_ITEM_EVENT(btnDelete,AddressVo*){
    [Alert confirm:@"真的要删除本地址吗?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            [AddressModel deleteAddress:data];
        }
    }];
    
}
API_JOB_SUCCESS(address, delete, id){
    
    [_tableView reloadWithStatus];
    
}

API_JOB_SUCCESS(address, save, id){
    
    [_tableView reloadWithStatus];
    
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
