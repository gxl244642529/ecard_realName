//
//  ExternalUtil.m
//  ecard
//
//  Created by randy ren on 15/3/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ExternalUtil.h"

@interface ActionSheetDelegate : NSObject<UIActionSheetDelegate>

@property (nonatomic,weak) UIViewController* parent;
@property (nonatomic,copy,readwrite) void(^completion)(UIImage* image);

@end

@interface ImagePicker : NSObject<UINavigationControllerDelegate,UIImagePickerControllerDelegate>

@property (nonatomic,weak) UIViewController* parent;
@property (nonatomic,copy,readwrite) void(^completion)(UIImage* image);
@end


static ImagePicker* g_imagePicker;
static ActionSheetDelegate* g_actionSheetDelegate;

@implementation ImagePicker




-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    
    [self.parent.navigationController dismissViewControllerAnimated:NO completion:^{
        [picker removeFromParentViewController];
    }];
    
    
    UIImage *image = [info objectForKey:@"UIImagePickerControllerOriginalImage"];//获取图片
   
    
    self.completion(image);
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self.parent.navigationController dismissViewControllerAnimated:NO completion:^{
        [picker removeFromParentViewController];
    }];
}

@end

@implementation ExternalUtil


+(void)destroy{
    g_imagePicker = nil;
    g_actionSheetDelegate = nil;
}

+(void)showActionSheet:(UIViewController*)parent  completion:(void (^)(UIImage*))completion{
    
    if(!g_actionSheetDelegate){
        g_actionSheetDelegate = [[ActionSheetDelegate alloc]init];
    }
    g_actionSheetDelegate.parent = parent;
    g_actionSheetDelegate.completion = completion;
    
    
    UIActionSheet *sheet = [[UIActionSheet alloc]initWithTitle:@"选择图片" delegate:g_actionSheetDelegate cancelButtonTitle:@"关闭" destructiveButtonTitle:nil otherButtonTitles:@"相册",@"拍照", nil];//关闭按钮在最后
    
    sheet.actionSheetStyle = UIActionSheetStyleAutomatic;//样式
    [sheet showInView:parent.view];//显示样式

}

+(ImagePicker*)getImagePicker:(UIViewController*)parent completion:(void (^)(UIImage*))completion{
    if(!g_imagePicker){
        g_imagePicker = [[ImagePicker alloc]init];
    }
    g_imagePicker.parent = parent;
    g_imagePicker.completion = completion;
    return g_imagePicker;
}


+(void)selectFromAlbum:(UIViewController*)parent completion:(void (^)(UIImage*))completion{
    //图像选取
    UIImagePickerController *imagePicker;
    imagePicker = [[UIImagePickerController alloc] init];//图像选取器
    imagePicker.delegate = [ExternalUtil getImagePicker:parent completion:completion];
    imagePicker.allowsEditing = NO;
    imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;//打开相册
    imagePicker.modalTransitionStyle = UIModalTransitionStyleCoverVertical;//过渡类型,有四种
    [parent.navigationController presentViewController:imagePicker animated:YES completion:NULL];
}

+(void)captureImage:(UIViewController*)parent completion:(void (^)(UIImage*))completion{
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        
        //图像选取
        UIImagePickerController *imagePicker;
        imagePicker = [[UIImagePickerController alloc] init];
        imagePicker.delegate = [ExternalUtil getImagePicker:parent completion:completion];
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;//照片来源为相机
        imagePicker.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
        
        [parent.navigationController presentViewController:imagePicker animated:YES completion:NULL];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"提示" message:@"该设备没有照相机" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
    }

}

@end



///
@implementation ActionSheetDelegate

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    switch (buttonIndex) {
        case 0:
        {
            [ExternalUtil selectFromAlbum:self.parent completion:self.completion];
            
        }
            break;
        case 1:
        {
            [ExternalUtil captureImage:self.parent completion:self.completion];
        }
            break;
    }

}

@end
