//
//  RCTQrView.m
//  ecard
//
//  Created by 任雪亮 on 16/12/5.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTQrView.h"

@interface RCTQrView()
{
  NSString* _content;
}

@end


@implementation RCTQrView
-(void)setContent:(NSString*)content{
  if(!content || content.length==0){
    _content = nil;
    [self setImage:nil];
    return;
  }
  
  if(![content isEqualToString:_content])
  {
    
    
    // 1.创建过滤器
    CIFilter *filter = [CIFilter filterWithName:@"CIQRCodeGenerator"];
    
    // 2.恢复默认
    [filter setDefaults];
    
    // 3.给过滤器添加数据(正则表达式/账号和密码)
    NSString *dataString = content;
    NSData *data = [dataString dataUsingEncoding:NSUTF8StringEncoding];
    [filter setValue:data forKeyPath:@"inputMessage"];
    
    // 4.获取输出的二维码
    CIImage *outputImage = [filter outputImage];
    
    //因为生成的二维码模糊，所以通过createNonInterpolatedUIImageFormCIImage:outputImage来获得高清的二维码图片
    
    // 5.显示二维码
    self.image = [self createNonInterpolatedUIImageFormCIImage:outputImage withSize:500];
    
    _content = content;
  }
}

/**
 *  根据CIImage生成指定大小的UIImage
 *
 *  @param image CIImage
 *  @param size  图片宽度
 */
- (UIImage *)createNonInterpolatedUIImageFormCIImage:(CIImage *)image withSize:(CGFloat) size
{
  CGRect extent = CGRectIntegral(image.extent);
  CGFloat scale = MIN(size/CGRectGetWidth(extent), size/CGRectGetHeight(extent));
  
  // 1.创建bitmap;
  size_t width = CGRectGetWidth(extent) * scale;
  size_t height = CGRectGetHeight(extent) * scale;
  CGColorSpaceRef cs = CGColorSpaceCreateDeviceGray();
  CGContextRef bitmapRef = CGBitmapContextCreate(nil, width, height, 8, 0, cs, (CGBitmapInfo)kCGImageAlphaNone);
  CIContext *context = [CIContext contextWithOptions:nil];
  CGImageRef bitmapImage = [context createCGImage:image fromRect:extent];
  CGContextSetInterpolationQuality(bitmapRef, kCGInterpolationNone);
  CGContextScaleCTM(bitmapRef, scale, scale);
  CGContextDrawImage(bitmapRef, extent, bitmapImage);
  
  // 2.保存bitmap到图片
  CGImageRef scaledImage = CGBitmapContextCreateImage(bitmapRef);
  CGContextRelease(bitmapRef);
  CGImageRelease(bitmapImage);
  return [UIImage imageWithCGImage:scaledImage];
}

@end
