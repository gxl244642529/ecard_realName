//
//  CardModel.m
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CardModel.h"
#import "ECardTaskManager.h"
#import <DMLib/dmlib.h>
#import <ecardlib/ecardlib.h>
////////////////////////////////////////////////////////////////

@implementation DiyVo

-(void)dealloc{
    self.fImage = NULL;
    self.image = NULL;
}
-(BOOL)hasImage{
    NSString* path = self.image;
    if(path){
        if([[NSFileManager defaultManager]fileExistsAtPath:path]){
            return YES;
        }else{
            self.image = nil;
        }
    }

    return NO;
}
-(UIImage*)getImage{
    if([self hasImage]){
        return [UIImage imageWithContentsOfFile: self.image];
    }
    return nil;
}
-(NSDictionary*)toJsonValue
{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    [dic setValue:[NSNumber numberWithInteger:self.ID] forKey:@"ID"];
    [dic setValue:self.cartID forKey:@"cartID"];
    [dic setValue:self.title forKey:@"title"];
    [dic setValue:[NSNumber numberWithInteger:self.price] forKey:@"price"];
    [dic setValue:self.thumb forKey:@"thumb"];
    [dic setValue:[NSNumber numberWithInteger:self.count] forKey:@"count"];
    [dic setValue:[NSNumber numberWithInteger:self.recharge] forKey:@"recharge"];
    [dic setValue:[NSNumber numberWithInt:self.imageID] forKey:@"imageID"];
    [dic setValue:[NSNumber numberWithInt:self.store] forKey:@"store"];
    [dic setValue:self.image forKey:@"image"];
    [dic setValue:self.fImage forKey:@"fImage"];
    
    return dic;
}

-(id)init{
    if(self=[super init]){
       // self.ID = [NSString stringWithFormat:@"%lf",[[NSDate date]timeIntervalSince1970]];
    }
    return  self;
}

-(void)saveImage:(UIImage*)aImage{
    NSString* aPath = [self getOrgImagePath];
    NSData* imgData =  UIImagePNGRepresentation(aImage); // UIImageJPEGRepresentation(aImage,1);
    [imgData writeToFile:aPath atomically:YES];
    self.image = aPath;//
}
/**
 *
 */
-(void)saveThumb:(UIImage*)aImage{
    
    NSString* aPath = [self getThumbImagePath];
    UIImage* thumbImage = [ImageUtil createThumbImage:aImage width:[UIScreen mainScreen].bounds.size.width/2];
    
    NSData* imgData = UIImagePNGRepresentation(thumbImage);
    [imgData writeToFile:aPath atomically:YES];
    self.thumb = aPath;//
}
-(NSString*)getImagePath{
    return [NSString stringWithFormat:@"%@/image_%lf.jpg",[CacheUtil getPath:@"image"],[[NSDate date] timeIntervalSince1970]  ];
}
-(NSString*)getOrgImagePath{
    return [NSString stringWithFormat:@"%@/org_%lf.jpg",[CacheUtil getPath:@"image"],[[NSDate date] timeIntervalSince1970]];
}
-(NSString*)getThumbImagePath{
    return [NSString stringWithFormat:@"%@/thumb_%lf.jpg",[CacheUtil getPath:@"image"],[[NSDate date] timeIntervalSince1970]];
}
-(NSMutableDictionary*)toJson{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    [dic setValue:[NSNumber numberWithInteger:self.ID] forKey:@"id"];
    [dic setValue:[NSNumber numberWithInteger:self.count] forKey:@"count"];
    [dic setValue:[NSNumber numberWithInteger:self.recharge] forKey:@"recharge"];
    return dic;
}

@end
////////////////////////////////////////////////////////////////
@interface CartModel()
{
    ArrayJsonTask* _listTask;
    ObjectJsonTask* _cartTask;
    WeakSet* _weakSet;
}

@end
@implementation CartModel
IMPLEMENT_SHARED_INSTANCE_DIRECT(CartModel)
-(void)removeObject:(id)object{
    [_listTask.array removeObject:object];
}
-(id)init{
    if(self = [super init]){
        _weakSet = [[WeakSet alloc]init];
    }
    return self;
}

-(void)dealloc{
    _listTask = nil;
    _cartTask = nil;
    _weakSet = nil;
    self.list = nil;
}
-(ArrayJsonTask*)listTask{
    return _listTask;
}


-(void)addObserver:(NSObject<IArrayRequestResult>*)listener{
    if(![_weakSet containsObject:listener]){
        [_weakSet addObject:listener];
    }
}
-(void)getList:(NSObject<IArrayRequestResult>*)listener{
    [self addObserver:listener];
    [self createListTask];
    [_listTask execute];
}

-(void)addToCart:(DiyVo*)data count:(NSInteger)count recharge:(NSInteger)recharge file:(NSString*)file successListener:(void (^)())successListener{
    [self createAddCartTask:successListener];
    [CartModel toJson:data task:_cartTask];
    [_cartTask execute];
}
+(NSString*)encodeURL:(NSString *)string
{
    NSString *newString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"),kCFStringEncodingUTF8));
    if (newString) {
        return newString;
    }
    return @"";
}

-(void)addToCart:(SCardListVo*)data count:(NSInteger)count recharge:(NSInteger)recharge successListener:(void (^)())successListener{
    [self createAddCartTask:successListener];
    [_cartTask put:@"id" value:[NSNumber numberWithInteger:data.ID]];
    [_cartTask put:@"count" value:[NSNumber numberWithInteger:count]];
    [_cartTask put:@"recharge" value:[NSNumber numberWithInteger:recharge]];
    [_cartTask execute];

}


+(NSDictionary*)toJson:(CartVo*)data{
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    [dic setValue:[NSNumber numberWithInteger:data.ID] forKey:@"id"];
    [dic setValue:[NSNumber numberWithInteger:data.count] forKey:@"count"];
    [dic setValue:[NSNumber numberWithInteger:data.recharge] forKey:@"recharge"];
    if(data.imageID>0){
        [dic setValue:[NSNumber numberWithInt:data.imageID] forKey:@"image_id"];
    }
    if([data isKindOfClass:[DiyVo class]]){
        
        DiyVo* diyVo = (DiyVo*)data;
        NSString* file = diyVo.image;
        [dic setValue:[CartModel getImageBase64String:file] forKey:@"file"];
    }else{
        //如果是购物车中的
        if(data.cartID){
            dic[@"cart_id"]=data.cartID;
        }
    }

    return dic;
}


+(NSString*)getImageBase64String:(NSString*)file{
    UIImage* image = [UIImage imageWithContentsOfFile:file];
    if(image.size.width> CARD_WIDTH){
        image = [image resize:CGSizeMake(CARD_WIDTH, CARD_HEIGHT)];
    }
    NSData* imgData = UIImageJPEGRepresentation(image,1);
    NSString* base64string = [imgData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    
    return [CartModel encodeURL:base64string];
}

+(void)toJson:(CartVo*)data task:(JsonTask*)task{
    
    [task put:@"id" value:[NSNumber numberWithInteger:data.ID]];
    [task put:@"count" value:[NSNumber numberWithInteger:data.count]];
    [task put:@"recharge" value:[NSNumber numberWithInteger:data.recharge]];
    
    if([data isKindOfClass:[DiyVo class]]){
        
        DiyVo* diyVo = (DiyVo*)data;
        NSString* file = diyVo.image;
        [task put:@"file" value:[CartModel getImageBase64String:file]];
        
    }else{
       // [task put:@"cart_id" value:data.cartID];
    }
    
}




#pragma private


-(void)createAddCartTask:(void (^)())successListener{
    if(!_cartTask){
        _cartTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_cart_add" cachePolicy:DMCachePolicy_NoCache];
        __weak ArrayJsonTask* __listTask = _listTask;
        __weak CartModel* __self = self;
        _cartTask.successListener = ^(id result){
            [__listTask clearCache];
            __self.list = nil;
            [__listTask reload];
            [SVProgressHUD showSuccessWithStatus:@"加入购物车成功"];
            successListener();
        };
        _cartTask.errorListener = ^(NSString* error,BOOL isNetworkError){
            [SVProgressHUD showErrorWithStatus:error];
        };
        [_cartTask setWaitMessage:@"请稍等..."];
    }else{
        [_cartTask clearParam];
    }
}

-(void)createListTask{
    if(!_listTask){
        _listTask = [[ECardTaskManager sharedInstance]createArrayJsonTask:@"s_cart_list" cachePolicy:DMCachePolicy_TimeLimit isPrivate:YES];
        __weak CartModel* __self = self;
       [_listTask setClass:[CartVo class]];
        _listTask.successListener = ^(NSArray* result,NSInteger position,BOOL isLastPage){
            __self.list = result;
            [__self notifyListHasChanged];
        };
        _listTask.errorListener = ^(NSString* error,BOOL isNetworkError){
           //加载失败
            [SVProgressHUD showErrorWithStatus:error];
        };
        [_listTask setPosition:Start_Position];
    }
}


-(void)notifyListHasChanged{
    for (NSObject<IArrayRequestResult>* listener in _weakSet.allObjects) {
        [listener task:_listTask result:self.list position:Start_Position isLastPage:YES];
    }
}


@end

////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////

//////////////
