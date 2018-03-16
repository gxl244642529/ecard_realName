//
//  FormImage.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMValue.h"
#import "DMFormElement.h"
IB_DESIGNABLE
@interface DMFormImage : UIImageView<DMFormElement,DMValue>

@property (nonatomic,retain) IBInspectable NSString* fieldName;
@property (nonatomic,assign) IBInspectable BOOL required;


//图片最大尺寸
@property (nonatomic,assign) IBInspectable NSInteger maxSize;


-(UIImage*)resizeImage:(UIImage*)image;

@end
