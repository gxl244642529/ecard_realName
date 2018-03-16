//
//  IWidget.h
//  ecard
//
//  Created by randy ren on 15/5/30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#ifndef ecard_IWidget_h
#define ecard_IWidget_h

@protocol IWidget;


@protocol IWidgetValueListener <NSObject>

-(void)onWidgetValueChanged:(id<IWidget>)widget value:(id)value;

@end

@protocol IWidget <NSObject>

-(id)getValue;
-(void)setValue:(id)value;



@end

#endif
