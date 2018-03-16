//
//  FormSubmit.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMApiView.h"

#import "DMFormElement.h"
#import "DMForm.h"

/**
 用于提交表单
 */
@protocol DMSubmit <DMApiView>


//表单元素的名字,如有多个,则用逗号隔开;
//如为空,则表示传输所有表单元素
@property (nonatomic,copy) NSString* submitItems;

//提交表单
-(void)submit;

@property (nonatomic,weak) NSObject<DMForm>* form;

@end


