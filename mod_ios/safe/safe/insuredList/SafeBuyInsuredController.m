//
//  TestViewController.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyInsuredController.h"
#import "SafeBuyHeaderCell.h"
#import "InsuredContactView.h"
#import "SafeContact.h"
#import "SafeOneInsuredCell.h"
#import "SafeBottomCell.h"
#import "SafeInsuredListCell.h"
#import "SafeModel.h"
#import "SafeUtil.h"
#import "SafePayAction.h"
#import "SafeCashierController.h"
#import "SafePayResultController.h"

@interface SafeBuyInsuredController ()
{
    BOOL _isCenterShow;
    
    SafeBuyHeaderCell* _header;
    id _center;
    UITableViewCell* _info;
    NSMutableArray* _views;
    DMApiJob* _task;
    BOOL _mutilSelect;
    
    InsuredContactView* _contactView;
    SafeBottomCell* _bottom;
    
    
    NSMutableArray* _selectedInsured;
    
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end

@implementation SafeBuyInsuredController

@synthesize data=_data;
INIT_BUNDLE_CONTROLLER(SafeBuyInsuredController, safebundle.bundle)
-(void)dealloc{
    _task = nil;
    _header = nil;
    _center = nil;
    _info = nil;
    _views = nil;
    _contactView = nil;
    _bottom = nil;
    _selectedInsured = nil;
}


-(void)onSelect:(UIButton *)button{
    NSArray* src = _task.data;
    if(_mutilSelect){
       SafeContact* contact =src[button.tag];
        NSInteger index = 0;
         SafeInsuredListCell* safeInsured = _center;
       if(!button.selected){
          
           
           NSMutableArray* insured = [safeInsured getInsuredList];
           if(insured.count>=5){
               [Alert toast:@"最多只能选择5个被保险人"];
               return;
           }
           if(insured.count == 0){
               [insured addObject:contact];
               button.selected = YES;
           }else{
               BOOL found = NO;
               for (SafeContact* data in insured) {
                   //如果,contact和所有的身份证都不一样
                   if([data.idCard isEqualToString:@""]){
                       //则替换这个
                       insured[index] =contact;
                       button.selected = YES;
                       found = YES;
                       break;
                   }
                   if([data.idCard isEqualToString:contact.idCard]){
                       found = YES;
                   }
                   ++index;
               }
               if(!found){
                    [insured addObject:contact];
                   button.selected = YES;
               }

           }
           
           [safeInsured.tableView setVal:insured];
          
        }else{
            //将选中的人下去
            
            NSMutableArray* insured = [safeInsured getInsuredList];
            for (SafeContact* data in insured) {
                //如果,contact和所有的身份证都不一样
                if([data.idCard isEqualToString:contact.idCard]){
                    [insured removeObject:data];
                     button.selected = NO;
                    break;
                }
                ++index;
            }

            [safeInsured.tableView setVal:insured];
           
        }
        
        
        [_tableView beginUpdates];
        [_tableView endUpdates];
        
    }else{
        //这里之选中这个
        
        SafeOneInsuredCell* one = _center;
        [one setVal:src[button.tag]];
        
        
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"填写投保信息"];
    self.view.backgroundColor = RGB_WHITE(f2);
    self.payResultController = [SafePayResultController class];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeBuyHeaderCell" bundle:nil] forCellReuseIdentifier:@"CellHeader"];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeInsuredListCell" bundle:nil] forCellReuseIdentifier:@"CellInsuredList"];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeOneInsuredCell" bundle:nil] forCellReuseIdentifier:@"CellInsured"];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeBuyAddressCell" bundle:nil] forCellReuseIdentifier:@"CellFamilly"];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeCarCell" bundle:nil] forCellReuseIdentifier:@"CellCar"];
    [_tableView registerNib:[UINib nibWithNibName:@"SafeBottomCell" bundle:nil] forCellReuseIdentifier:@"BottomButton"];
    //多选还是单选
    //如果是多人保险
    if([_data.inId isEqualToString:@"2003"]){
        _mutilSelect = YES;
    }else{
        _mutilSelect = NO;
    }
    if(_mutilSelect){
        _selectedInsured = [[NSMutableArray alloc]init];
        [_selectedInsured addObject:[[SafeContact alloc]init]];
    }

    
    
    
    _tableView.dataSource = self;
    _tableView.delegate= self;
    [_tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    [_tableView form];
    
}
- (IBAction)onAddInsured:(id)sender {
     SafeInsuredListCell* safeInsured = _center;
    [safeInsured.tableView.array addObject:[[SafeContact alloc]init]];
    [safeInsured.tableView setVal:safeInsured.tableView.array];
    [_tableView beginUpdates];
    [_tableView endUpdates];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if(section==1){
        return 3;
    }
    return 1;
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}

-(void)onInitializeView:(UIView*)parent cell:(UIView*)cell data:(NSObject*)data index:(NSInteger)index{
    
    
    
}


-(void)jobSuccess:(DMApiJob*)request{
    
    [_contactView setData:request.data];
    [_tableView beginUpdates];
    [_tableView endUpdates];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    if(section==1){
        if(_isCenterShow){
           
            return _contactView;
        }
    }
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if(section == 1){
        if(_isCenterShow){
            if(!_contactView){
                _contactView = [[InsuredContactView alloc]initWithFrame:CGRectMake(0, 210, self.view.frame.size.width, 10)];
                _contactView.tabDelegate = self;
                //加载联系人
                if(!_task){
                    _task = [[DMJobManager sharedInstance]createArrayJsonTask:@"i_contact/list" cachePolicy:DMCachePolicy_NoCache server:1];
                    _task.clazz = [SafeContact class];
                    _task.delegate = self;
                    [_task execute];
                }
                
                if(_task.data){
                    [_contactView setData:_task.data];
                    //将_contactView的button选中
                    NSArray* arr = _task.data;
                    NSInteger index = 0;
                    //如果在被选中的里面有
                    for (SafeContact* contact in arr) {
                        if([_selectedInsured containsObject:contact]){
                            
                            UIButton* btn = _contactView.subviews[index];
                            btn.selected = YES;
                        }
                        ++index;
                    }
                }
            }
            return _contactView.currentHeight;
        }else{
            if(_contactView){
                [_contactView removeFromSuperview];
                _contactView = nil;
            }
        }
    }
    return 0;
}

-(void)tableView:(UITableView*)tableView  willDisplayCell:(UITableViewCell*)cell forRowAtIndexPath:(NSIndexPath*)indexPath
{
    [cell setBackgroundColor:[UIColor clearColor]];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    //1
    __kindof UITableViewCell* cell;
    NSInteger section = indexPath.section;
    if(section == 0){
        if(!_header){
            cell = [tableView dequeueReusableCellWithIdentifier:@"CellHeader"];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            _header = cell;
            _header.check.checkDelegate = self;
        }
        
        return 210;
    }else{
        switch (indexPath.row) {
            case 0:
            {
                
                if(!_center){
                    if(_mutilSelect){
                        cell = [tableView dequeueReusableCellWithIdentifier:@"CellInsuredList"];
                        SafeInsuredListCell* safeInsured = _center;
                        [safeInsured.tableView setVal:_selectedInsured];
                        [safeInsured.tableView setListener:self];
                    }else{
                        cell = [tableView dequeueReusableCellWithIdentifier:@"CellInsured"];
                    }
                    
                    cell.selectionStyle = UITableViewCellSelectionStyleNone;
                    _center = cell;
                }
                if(_isCenterShow){
                    if(_mutilSelect){
                        return 180 * _selectedInsured.count + 30;
                    }
                    return 130;
                }else{
                    return 0;
                }

            }
                break;
            case 1:
            {
                if(!_info){
                    if(_data.type == SafeType_Family){
                        cell = [tableView dequeueReusableCellWithIdentifier:@"CellFamilly"];
                    }else if(_data.type == SafeType_Car){
                        cell = [tableView dequeueReusableCellWithIdentifier:@"CellCar"];
                    }else{
                        cell = [[UITableViewCell alloc]initWithFrame:CGRectZero];
                    }
                    
                    cell.selectionStyle = UITableViewCellSelectionStyleNone;
                    _info = cell;
                }
                
                
                if(_data.type == SafeType_Family || _data.type == SafeType_Car){
                    return 100;
                }
                return 0;
                
            }
            case 2:
            {
                if(!_bottom){
                    cell = [tableView dequeueReusableCellWithIdentifier:@"BottomButton"];
                    cell.selectionStyle = UITableViewCellSelectionStyleNone;
                    _bottom = cell;
                    Control_AddTarget(_bottom.btnSubmit, onSubmit);
                }
            }
                return 60;
        }
        
    }
    return 0;
    
}


-(void)onSubmit:(id)sender{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    
    NSString* error = [_header validate:dic];
    if(error){
        [Alert alert:error];
        return;
    }
    if(_isCenterShow){
        id<DMFormElement> form = _center;
        error = [form validate:dic];
        if(error){
            [Alert alert:error];
            return;
        }

    }
    //否则验证底下的
    if([_info conformsToProtocol:@protocol(DMFormElement)]){
        id<DMFormElement> info = (id<DMFormElement>)_info;
        error = [info validate:dic];
        if(error){
            [Alert alert:error];
            return;
        }
    }
    
    //最后提交
 //   [SafeModel submitInsured:dic typeId:_data.typeId inId:_data.inId count:_data.count button:_bottom.btnSubmit];
 }

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSInteger section = indexPath.section;
    if(section == 0){
        return _header;
    }else{
        switch (indexPath.row) {
            case 0:
            {
                if(_mutilSelect){
                    SafeInsuredListCell* safeInsured = _center;
                    [safeInsured.tableView setVal:_selectedInsured];
                }
            }
                return _center;
                break;
            case 1:
                return _info;
            case 2:
                return _bottom;
        }
    }
    return nil;
}

-(void)check:(id)check didChangeSelected:(BOOL)selected{
    _isCenterShow = !selected;
    [_tableView beginUpdates];
    [_tableView endUpdates];
    
}


API_JOB_SUCCESS(i_safe, submitInsured, NSDictionary*){
    [self createPayModel];
    
    if(!_payModel.action){
        _payModel.action = [[SafePayAction alloc]init];
    }
    
    
    //这里进入收银台
    NSMutableDictionary* data = [[NSMutableDictionary alloc]initWithDictionary:result];
    //这里修改几个字段
    data[@"guardUrl"] = [SafeUtil getUrl:data[@"guardUrl"]];
    //infos
    NSMutableArray* infos = [[NSMutableArray alloc]init];
    if([data getString:@"addr"]){
        [infos addObject:@{@"label":@"保障地址:",@"value":data[@"addr"]}];
    }
    data[@"infos"]=infos;
    data[@"fee"] = [NSString stringWithFormat:@"%.2f",[data[@"fee"]floatValue] / 100];
    [DMPayModel runningInstance].orderId = data[@"id"];
     Push_ViewController_Data(SafeCashierController, data);
}
/*
{
    "result": {
        "id": "16012812154587201",
        "platId": null,
        "payStatus": 0,
        "payType": 0,
        "fee": 1,
        "createTime": "2016-01-28 03:36:17",
        "payTime": null,
        "refundTime": null,
        "cmId": 95,
        "cmAcc": "18659210057",
        "insured": null,
        "carNo": null,
        "carFrame": null,
        "userError": null,
        "name": "high",
        "addr": "福建省漳州市市辖区ffgggg",
        "idCard": "320483198110070514",
        "phone": "1808766",
        "pc": null,
        "idImg": null,
        "cardId": null,
        "ticket": null,
        "inId": "2001",
        "duration": "1",
        "typeId": "2",
        "title": "家康险",
        "piccOrderId": null,
        "guardUrl": "/article/show/37",
        "guard": "产品详情",
        "com": "PICC",
        "comIcon": "PICC",
        "protocolUrl": "/article/show/31",
        "startTime": null,
        "endTime": null,
        "noticeUrl": "/article/show/34",
        "backgroundUrl": null,
        "summary": "居家日用，一张保单来呵护^门槛超低，18万保障过一年",
        "serviceTel": "0592-95518",
        "count": 0,
        "dynamicFields": []
    },
    "flag": 0
}*/
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
