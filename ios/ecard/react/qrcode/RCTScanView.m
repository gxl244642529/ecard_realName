//
//  RCTScanView.m
//  ebusiness
//
//  Created by 任雪亮 on 16/11/11.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTScanView.h"
#import <dmlib/dmlib.h>
@interface RCTScanView (){
  AVCaptureDevice *device;
  AVCaptureMetadataOutput *output;
  AVCaptureSession *session;
}
@property (nonatomic, retain) AVCaptureVideoPreviewLayer *qrVideoPreviewLayer;//读取
@end
@implementation RCTScanView

-(id)init{
  if(self = [super init]){
  }
  return self;
}


-(void)removeFromSuperview{
   [self stopSession];
  [super removeFromSuperview];
}


-(void)layoutSubviews{
  [super layoutSubviews];
  self.qrVideoPreviewLayer.frame = self.bounds;
}

-(void)startSession{
  dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
    dispatch_async(dispatch_get_main_queue(), ^{
      if(!session){
        
        device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
        //摄像头判断
        NSError *error = nil;
        AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
        if (error)
        {
          NSLog(@"没有摄像头-%@", error.localizedDescription);
          dispatch_async(dispatch_get_main_queue(), ^{
            [Alert alert:@"本设备没有摄像头或没有打开摄像头权限"];
          });
          device = nil;
          return;
        }
        //设置输出(Metadata元数据)
        output = [[AVCaptureMetadataOutput alloc] init];
        //设置输出的代理
        //使用主线程队列，相应比较同步，使用其他队列，相应不同步，容易让用户产生不好的体验
        [output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
        [output setRectOfInterest:[self getReaderViewBoundsWithSize:CGSizeMake(kReaderViewWidth, kReaderViewHeight)]];
        //拍摄会话
        session = [[AVCaptureSession alloc] init];
        // 读取质量，质量越高，可读取小尺寸的二维码
        if ([session canSetSessionPreset:AVCaptureSessionPreset1920x1080])
        {
          [session setSessionPreset:AVCaptureSessionPreset1920x1080];
        }
        else if ([session canSetSessionPreset:AVCaptureSessionPreset1280x720])
        {
          [session setSessionPreset:AVCaptureSessionPreset1280x720];
        }
        else
        {
          [session setSessionPreset:AVCaptureSessionPresetPhoto];
        }
        
        if ([session canAddInput:input])
        {
          [session addInput:input];
        }
        
        if ([session canAddOutput:output])
        {
          [session addOutput:output];
        }
        
        [session startRunning];
        
        //设置输出的格式
        //一定要先设置会话的输出为output之后，再指定输出的元数据类型
        [output setMetadataObjectTypes:@[AVMetadataObjectTypeQRCode]];
        
        //设置预览图层
        AVCaptureVideoPreviewLayer *preview = [AVCaptureVideoPreviewLayer layerWithSession:session];
        //设置preview图层的属性
        [preview setVideoGravity:AVLayerVideoGravityResizeAspectFill];
        //设置preview图层的大小
        preview.frame = CGRectMake(0, 0, 300, 300);
        //将图层添加到视图的图层
        [self.layer insertSublayer:preview atIndex:0];
        self.qrVideoPreviewLayer = preview;
      }else{
        if(![session isRunning]){
          [session startRunning];
        }
      }
    });
    

  });
}



-(void)stopSession{
  dispatch_async(dispatch_get_main_queue(), ^{
    if(session){
      if([session isRunning]){
        [session stopRunning];
      }
      
      device = nil;
      output = NULL;
      
      [self.qrVideoPreviewLayer removeFromSuperlayer];
      self.qrVideoPreviewLayer = nil;
      session = nil;
    }

  });
 }

//此方法是在识别到QRCode，并且完成转换
//如果QRCode的内容越大，转换需要的时间就越长
- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
  //扫描结果
  if (metadataObjects.count > 0)
  {
    //  [self stopSYQRCodeReading];
    
    AVMetadataMachineReadableCodeObject *obj = metadataObjects[0];
    
    if (obj.stringValue && ![obj.stringValue isEqualToString:@""] && obj.stringValue.length > 0)
    {
      self.onBarCodeRead(@{
                           @"code":obj.stringValue
                           });
      [session stopRunning];
    }
  }
}



- (CGRect)getReaderViewBoundsWithSize:(CGSize)asize
{
  return CGRectMake(kLineMinY / KDeviceHeight, ((kDeviceWidth - asize.width) / 2.0) / kDeviceWidth, asize.height / KDeviceHeight, asize.width / kDeviceWidth);
}

@end
