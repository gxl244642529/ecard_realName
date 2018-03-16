//
//  RCTImagePickerModule.m
//  ecard
//
//  Created by 任雪亮 on 16/7/3.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTImagePickerModule.h"

#import <dmlib/dmlib.h>

#import <ecardlib/ecardlib.h>

@interface RCTImagePickerModule()<CLImageEditorDelegate>

@property (assign,nonatomic) CGFloat quality;
@property (copy,readwrite,nonatomic) RCTResponseSenderBlock callback;
@end


@implementation RCTImagePickerModule
RCT_EXPORT_MODULE();
- (void)imageEditorDidFinishEdittingWithImage:(UIImage*)imageResult{
  NSData *data = UIImageJPEGRepresentation(imageResult, _quality);
  NSString* path = [self persistFile:data];
  
  dispatch_async(dispatch_get_main_queue(), ^{
    [Alert cancelWait];
    if(_callback)_callback(@[path]);
    
  });

  
  
  
}
-(void)imageEditorWillFinishEditingWidthImage:(UIImage*)image{
  
}
RCT_EXPORT_METHOD(select:(NSDictionary*)data  callback:(RCTResponseSenderBlock)callback)
{
 
  self.callback = callback;
  
 // [EditorUtil editImage:<#(UIImage *)#> parent:<#(UIViewController *)#> delegate:(id<CLImageEditorDelegate>) title:<#(NSString *)#> autoEdit:<#(BOOL)#>]
  
  id album = [data objectForKey:@"album"];
  id editWidth = [data objectForKey:@"editWidth"];
  id editHeight =[data objectForKey:@"editHeight"];
  id width = [data objectForKey:@"width"];
   NSInteger maxWidth = 640;
  if(width){
    maxWidth=[width integerValue];
  }
  id height = [data objectForKey:@"height"];
  NSInteger maxHeight = 640;
  if(height){
    maxHeight=[height integerValue];
  }
  id quality = [data objectForKey:@"quality"];
  CGFloat fQuality = [quality floatValue];
  if(fQuality==0){
    fQuality = 1;
  }
  _quality = fQuality;
  BOOL requireAlbum = album ? [album boolValue] : false;
   __weak RCTImagePickerModule* __self = self;
  dispatch_async(dispatch_get_main_queue(), ^{
    if(requireAlbum){
      [ExternalUtil showActionSheet:[DMJobManager sharedInstance].topController completion:^(UIImage * image) {
        [__self onShowImage:image width:maxWidth height:maxHeight quality:fQuality editWidth:editWidth editHeight:editHeight callback:callback];
      }];
      
    }else{
      [ExternalUtil captureImage:[DMJobManager sharedInstance].topController completion:^(UIImage * image) {
         [__self onShowImage:image width:maxWidth height:maxHeight quality:fQuality editWidth:editWidth editHeight:editHeight callback:callback];
      }];
    }
    
  });
  
  
}

-(void)onShowImage:(UIImage*)image width:(NSInteger)maxWidth height:(NSInteger)maxHeight
           quality:(CGFloat)quality editWidth:(id)editWidth editHeight:(id)editHeight callback:(RCTResponseSenderBlock)callback{
  

  
  [Alert wait:@"处理图片..."];
  dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
    
   UIImage* imageResult =[image resize:CGSizeMake(maxWidth, maxHeight)];
    
    if(editWidth){
      dispatch_async(dispatch_get_main_queue(), ^{
        NSInteger w = [editWidth integerValue];
        NSInteger h = [editHeight integerValue];
        [EditorUtil configEditor:
                        (float)h / w
                        minWidth:w minHeight:h];
        [EditorUtil editImage:imageResult parent:[DMJobManager sharedInstance].topController delegate:self title:@"编辑" autoEdit:TRUE];
      });
     
    }else{
      NSData *data = UIImageJPEGRepresentation(imageResult, quality);
      NSString* path = [self persistFile:data];
      
      dispatch_async(dispatch_get_main_queue(), ^{
        [Alert cancelWait];
        callback(@[path]);
        
      });
    }
    
    
  });

  
}


// at the moment it is not possible to upload image by reading PHAsset
// we are saving image and saving it to the tmp location where we are allowed to access image later
- (NSString*) persistFile:(NSData*)data {
  // create temp file
  NSString *filePath = [NSTemporaryDirectory() stringByAppendingString:[[NSUUID UUID] UUIDString]];
  filePath = [filePath stringByAppendingString:@".jpg"];
  
  // save cropped file
  BOOL status = [data writeToFile:filePath atomically:YES];
  if (!status) {
    return nil;
  }
  
  return filePath;
}


@end
