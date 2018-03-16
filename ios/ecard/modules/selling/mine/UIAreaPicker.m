//
//  UIAreaPicker.m
//  test123123
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//





#import "UIAreaPicker.h"
#import <DMLib/dmlib.h>


@implementation AddrInfo

-(void)dealloc{
    self.name = nil;
    self.list = nil;
}

-(void)fromJson:(NSDictionary *)json{
    
    self.name = [json valueForKey:@"NAME"];
    self.ID = [[json valueForKey:@"ID"]integerValue];
    
    
    NSArray* arr = [json valueForKey:@"list"];
    if(arr){
        arr = [AddrInfo parseJsonArray:arr class:[AddrInfo class]];
        self.list = arr;
    }
    
}
+(NSMutableArray*)parseJsonArray:(NSArray*)src class:(Class)clazz{
    NSMutableArray* ret = [[NSMutableArray alloc]init];
    for (NSDictionary* dic in src) {
        NSObject<IJsonValueObject>* obj = [[clazz alloc]init];
        [obj fromJson:dic];
        [ret addObject:obj];
    }
    return ret;
}

@end


@interface UIAreaPicker()
{
    NSArray *_provinces, *cities, *areas;
    UIPickerView* _picker;
}

@end

@implementation UIAreaPicker

-(void)dealloc{
    
    _provinces = NULL;
    cities = NULL;
    areas = NULL;
    
    self.province = nil;
    self.city = nil;
    self.area = nil;
}



-(id)initWithSheng:(NSArray*)provinces{
    if(self=[super initWithTitle:@"选择地区"]){
        
        _picker= [[UIPickerView alloc]initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 200)];
        [self setContentView:_picker];
        _picker.delegate = self;
        _picker.dataSource = self;
        
        _provinces = provinces;
        cities = [provinces[0] valueForKey:@"list"];
        areas = [[cities objectAtIndex:0] valueForKey:@"list"];
        

        
    }
    return self;
}



-(void)show
{
    //查找
    if(self.province_id>0){
        NSInteger index=0;
        for (AddrInfo* dic in _provinces) {
            if(dic.ID == self.province_id){
                [_picker selectRow:index inComponent:0 animated:NO];
                cities = dic.list;
                if(self.city_id > 0 ){
                    [self selectCity];
                }
                break;

            }
            index++;
        }
    }
    
    
    [super show];
}

-(BOOL)selectCity{
    NSInteger index=0;

    for(AddrInfo* dic in cities){
        if(dic.ID == self.city_id){
            [_picker selectRow:index inComponent:1 animated:NO];
            areas = dic.list;
            if(self.area_id > 0){
                [self selectArea];
            }
            return TRUE;
        };
        index++;
    }
    return FALSE;
}

-(BOOL)selectArea{
    NSInteger index=0;
    
    for(AddrInfo* dic in areas){
        if(dic.ID == self.area_id){
            [_picker selectRow:index inComponent:2 animated:NO];
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
            return [cities count];
            break;
        case 2:
            return [areas count];
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
            return [[cities objectAtIndex:row] valueForKey:@"name"];
            break;
        case 2:
            if (areas > 0) {
                return [[areas objectAtIndex:row] valueForKey:@"name"];
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
            cities = [[_provinces objectAtIndex:row] valueForKey:@"list"];
            [_picker selectRow:0 inComponent:1 animated:YES];
            [_picker reloadComponent:1];
            areas = [[cities objectAtIndex:0] valueForKey:@"list"];
            [_picker selectRow:0 inComponent:2 animated:YES];
            [_picker reloadComponent:2];
            /*
            self.province = [[provinces objectAtIndex:row] valueForKey:@"NAME"];
            self.city = [[cities objectAtIndex:0] valueForKey:@"NAME"];
            if ([areas count] > 0) {
                self.area = [areas objectAtIndex:0];
            } else{
                self.area = NULL;
            }*/
            break;
        case 1:
            areas = [[cities objectAtIndex:row] valueForKey:@"list"];
            [_picker selectRow:0 inComponent:2 animated:YES];
            [_picker reloadComponent:2];
            /*
            self.city = [[cities objectAtIndex:row] valueForKey:@"NAME"];
            if ([areas count] > 0) {
                self.area = [areas objectAtIndex:0];
            } else{
                self.area = NULL;
            }*/
            break;
        case 2:
            /*
            if ([areas count] > 0) {
                self.area = [areas objectAtIndex:row];
            } else{
                self.area = NULL;
            }*/
            break;
        default:
            break;
    }
}

-(void)onOk{
    NSInteger row = [_picker selectedRowInComponent:0];
    self.province_id = [[[_provinces objectAtIndex:row] valueForKey:@"ID"]integerValue];
    self.province =[[_provinces objectAtIndex:row] valueForKey:@"name"];
    row = [_picker selectedRowInComponent:1];
    self.city_id = [[[cities objectAtIndex:row] valueForKey:@"ID"]integerValue];
    self.city =[[cities objectAtIndex:row] valueForKey:@"name"];
    row = [_picker selectedRowInComponent:2];
    if(areas ){
        self.area_id = [[areas[row] valueForKey:@"ID"]integerValue];
        self.area =[areas[row] valueForKey:@"name"];
    }else{
        self.area_id = 0;
        self.area = nil;
    }
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
