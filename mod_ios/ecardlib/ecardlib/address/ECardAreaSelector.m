//
//  ECardAreaSelector.m
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardAreaSelector.h"
#import "AddrInfoV2.h"

#import "AddressVo.h"
@interface ECardAreaSelector(){
    DMApiJob* _task;
    NSArray *_provinces, *_cities, *_areas;
    UITextField* _txt;
    
}

@end

@implementation ECardAreaSelector

-(void)dealloc{
    _task = nil;
    _provinces = NULL;
    _cities = NULL;
    _areas = NULL;
    _txt = nil;
}

-(void)setVal:(AddressVo*)data{
    _cityId = data.shiId;
    _provinceId = data.shengId;
    _areaId = data.quId;
    _province = data.sheng;
    _city = data.shi;
    _area = data.qu;
    _txt.text = data.area;
}



-(NSString*)validate:(NSMutableDictionary*)data{
    if([_txt.text isEqualToString:@""]){
        
        return @"请选择区域";
        
    }
    
    data[@"sheng"] = _province;
    data[@"shi"]=_city;
    data[@"shengId"] = WRAP_INTEGER(_provinceId);
    data[@"shiId"] = WRAP_INTEGER(_cityId);
    if(_areaId>0){
        data[@"quId"] =  WRAP_INTEGER(_areaId);
        data[@"qu"] = _area;
    }
    

   
    return nil;
}

-(NSString*)getArea{
    if(_area){
        return [NSString stringWithFormat:@"%@%@%@",_province,_city,_area];
    }
    return [NSString stringWithFormat:@"%@%@",_province,_city];

}
-(void)awakeFromNib{
    [super awakeFromNib];
    [self setTarget:self withAction:@selector(onClick:)];
    
    if(!_provinceId){
        _provinceId = 350000;
    }
    
    if(!_cityId){
        _cityId = 350200;
    }
    
    if(!_areaId){
        _areaId =350203;
    }
    
    
    for(__kindof UIView* view in self.subviews){
        if([view isKindOfClass:[UITextField class]]){
            _txt = view;
            _txt.userInteractionEnabled = NO;
            break;
        }
    }
    
    
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
            _areas = [[_cities objectAtIndex:0] valueForKey:@"list"];
        }
        

    }
    
  
    
    
    
    
    
    __weak ECardAreaSelector* __self = self;
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
    if(_areas ){
        _areaId = [[_areas[row] valueForKey:@"id"]integerValue];
        self.area =[_areas[row] valueForKey:@"name"];
    }else{
        _areaId = 0;
        self.area = nil;
    }
    
    
    _txt.text = [self getArea];

}

-(void)onClick:(id)sender{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[[self findViewController].view findFirstResponsder]resignFirstResponder];
    });

    
    dispatch_async(dispatch_get_main_queue(), ^{
        if(!_task){
            _task = [[DMJobManager sharedInstance]createArrayJsonTask:@"ssq/ssq" cachePolicy:DMCachePolicy_Permanent server:1];
            _task.clazz = [AddrInfoV2 class];
            _task.delegate = self;
            _task.waitingMessage = @"加载省市区...";
        }else{
            if(_task.isRunning){
                return;
            }
        }
        [_task execute];

    });
    
    
}


-(BOOL)selectCity:(UIPickerView*) picker{
    NSInteger index=0;
    
    for(AddrInfoV2* dic in _cities){
        if(dic.id == _cityId){
            _areas = dic.list;
            dispatch_async(dispatch_get_main_queue(), ^{
                [picker selectRow:index inComponent:1 animated:NO];
            });
            
            if(_areaId > 0){
                [self selectArea:picker];
            }
            return TRUE;
        };
        index++;
    }
    return FALSE;
}

-(BOOL)selectArea:(UIPickerView*) picker{
    NSInteger index=0;
    
    for(AddrInfoV2* dic in _areas){
        if(dic.id == _areaId){
            dispatch_async(dispatch_get_main_queue(), ^{
                [picker selectRow:index inComponent:2 animated:NO];
            });

            
            return TRUE;
        }
        index++;
    }
    return FALSE;
    
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 3;
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
        case 2:
            return [_areas count];
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
        case 2:
            if (_areas > 0) {
                return [[_areas objectAtIndex:row] valueForKey:@"name"];
                break;
            }
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
            _areas = [[_cities objectAtIndex:0] valueForKey:@"list"];
            [pickerView selectRow:0 inComponent:2 animated:YES];
            [pickerView reloadComponent:2];
            break;
        case 1:
            _areas = [[_cities objectAtIndex:row] valueForKey:@"list"];
            [pickerView selectRow:0 inComponent:2 animated:YES];
            [pickerView reloadComponent:2];
            break;
    }
}


@end
