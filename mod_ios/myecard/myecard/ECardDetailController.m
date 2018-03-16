//
//  ECardDetailController.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardDetailController.h"
#import "ECardModel.h"
#import "ECardUpdateController.h"

@interface ECardDetailController ()
@property (weak, nonatomic) IBOutlet DMDetailView *detailView;
@property (weak, nonatomic) IBOutlet DMButton *btnUnbind;
@property (weak, nonatomic) IBOutlet DMButton *btnUpdate;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *menuHeight;
@property (weak, nonatomic) IBOutlet UIView *btnContainer;

@end

@implementation ECardDetailController
@synthesize data=_data;
- (void)viewDidLoad {
    [super viewDidLoad];
     UIImageView* flag = [self.view viewWithTag:102];
       UIImageView* image = [self.view viewWithTag:23];
    flag.hidden = YES;
    image.hidden = YES;
    _detailView.extraData = @{@"cardFlag" : [NSNumber numberWithInt:_data.cardFlag],@"cardid":_data.cardId  };
    dispatch_async(dispatch_get_main_queue(), ^{
     
       
        
        if(_data.cardFlag == 1 ){
            flag.hidden = NO;
        }else{
            if(_data.createDate && _data.createDate!=[NSNull null]){
                image.image = [UIImage imageNamed:@"myecardbundle.bundle/isreal_flag.png"];
            }
            image.hidden = NO;
        }
    });
   
 
    [self onHideMenu:nil];
}


INIT_BUNDLE_CONTROLLER(ECardDetailController,myecardbundle.bundle)

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self setTitle: [StringUtils isEmpty:self.data.cardName] ? self.data.cardId : self.data.cardName ];
}

-(NSDictionary*)createData:(BOOL)isReal{
    return @{@"cardId":self.data.cardId,
             @"createDate":self.data.createDate ? self.data.createDate : [NSNull null],
             @"real":isReal ? @true:@false
             };
}
API_JOB_SUCCESS(real, is, id){
    BOOL r = [result boolValue];
    SendNotification(@"gotoRealCard", [self createData:r]);
    [self.navigationController popToRootViewControllerAnimated:NO];
    
    
}

- (IBAction)onReal:(id)sender {
    //这里需要查询一下有没有实名
    
    if(self.data.createDate){
       SendNotification(@"gotoRealCard", [self createData:true]);
        [self.navigationController popToRootViewControllerAnimated:NO];
    }else{
        DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"real/is" cachePolicy:DMCachePolicy_NoCache server:1];
        job.waitingMessage=@"请稍等...";
        [job execute];
        
    }
  
    

    
}

-(void)onShowMenu{
    UIBarButtonItem* barItem = [DMViewController createImageItem:[UIImage imageNamed:@"myecardbundle.bundle/ic_ecard_menu"] target:self action:@selector(onHideMenu:)];
    barItem.tintColor = [UIColor redColor];
    self.navigationItem.rightBarButtonItem = barItem;
    _menuHeight.constant = 60;
    [UIView animateWithDuration:0.3 animations:^{
        
        [self.view layoutIfNeeded];
    }];
    
    
}
- (IBAction)onUpdate:(id)sender {
    [self onHideMenu:nil];
    
    ECardUpdateController* controller = [[ECardUpdateController alloc]init];
    controller.data = self.data;
    [self.navigationController pushViewController:controller animated:YES];
    
}

- (IBAction)onUnbind:(id)sender {
    
    __weak ECard* ecard = _data;
    __weak ECardDetailController* __self = self;
    [Alert confirm:@"确定要解除绑定吗?" title:@"温馨提示" confirmListener:^(NSInteger buttonIndex) {
        [__self onHideMenu:sender];
        if(buttonIndex==1){
            [ECardModel unbind:ecard.cardId];
        }
    }];
}


API_JOB_SUCCESS(ecard, unbind, id){
    
    [self.navigationController popViewControllerAnimated:YES];
    
}



-(void)onHideMenu:(id)animated{
    UIBarButtonItem* barItem = [DMViewController createImageItem:[UIImage imageNamed:@"myecardbundle.bundle/ic_ecard_menu_h"] target:self action:@selector(onShowMenu)];
     barItem.tintColor = [UIColor redColor];
    self.navigationItem.rightBarButtonItem = barItem;
    _menuHeight.constant = 0;
    if(animated){
        [UIView animateWithDuration:0.3 animations:^{
            
            [self.view layoutIfNeeded];
        }];
    }
   

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
