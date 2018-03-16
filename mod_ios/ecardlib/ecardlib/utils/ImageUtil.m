//
//  ImageUtil.m
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ImageUtil.h"



//
//  RootViewController.h
//  pictureProcess
//
//  Created by Ibokan on 12-9-7.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//



//LOMO
float colormatrix_lomo[] = {
    1.7f,  0.1f, 0.1f, 0, -73.1f,
    0,  1.7f, 0.1f, 0, -73.1f,
    0,  0.1f, 1.6f, 0, -73.1f,
    0,  0, 0, 1.0f, 0 };

//黑白
float colormatrix_heibai[] = {
    0.8f,  1.6f, 0.2f, 0, -163.9f,
    0.8f,  1.6f, 0.2f, 0, -163.9f,
    0.8f,  1.6f, 0.2f, 0, -163.9f,
    0,  0, 0, 1.0f, 0 };
//复古
float colormatrix_huajiu[] = {
    0.2f,0.5f, 0.1f, 0, 40.8f,
    0.2f, 0.5f, 0.1f, 0, 40.8f,
    0.2f,0.5f, 0.1f, 0, 40.8f,
    0, 0, 0, 1, 0 };

//哥特
float colormatrix_gete[] = {
    1.9f,-0.3f, -0.2f, 0,-87.0f,
    -0.2f, 1.7f, -0.1f, 0, -87.0f,
    -0.1f,-0.6f, 2.0f, 0, -87.0f,
    0, 0, 0, 1.0f, 0 };

//锐化
 float colormatrix_ruise[] = {
    4.8f,-1.0f, -0.1f, 0,-388.4f,
    -0.5f,4.4f, -0.1f, 0,-388.4f,
    -0.5f,-1.0f, 5.2f, 0,-388.4f,
    0, 0, 0, 1.0f, 0 };

//淡雅
 float colormatrix_danya[] = {
    0.6f,0.3f, 0.1f, 0,73.3f,
    0.2f,0.7f, 0.1f, 0,73.3f,
    0.2f,0.3f, 0.4f, 0,73.3f,
    0, 0, 0, 1.0f, 0 };


//酒红
 float colormatrix_jiuhong[] = {
    1.2f,0.0f, 0.0f, 0.0f,0.0f,
    0.0f,0.9f, 0.0f, 0.0f,0.0f,
    0.0f,0.0f, 0.8f, 0.0f,0.0f,
    0, 0, 0, 1.0f, 0 };

//清宁
 float colormatrix_qingning[] = {
    0.9f, 0, 0, 0, 0,
    0, 1.1f,0, 0, 0,
    0, 0, 0.9f, 0, 0,
    0, 0, 0, 1.0f, 0 };

//浪漫
 float colormatrix_langman[] = {
    0.9f, 0, 0, 0, 63.0f,
    0, 0.9f,0, 0, 63.0f,
    0, 0, 0.9f, 0, 63.0f,
    0, 0, 0, 1.0f, 0 };

//光晕
 float colormatrix_guangyun[] = {
    0.9f, 0, 0,  0, 64.9f,
    0, 0.9f,0,  0, 64.9f,
    0, 0, 0.9f,  0, 64.9f,
    0, 0, 0, 1.0f, 0 };

//蓝调
 float colormatrix_landiao[] = {
    2.1f, -1.4f, 0.6f, 0.0f, -31.0f,
    -0.3f, 2.0f, -0.3f, 0.0f, -31.0f,
    -1.1f, -0.2f, 2.6f, 0.0f, -31.0f,
    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
};

//梦幻
 float colormatrix_menghuan[] = {
    0.8f, 0.3f, 0.1f, 0.0f, 46.5f,
    0.1f, 0.9f, 0.0f, 0.0f, 46.5f,
    0.1f, 0.3f, 0.7f, 0.0f, 46.5f,
    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
};



//夜色
 float colormatrix_yese[] = {
    1.0f, 0.0f, 0.0f, 0.0f, -66.6f,
    0.0f, 1.1f, 0.0f, 0.0f, -66.6f,
    0.0f, 0.0f, 1.0f, 0.0f, -66.6f,
    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
};


 float* image_filters[] = {
    colormatrix_lomo,
    colormatrix_heibai,
    colormatrix_huajiu,
    colormatrix_gete,
    colormatrix_ruise,
    colormatrix_danya,
    colormatrix_jiuhong,
    colormatrix_qingning,
    colormatrix_langman,
    colormatrix_guangyun,
    colormatrix_landiao,
    colormatrix_menghuan,
    colormatrix_yese
};

 NSString* g_filter_names[]={
    @"原图",
    @"LOMO",
    @"黑白",
    @"复古",
    @"哥特",
    @"锐化",
    @"淡雅",
    @"酒红",
    @"清宁",
    @"浪漫",
    @"光晕",
    @"蓝调",
    @"梦幻",
    @"夜色"
};

@implementation ImageUtil


static CGContextRef CreateRGBABitmapContext (CGImageRef inImage)// 返回一个使用RGBA通道的位图上下文
{
	CGContextRef context = NULL;
	CGColorSpaceRef colorSpace;
	void *bitmapData; //内存空间的指针，该内存空间的大小等于图像使用RGB通道所占用的字节数。
	size_t bitmapByteCount;
	size_t bitmapBytesPerRow;
    
	size_t pixelsWide = CGImageGetWidth(inImage); //获取横向的像素点的个数
	size_t pixelsHigh = CGImageGetHeight(inImage); //纵向
    
	bitmapBytesPerRow	= (pixelsWide * 4); //每一行的像素点占用的字节数，每个像素点的ARGB四个通道各占8个bit(0-255)的空间
	bitmapByteCount	= (bitmapBytesPerRow * pixelsHigh); //计算整张图占用的字节数
    
	colorSpace = CGColorSpaceCreateDeviceRGB();//创建依赖于设备的RGB通道
	
	bitmapData = malloc(bitmapByteCount); //分配足够容纳图片字节数的内存空间
    
	context = CGBitmapContextCreate (bitmapData,
                                     pixelsWide,
                                     pixelsHigh,
                                     8, bitmapBytesPerRow,
                                     colorSpace,
                                (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    //创建CoreGraphic的图形上下文，该上下文描述了bitmaData指向的内存空间需要绘制的图像的一些绘制参数
    
	CGColorSpaceRelease( colorSpace );
    //Core Foundation中通过含有Create、Alloc的方法名字创建的指针，需要使用CFRelease()函数释放
    
	return context;
}

static unsigned char *RequestImagePixelData(UIImage *inImage)
// 返回一个指针，该指针指向一个数组，数组中的每四个元素都是图像上的一个像素点的RGBA的数值(0-255)，用无符号的char是因为它正好的取值范围就是0-255
{
	CGImageRef img = [inImage CGImage];
	CGSize size = [inImage size];
    
	CGContextRef cgctx = CreateRGBABitmapContext(img); //使用上面的函数创建上下文
	
	CGRect rect = {{0,0},{size.width, size.height}};
    
	CGContextDrawImage(cgctx, rect, img); //将目标图像绘制到指定的上下文，实际为上下文内的bitmapData。
	unsigned char *data = CGBitmapContextGetData (cgctx);
    
	CGContextRelease(cgctx);//释放上面的函数创建的上下文
	return data;
}

static void changeRGBA(int *red,int *green,int *blue,int *alpha, const float* f)//修改RGB的值
{
    int redV = *red;
    int greenV = *green;
    int blueV = *blue;
    int alphaV = *alpha;
    
    *red = f[0] * redV + f[1] * greenV + f[2] * blueV + f[3] * alphaV + f[4];
    *green = f[0+5] * redV + f[1+5] * greenV + f[2+5] * blueV + f[3+5] * alphaV + f[4+5];
    *blue = f[0+5*2] * redV + f[1+5*2] * greenV + f[2+5*2] * blueV + f[3+5*2] * alphaV + f[4+5*2];
    *alpha = f[0+5*3] * redV + f[1+5*3] * greenV + f[2+5*3] * blueV + f[3+5*3] * alphaV + f[4+5*3];
    
    if (*red > 255)
    {
        *red = 255;
    }
    if(*red < 0)
    {
        *red = 0;
    }
    if (*green > 255)
    {
        *green = 255;
    }
    if (*green < 0)
    {
        *green = 0;
    }
    if (*blue > 255)
    {
        *blue = 255;
    }
    if (*blue < 0)
    {
        *blue = 0;
    }
    if (*alpha > 255)
    {
        *alpha = 255;
    }
    if (*alpha < 0)
    {
        *alpha = 0;
    }
}

+ (UIImage*)imageWithImage:(UIImage*)inImage withColorMatrix:(const float*) f
{
	unsigned char *imgPixel = RequestImagePixelData(inImage);
	CGImageRef inImageRef = [inImage CGImage];
	GLuint w = (GLuint)CGImageGetWidth(inImageRef);
	GLuint h = (GLuint)CGImageGetHeight(inImageRef);
	
	int wOff = 0;
	int pixOff = 0;
	
    
	for(GLuint y = 0;y< h;y++)//双层循环按照长宽的像素个数迭代每个像素点
	{
		pixOff = wOff;
		
		for (GLuint x = 0; x<w; x++)
		{
			int red = (unsigned char)imgPixel[pixOff];
			int green = (unsigned char)imgPixel[pixOff+1];
			int blue = (unsigned char)imgPixel[pixOff+2];
            int alpha = (unsigned char)imgPixel[pixOff+3];
            changeRGBA(&red, &green, &blue, &alpha, f);
            
            //回写数据
			imgPixel[pixOff] = red;
			imgPixel[pixOff+1] = green;
			imgPixel[pixOff+2] = blue;
            imgPixel[pixOff+3] = alpha;
            
            
			pixOff += 4; //将数组的索引指向下四个元素
		}
        
		wOff += w * 4;
	}
    
	NSInteger dataLength = w * h * 4;
    
    //下面的代码创建要输出的图像的相关参数
	CGDataProviderRef provider = CGDataProviderCreateWithData(NULL, imgPixel, dataLength, NULL);
    
	int bitsPerComponent = 8;
	int bitsPerPixel = 32;
	int bytesPerRow = 4 * w;
	CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
	CGBitmapInfo bitmapInfo = kCGBitmapByteOrderDefault;
	CGColorRenderingIntent renderingIntent = kCGRenderingIntentDefault;
	
	
	CGImageRef imageRef = CGImageCreate(w, h, bitsPerComponent, bitsPerPixel, bytesPerRow,colorSpaceRef, bitmapInfo, provider, NULL, NO, renderingIntent);//创建要输出的图像
	
	UIImage *myImage = [UIImage imageWithCGImage:imageRef];
	
	CFRelease(imageRef);
	CGColorSpaceRelease(colorSpaceRef);
	CGDataProviderRelease(provider);
	return myImage;
}

+ (UIImage *)imageFromView: (UIView *) theView{
    UIGraphicsBeginImageContext(theView.frame.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [theView.layer renderInContext:context];
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return theImage;
}
+(UIImage *)scaleImage:(UIImage*)image toSize:(CGSize)newSize
{
    
    UIGraphicsBeginImageContext(newSize);//根据当前大小创建一个基于位图图形的环境
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];//根据新的尺寸画出传过来的图片
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();//从当前环境当中得到重绘的图片
    UIGraphicsEndImageContext();//关闭当前环境
    
    return newImage;
}


+(UIImage *)createThumbImage:(UIImage*)image width:(float)width{
    
    CGSize size = image.size;
    float height = width * size.height / size.width ;
    CGSize newSize = CGSizeMake(width, height);
    
    return [ImageUtil scaleImage:image toSize:newSize];
}


+(UIImage*)applyFilter:(UIImage*)image index:(NSInteger)index{
    if(index>0){
        image =[ImageUtil imageWithImage:image withColorMatrix:image_filters[index-1]];
    }
    return image;
}

+(float*)getMatrix:(int)index{
    
    
    return image_filters[index-1];
    
}


+(NSString*)encodeURL:(NSString *)string
{
    NSString *newString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes( kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"),kCFStringEncodingUTF8));
    if (newString) {
        return newString;
    }
    return @"";
}

+(NSString*)base64FromImage:(UIImage*)image quality:(CGFloat)quality{
    NSData* imgData = UIImageJPEGRepresentation(image,quality);
    NSString* base64string = [imgData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    return [ImageUtil encodeURL:base64string];
}

@end
