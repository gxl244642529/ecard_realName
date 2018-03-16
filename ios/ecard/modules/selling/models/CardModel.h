//
//  CardModel.h
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CommonMacro.h"
#import "SellingModel.h"
#import "FileArrayJsonTask.h"
#import <DMLib/dmlib.h>
//
typedef enum _ConfirmType{
    ConfirmType_Direct,//直接购买下单
    ConfirmType_Cart//通过购物车下单
}ConfirmType;


/**
 *  我的diy数据
 */
@interface DiyVo:CartVo<IJsonToObject>


/**
 *  图片路径
 */
@property (nonatomic,retain) NSString* image;

//反面
@property (nonatomic,retain) NSString* fImage;

/**
 *  获取图片路径
 *
 *  @return <#return value description#>
 */


-(void)saveImage:(UIImage*)aImage;

-(void)saveThumb:(UIImage*)aImage;

-(BOOL)hasImage;

-(UIImage*)getImage;


@end



@interface CartModel : NSObject


DECLARE_SHARED_INSTANCE_DIRECT(CartModel)



-(void)addToCart:(SCardListVo*)data count:(NSInteger)count recharge:(NSInteger)recharge successListener:(void (^)())successListener;


-(void)addToCart:(DiyVo*)data count:(NSInteger)count recharge:(NSInteger)recharge file:(NSString*)file successListener:(void (^)())successListener;

-(void)getList:(NSObject<IArrayRequestResult>*)listener;

-(ArrayJsonTask*)listTask;
-(void)removeObject:(id)object;
+(NSString*)encodeURL:(NSString *)string;
+(void)toJson:(CartVo*)data task:(JsonTask*)task;
+(NSString*)getImageBase64String:(NSString*)path;
+(NSDictionary*)toJson:(CartVo*)data;

-(void)createListTask;
@property (nonatomic,retain) NSArray* list;
-(void)notifyListHasChanged;

-(void)addObserver:(NSObject<IArrayRequestResult>*)listener;
@end



