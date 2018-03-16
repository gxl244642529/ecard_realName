//
//  RCTGeoCoderModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/11/12.
//  Copyright © 2016年 Facebook. All rights reserved.
//




#import "RCTGeoCoderModule.h"
//#import <MAMapKit/MAMapKit.h>
//#import <AMapSearchKit/AMapSearchAPI.h>
//#import <AMapFoundationKit/AMapFoundationKit.h>
//#import <AMapSearchKit/AMapCommonObj.h>
#import <dmlib/dmlib.h>


@interface RCTGeoCoderModule()//<AMapSearchDelegate>
{
  //AMapSearchAPI *_search;
  RCTResponseSenderBlock _success;
  RCTResponseSenderBlock _error;
}

@end

@implementation RCTGeoCoderModule
RCT_EXPORT_MODULE();


-(id)init{
  if(self = [super init]){
  //  _search = [[AMapSearchAPI alloc]init];
   // _search.delegate = self;
  }
  return self;
}

-(void)dealloc{
  //_search = nil;
  _success = nil;
  _error = nil;
}
/**
 逆地理编码
 */
/*

RCT_EXPORT_METHOD(getAddress:(double)lat lng:(double)lng
                  success:(RCTResponseSenderBlock)success
                  error:(RCTResponseSenderBlock)error){
  
  _success = success;
  _error = error;
 
  AMapReGeocodeSearchRequest *regeo = [[AMapReGeocodeSearchRequest alloc] init];
  regeo.location  = [AMapGeoPoint locationWithLatitude:lat longitude:lng];
  regeo.requireExtension            = YES;
 [_search AMapReGoecodeSearch:regeo];
}


- (void)AMapSearchRequest:(id)request didFailWithError:(NSError *)error{
  if(_error){
    _error(@[]);
    _error = nil;
  }
}




- (void)onGeocodeSearchDone:(AMapGeocodeSearchRequest *)request response:(AMapGeocodeSearchResponse *)response{
  if(_success){
    _success = nil;
  }
}


- (void)onReGeocodeSearchDone:(AMapReGeocodeSearchRequest *)request response:(AMapReGeocodeSearchResponse *)response{
  if(_success){
    //兴趣点
    AMapReGeocode* regeocode = response.regeocode;
    NSMutableArray* pois = [[NSMutableArray alloc]initWithCapacity:regeocode.pois.count];
    for (AMapPOI* poi in   regeocode.pois) {
      [pois addObject:@{
                      @"name":poi.name,
                      @"lat": [NSNumber numberWithDouble: poi.location.latitude],
                      @"lng":[NSNumber numberWithDouble: poi.location.longitude],
                      @"address":poi.address,
                      
                        }];
    }
    
    
    AMapAddressComponent* c = regeocode.addressComponent;
    AMapStreetNumber* n = c.streetNumber;
    
    _success(@[@{@"formatAddress":regeocode.formattedAddress,
                 @"address":@{
                     @"province":c.province,
                     @"city":c.city,
                     @"district":c.district,
                     @"township":c.township,
                     @"building":c.building,
                     @"street": @{
                         @"street":n.street,
                         @"number":n.number,
                         @"lat": WRAP_DOUBLE( n.location.latitude ),
                         @"lng":WRAP_DOUBLE( n.location.longitude),
                         @"direction":n.direction,
                         @"distance": WRAP_INTEGER(n.distance)
                         
                         }
                     
                     },
                 @"pois":pois}]);
    
    _success = nil;
  }
}*/

@end
