//
//  Form.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>


//表示一个抽象的表单类,加载后应该自动找到内部的表单元素
@protocol DMForm <NSObject>

//所有的表单元素
//-(NSArray*)allFormElements;

-(NSMutableArray<NSObject<DMFormElement>*>*)elements;
@end
