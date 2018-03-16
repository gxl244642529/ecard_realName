//
//  UIAreaPicker.h
//  test123123
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "UIBottomView.h"
#import <DMLib/dmlib.h>

@interface AddrInfo : NSObject<IJsonValueObject>

@property (nonatomic,retain) NSString* name;
@property (nonatomic) NSInteger ID;
@property (nonatomic,retain) NSArray* list;

@end

@interface UIAreaPicker : UIBottomView<UIPickerViewDataSource,UIPickerViewDelegate>


-(id)initWithSheng:(NSArray*)provinces;

@property (nonatomic) NSInteger province_id;
@property (nonatomic) NSInteger city_id;
@property (nonatomic) NSInteger area_id;

@property (nonatomic) NSString* province;
@property (nonatomic) NSString* city;
@property (nonatomic) NSString* area;

@end
