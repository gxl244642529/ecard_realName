//
//  SEditAddrController.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "FormScrollView.h"
#import "FormModel.h"
#import "UIAreaPicker.h"
#import "BaseViewController.h"
#import "SellingModel.h"
typedef enum _AddrEditMode{
    AddrEditMode_Add,
    AddrEditMode_Edit
}AddrEditMode;

@interface SEditAddrController : BaseViewController<IFormListener,UIPopupDelegate,IRequestResult>
@property (nonatomic) AddrEditMode mode;

-(SAddrListVo*)addrData;
@end
