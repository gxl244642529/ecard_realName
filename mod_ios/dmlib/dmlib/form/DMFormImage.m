//
//  FormImage.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormImage.h"
#import "DMBase64.h"
#import "UIImage+Utility.h"
#import "UIView+ViewNamed.h"

@interface DMFormImage()
{
    //NSData* _image;
    UIImage* _image;
}
@end

@implementation DMFormImage

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(void)dealloc{
    _image = nil;
    _fieldName = nil;
}

-(void)awakeFromNib{
    [super awakeFromNib];
    
    if(!_maxSize){
        _maxSize = 640;
    }
}


-(NSString*)validate:(NSMutableDictionary *)data{
    
    if(!_image){
        return [NSString stringWithFormat:@"请选择%@",_fieldName];
    }
    [data setValue:[self val] forKey:self.viewName];
    return nil;
}

-(void)setImage:(UIImage *)image{
    _image = image;
    [super setImage:image];
}


-(id)val{
    //这里的是nsdata
    if(!_image)return nil;
    return UIImageJPEGRepresentation(_image,1);
}
-(void)setVal:(id)val{
    if([val isKindOfClass:[NSData class]]){
        UIImage* image = [UIImage imageWithData:val];
        image = [self resizeImage:image];
        [self setImage:image];
    }else if([val isKindOfClass:[NSString class]]){
        NSData* decodeData = [[NSData alloc]initWithBase64EncodedString:val options:0];
        if(decodeData && decodeData.length > 0 ){
            UIImage *image = [UIImage imageWithData: decodeData];
            image = [self resizeImage:image];
            [self setImage:image];
        }
    }else if([val isKindOfClass:[UIImage class]]){
        //说明是本地图片
        
        [self setImage:[self resizeImage:val]];
    }
    self.userInteractionEnabled = YES;
}

-(UIImage*)resizeImage:(UIImage*)image{
    CGSize oldSize = image.size;
    UIImage* result = image;
    if(oldSize.width > _maxSize || oldSize.height > _maxSize){
        double dx = _maxSize/oldSize.width;
        double dy = _maxSize / oldSize.height;
        dx = MIN(dx, dy);
        result = [image resize:CGSizeMake(oldSize.width * dx, oldSize.height*dx)];
    }
    
    return result;
}
@end
