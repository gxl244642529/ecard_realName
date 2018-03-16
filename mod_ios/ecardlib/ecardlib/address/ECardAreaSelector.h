//
//  ECardAreaSelector.h
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <dmlib/dmlib.h>


IB_DESIGNABLE
@interface ECardAreaSelector : DMItem<DMApiDelegate,UIPickerViewDataSource,UIPickerViewDelegate,DMFormElement,DMMutilValue>



-(void)shiId:(NSNumber*)shiId shengId:(NSNumber*)shengId quId:(NSNumber*)quId area:(NSString*)area sheng:(NSString*)sheng shi:(NSString*)shi qu:(NSString*)qu;

@property (nonatomic,assign) IBInspectable NSInteger provinceId;
@property (nonatomic,assign) IBInspectable NSInteger cityId;
@property (nonatomic,assign) IBInspectable NSInteger areaId;

//固定省id
@property (nonatomic,assign) IBInspectable NSInteger fixProvinceId;


@property (nonatomic,copy) NSString* province;
@property (nonatomic,copy) NSString* city;
@property (nonatomic,copy) NSString* area;



-(NSString*)getArea;
@end
