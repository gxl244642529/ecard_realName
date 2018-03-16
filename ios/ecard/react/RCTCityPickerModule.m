//
//  RCTCityPickerModule.m
//  ecard
//
//  Created by 任雪亮 on 17/3/14.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTCityPickerModule.h"

#import <dmlib/dmlib.h>


#import <ecardlib/ecardlib.h>

@interface RCTCityPickerModule()<UIPickerViewDataSource,UIPickerViewDelegate>{
  DMApiJob* _task;
  NSArray *_provinces, *_cities;
  UITextField* _txt;
  
}
@property (nonatomic,assign) IBInspectable NSInteger provinceId;
@property (nonatomic,assign) IBInspectable NSInteger cityId;

//固定省id
@property (nonatomic,assign) IBInspectable NSInteger fixProvinceId;


@property (nonatomic,copy) NSString* province;
@property (nonatomic,copy) NSString* city;
@end

@implementation RCTCityPickerModule

RCT_EXPORT_MODULE();


RCT_EXPORT_METHOD(select:(RCTResponseSenderBlock)callback){
  
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIView* view = [[DMJobManager sharedInstance].topController.view findFirstResponsder];
    if(view){
      [view resignFirstResponder];
    }
  });
  
  
  dispatch_async(dispatch_get_main_queue(), ^{
    if(!_task){
      _task = [[DMJobManager sharedInstance]createArrayJsonTask:@"ssq/ssq" cachePolicy:DMCachePolicy_Permanent server:1];
      _task.clazz = [AddrInfoV2 class];
      _task.delegate = self;
      _task.waitingMessage = @"加载中...";
    }else{
      if(_task.isRunning){
        return;
      }
    }
    [_task execute];
    
  });
  

}





-(void)jobSuccess:(DMApiJob*)request{
  UIPickerView* picker = [[UIPickerView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT/3)];
  
  
  _provinces = request.data;
  picker.delegate = self;
  picker.dataSource = self;
  
  NSInteger index=0;
  if(_fixProvinceId > 0){
    
    for (AddrInfoV2* dic in _provinces) {
      if(dic.id == _fixProvinceId){
        
        _provinces = @[dic];
        [picker selectRow:0 inComponent:0 animated:NO];
        _cities = dic.list;
        if(_cityId > 0 ){
          
          [self selectCity:picker];
        }
        
        break;
      }
      ++index;
    }
    
    
    
  }else{
    
    //查找
    if(_provinceId>0){
      
      for (AddrInfoV2* dic in _provinces) {
        if(dic.id == _provinceId){
          [picker selectRow:index inComponent:0 animated:NO];
          _cities = dic.list;
          if(_cityId > 0 ){
            
            [self selectCity:picker];
          }
          break;
          
        }
        index++;
      }
    }else{
      _cities = [_provinces[0] valueForKey:@"list"];
    }
    
    
  }
  
  
  
  
  
  
  
  __weak RCTCityPickerModule* __self = self;
  [DMPopup bottom:picker title:@"选择地区" listener:^(UIPickerView* picker, BOOL isOk) {
    if(isOk){
      [__self onSelect:picker];
    }
  }];
  
  
}



-(void)onSelect:(UIPickerView*)picker{
  NSInteger row = [picker selectedRowInComponent:0];
  _provinceId = [[[_provinces objectAtIndex:row] valueForKey:@"id"]integerValue];
  self.province =[[_provinces objectAtIndex:row] valueForKey:@"name"];
  row = [picker selectedRowInComponent:1];
  _cityId = [[[_cities objectAtIndex:row] valueForKey:@"id"]integerValue];
  self.city =[[_cities objectAtIndex:row] valueForKey:@"name"];
  row = [picker selectedRowInComponent:2];
  /*if(_areas ){
    _areaId = [[_areas[row] valueForKey:@"id"]integerValue];
    self.area =[_areas[row] valueForKey:@"name"];
  }else{
    _areaId = 0;
    self.area = nil;
  }*/
  
  
  ///_txt.text = [self getArea];
  
}



-(BOOL)selectCity:(UIPickerView*) picker{
  NSInteger index=0;
  
  for(AddrInfoV2* dic in _cities){
    if(dic.id == _cityId){
      dispatch_async(dispatch_get_main_queue(), ^{
        [picker selectRow:index inComponent:1 animated:NO];
      });
      
     
      return TRUE;
    };
    index++;
  }
  return FALSE;
}



- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
  return 2;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
  switch (component) {
    case 0:
      return [_provinces count];
      break;
    case 1:
      return [_cities count];
      break;
    default:
      return 0;
      break;
  }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
  switch (component) {
    case 0:
      return [[_provinces objectAtIndex:row] valueForKey:@"name"];
      break;
    case 1:
      return [[_cities objectAtIndex:row] valueForKey:@"name"];
      break;
    default:
      return  @"";
      break;
  }
  
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
  switch (component) {
    case 0:
      _cities = [[_provinces objectAtIndex:row] valueForKey:@"list"];
      [pickerView selectRow:0 inComponent:1 animated:YES];
      [pickerView reloadComponent:1];
      break;
    case 1:
     
      break;
  }
}


@end
