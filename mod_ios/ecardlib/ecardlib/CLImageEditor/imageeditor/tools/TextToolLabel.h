//
//  TextToolLabel.h
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "IDrag.h"

@interface TextToolLabel : UILabel

-(void)setFontSize:(float)size;
-(NSString*)realText;
-(void)setRealText:(NSString*)realText;

@property (nonatomic,weak) NSObject<IDrag>* delegate;

@end
