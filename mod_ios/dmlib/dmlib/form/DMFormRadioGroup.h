//
//  FormRadio.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMFormElement.h"
@class DMFormRadioGroup;

@protocol DMFormRadioGroupDelegate <NSObject>

-(void)formRadio:(DMFormRadioGroup*)group selectChanged:(NSString*)value index:(NSInteger)index;

@end

IB_DESIGNABLE
@interface DMFormRadioGroup : UIView<DMFormElement>

@property (nonatomic,assign) IBInspectable BOOL required;

//值列表,多个之间用逗号隔开
@property (nonatomic,retain) IBInspectable NSString* values;

@property (nonatomic,retain) IBInspectable NSString* defaultValue;
@property (weak,nonatomic) id<DMFormRadioGroupDelegate> delegate;
@end
