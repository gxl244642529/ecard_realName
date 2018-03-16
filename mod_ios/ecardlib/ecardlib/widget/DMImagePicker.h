//
//  DMImagePicker.h
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditorUtil.h"
#import <dmlib/dmlib.h>
//图片抓取
IB_DESIGNABLE
@interface DMImagePicker : DMItem<CLImageEditorDelegate>

//是否支持我的图片
@property (nonatomic,assign) IBInspectable BOOL album;

//是否支持编辑
@property (nonatomic,assign) IBInspectable BOOL edit;

//一下属性只有edit为true有效


@property (nonatomic,copy) IBInspectable NSString* title;
//是否强制剪裁 
@property (nonatomic,assign) IBInspectable BOOL forceCrop;





@end
