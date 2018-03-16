//
//  DMShareUil.m
//  ecardlib
//
//  Created by 任雪亮 on 17/1/21.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "DMShareUtil.h"

#import "ImageUtil.h"

//腾讯开放平台（对应QQ和QQ空间）SDK头文件

//微信SDK头文件
#import "WXApi.h"

//新浪微博SDK头文件
//新浪微博SDK需要在项目Build Settings中的Other Linker Flags添加"-ObjC"

@interface DMShareWXDelegate : NSObject<WXApiDelegate>


BLOCK_PROPERTY(onSuccess,BOOL);

@end


@implementation DMShareWXDelegate


/*! @brief 收到一个来自微信的请求，第三方应用程序处理完后调用sendResp向微信发送结果
 *
 * 收到一个来自微信的请求，异步处理完成后必须调用sendResp发送处理结果给微信。
 * 可能收到的请求有GetMessageFromWXReq、ShowMessageFromWXReq等。
 * @param req 具体请求内容，是自动释放的
 */
-(void) onReq:(BaseReq*)req{
    
}



/*! @brief 发送一个sendReq后，收到微信的回应
 *
 * 收到一个来自微信的处理结果。调用一次sendReq后会收到onResp。
 * 可能收到的处理结果有SendMessageToWXResp、SendAuthResp等。
 * @param resp具体的回应内容，是自动释放的
 */
-(void) onResp:(BaseResp*)resp{
    //把返回的类型转换成与发送时相对于的返回类型,这里为SendMessageToWXResp
    if([resp isKindOfClass:[SendMessageToWXResp class]]){
        SendMessageToWXResp *sendResp = (SendMessageToWXResp *)resp;
        if(sendResp.errCode==0){
            if(self.onSuccess!=nil)self.onSuccess(YES);
        }else{
            if(self.onSuccess!=nil)self.onSuccess(NO);

        }
        
    }
}

@end
DMShareWXDelegate* g_DMShareWXDelegate;
@implementation DMShareUtil
+(void)initSDK{
   
}
+(void)initialize{
    
    
    
     g_DMShareWXDelegate = [[DMShareWXDelegate alloc]init];
    /**
     *  设置ShareSDK的appKey，如果尚未在ShareSDK官网注册过App，请移步到http://mob.com/login 登录后台进行应用注册
     *  在将生成的AppKey传入到此方法中。
     *  方法中的第二个第三个参数为需要连接社交平台SDK时触发，
     *  在此事件中写入连接代码。第四个参数则为配置本地社交平台时触发，根据返回的平台类型来配置平台信息。
     *  如果您使用的时服务端托管平台信息时，第二、四项参数可以传入nil，第三项参数则根据服务端托管平台来决定要连接的社交SDK。
     */
    }
+(void)share:(NSDictionary*)map onSuccess:BLOCK_PARAM(onSuccess,BOOL){
    NSInteger type = [[map valueForKey:@"type"] integerValue];
    NSString* title = [map objectForKey:@"title"];
    NSString* url = [map objectForKey:@"url"];

    NSString* content = [map objectForKey:@"content"];
     NSString* imageUrl = [map objectForKey:@"imageUrl"];
    //创建发送对象实例
    SendMessageToWXReq *sendReq = [[SendMessageToWXReq alloc] init];
    sendReq.bText = NO;//不使用文本信息
    sendReq.scene = type;// 0;//0 = 好友列表 1 = 朋友圈 2 = 收藏
    
    //创建分享内容对象
    WXMediaMessage *urlMessage = [WXMediaMessage message];
    urlMessage.title = title;//分享标题
    urlMessage.description = content;//分享描述
    
    [urlMessage setThumbImage:[UIImage imageNamed:@"share.png"]];//分享图片,使用SDK的setThumbImage方法可压缩图片大小
    
    //创建多媒体对象
    WXWebpageObject *webObj = [WXWebpageObject object];
    webObj.webpageUrl = url;//分享链接
    
  //  NSData* data= UIImageJPEGRepresentation(image,1);
    
   // webObj.imageData =   data ;//分享链接
    
    //完成发送对象实例
    urlMessage.mediaObject = webObj;
    sendReq.message = urlMessage;
    
    //发送分享信息
    [WXApi sendReq:sendReq];

    g_DMShareWXDelegate.onSuccess = onSuccess;
    
}

+(BOOL)handleOpenUrl:(NSURL *)url{
    
    return [WXApi handleOpenURL:url delegate:g_DMShareWXDelegate];
    
}
+(void)shareWX:(NSString*)content title:(NSString*)title image:(UIImage*)image type:(DMShareType)type{
    
     UIImage* thumbImage = [ImageUtil scaleImage:image toSize:CGSizeMake(100,100)];
    
    //创建发送对象实例
    SendMessageToWXReq *sendReq = [[SendMessageToWXReq alloc] init];
    sendReq.bText = NO;//不使用文本信息
    sendReq.scene =type ==ShareType_WXSingle ? 0 : 1;// 0;//0 = 好友列表 1 = 朋友圈 2 = 收藏
    
    //创建分享内容对象
    WXMediaMessage *urlMessage = [WXMediaMessage message];
    urlMessage.title = title;//分享标题
    urlMessage.description = content;//分享描述
    [urlMessage setThumbImage:thumbImage];//分享图片,使用SDK的setThumbImage方法可压缩图片大小
    
    //创建多媒体对象
    WXImageObject *webObj = [WXImageObject object];
    
    
    NSData* data= UIImageJPEGRepresentation(image,1);
    
    webObj.imageData =   data ;//分享链接
    
    //完成发送对象实例
    urlMessage.mediaObject = webObj;
    sendReq.message = urlMessage;
    
    //发送分享信息
    [WXApi sendReq:sendReq];

}

+(void)share:(NSString*)content title:(NSString*)title  parent:(UIView*)parent type:(DMShareType)type{
  
    UIImage* image = [parent takeSnapshot];
    
    switch (type) {
        case ShareType_WXSingle:
        case ShareType_WXFrend:
            g_DMShareWXDelegate.onSuccess = ^(BOOL success){
                if(success){
                    [Alert alert:@"分享成功"];
                }
            };
            [self shareWX:content title:title image:image type:type];
            break;
        default:
            break;
    }
   
 }

@end
