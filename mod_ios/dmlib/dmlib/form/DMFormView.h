//
//  FormView.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMFormElement.h"
#import "DMForm.h"

IB_DESIGNABLE
@interface DMFormView : UIView<DMForm>

//是否需要提交文件

-(NSMutableArray<NSObject<DMFormElement>*>*)elements;

@end
