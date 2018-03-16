//
//  ScanViewController.m
//  ecard
//
//  Created by randy ren on 15/4/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ScanViewController.h"
#import "ScanViewWrapper.h"

#import "ECardBundle.h"

//设备宽/高/坐标
#define kDeviceWidth [UIScreen mainScreen].bounds.size.width
#define KDeviceHeight [UIScreen mainScreen].bounds.size.height
#define KDeviceFrame [UIScreen mainScreen].bounds

#define kLineMinY (KDeviceHeight / 2 - 100)
static const float kReaderViewWidth = 200;
static const float kReaderViewHeight = 200;


int REQUEST_SCAN = 100;

@interface ScanViewController(){
    AVCaptureDevice *device;
    AVCaptureMetadataOutput *output;
    AVCaptureSession *session;
}

@property (nonatomic, retain) AVCaptureVideoPreviewLayer *qrVideoPreviewLayer;//读取
@property (nonatomic, retain) UIImageView *line;//交互线
@property (nonatomic, retain) NSTimer *lineTimer;//交互线控制
@property (nonatomic,weak) NSObject<SCanDelegate>* listener;
@end


@implementation ScanViewController
-(void)resumeScan{
    if(![session isRunning]){
        [session startRunning];
    }
}
-(id)initWithListener:(NSObject<SCanDelegate>*)listener{
    if(self = [super init]){
        self.listener = listener;
    }
    return self;
}
- (void)dealloc
{
    device = nil;
    output = NULL;
    
    if (session && [session isRunning] ) {
        [session stopRunning];
        session = nil;
    }
    
    self.qrVideoPreviewLayer = nil;
    
    self.line = nil;
    
    if (self.lineTimer)
    {
        [self.lineTimer invalidate];
        self.lineTimer = nil;
    }
}

-(void)viewDidLoad{
    [super viewDidLoad];
    [self.view setBackgroundColor:RGB_WHITE(f2)];
    [self setTitle:@"扫描绑定e通卡"];
    CGRect rect = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
    NSBundle* bundle = CREATE_BUNDLE(myecardbundle.bundle);
    NSArray* views= [bundle loadNibNamed:@"ScanViewWrapper" owner:nil options:nil];
    ScanViewWrapper* wrapper =views[0];
    wrapper.frame = rect;
    [self.view addSubview:wrapper];
    dispatch_async(GLOBAL_QUEUE, ^{
        dispatch_async(dispatch_get_main_queue(), ^{
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
            [output setMetadataObjectTypes:@[AVMetadataObjectTypeEAN13Code]];
            
            //设置预览图层
            AVCaptureVideoPreviewLayer *preview = [AVCaptureVideoPreviewLayer layerWithSession:session];
            //设置preview图层的属性
            [preview setVideoGravity:AVLayerVideoGravityResizeAspectFill];
            //设置preview图层的大小
            preview.frame = self.view.layer.bounds;
            //将图层添加到视图的图层
            [self.view.layer insertSublayer:preview atIndex:0];
            self.qrVideoPreviewLayer = preview;
            

        });
        
    });
    
    
}
- (CGRect)getReaderViewBoundsWithSize:(CGSize)asize
{
    return CGRectMake(kLineMinY / KDeviceHeight, ((kDeviceWidth - asize.width) / 2.0) / kDeviceWidth, asize.height / KDeviceHeight, asize.width / kDeviceWidth);
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if(session && ![session isRunning]){
        
        [session startRunning];
    }
    
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    if(session && [session isRunning]){
        [session stopRunning];
    }
    
}



#pragma mark -
#pragma mark 输出代理方法

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
            [session stopRunning];
            [self.listener onScan:self data:obj.stringValue];
        }
    }
}


#pragma mark -

@end
