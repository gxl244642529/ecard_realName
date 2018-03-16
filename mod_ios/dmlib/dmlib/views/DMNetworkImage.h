//
//  UINetworkImage.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMJobDelegate.h"
#import "DMValue.h"

IB_DESIGNABLE
@interface DMNetworkImage : UIImageView<DMJobDelegate,DMValue>


-(void)load:(NSString*)url;

-(CGSize)imageSize;
@property (nonatomic,retain) IBInspectable NSString* url;

//保持图片大小和这个一直
@property (nonatomic,assign) IBInspectable BOOL keepRate;

@end
