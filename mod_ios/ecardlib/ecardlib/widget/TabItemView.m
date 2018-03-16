//
//  TabItemView.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TabItemView.h"
#import "ViewUtil.h"

@interface TabItemView()
{
    UIImageView* _image;
    UILabel* _label;
    BOOL _selected;
}

@end

@implementation TabItemView


-(void)dealloc{
    _image = NULL;
    _label = NULL;
}

-(void)setSelected:(BOOL)selected{
    [_label setHighlighted:selected];
    [_image setHighlighted:selected];
}

-(void)awakeFromNib{
    [super awakeFromNib];
    self.autoresizesSubviews = NO;
    for (UIView* view in self.subviews) {
        
        if( [view isKindOfClass:[UIImageView class]] ){
            _image = (UIImageView*)view;
        }else if( [view isKindOfClass:[UILabel class]] ){
            _label = (UILabel*)view;
        }
    }
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        _label = [ViewUtil createLabel:@"" parent:self];
        _image = [[UIImageView alloc]initWithFrame:CGRectZero];
        self.autoresizesSubviews = NO;
        [self addSubview:_image];
    }
    return self;
}
-(void)layoutSubviews{
    [super layoutSubviews];
    
    CGRect frame = self.bounds;
    CGPoint point = CGPointMake(frame.size.width/2, frame.size.height/2);
    //计算文字大小
    CGSize tsize = _label.frame.size;
    CGSize isize = _image.frame.size;
    NSLog(@"%@",_label.text);
    NSInteger total;
    switch (_iconDir) {
        case Dir_Left:
        {
            //图片在文字的左边
            total = tsize.width + _padding + isize.width;
            NSInteger start = (frame.size.width - total )/2;
            _image.center = CGPointMake(start + isize.width/2 , point.y);
            _label.center = CGPointMake(start+ total - tsize.width/2, point.y);
            
        }
            break;
        case Dir_Right:
        {
            
            
            //图片在文字的左边
            total = tsize.width + _padding + isize.width;
            NSInteger start = (frame.size.width - total)/2;
            _label.center = CGPointMake(start + tsize.width/2, point.y );
            _image.center = CGPointMake(start + total - isize.width/2, point.y);
            
        }
            break;
        case Dir_Bottom:
        {
            //图片在文字的下面
            total = tsize.height + _padding + isize.height;
            NSInteger start = (frame.size.height - total)/2;
            
            _label.center = CGPointMake(point.x, start + tsize.height/2);
            _image.center = CGPointMake(point.x , start+ total - isize.height/2);
            
            break;
        }
        case Dir_Top:
        {
            //图片在文字的下面
            total = tsize.height + _padding + isize.height;
            NSInteger start = (frame.size.height - total)/2;
            
            _image.center = CGPointMake(point.x , start + isize.height/2);
            _label.center = CGPointMake(point.x, start+total - tsize.height/2);
            
            break;
        }
            
        default:
            break;
    }
}



/**
 *  设置文字
 *
 *  @param text <#text description#>
 */
-(void)setText:(NSString*)text{
    _label.text = text;
    [_label sizeToFit];
}

-(NSString*)text{
    return _label.text;
}


/**
 *  设置图标
 *
 *  @param name <#name description#>
 */
-(void)setIcon:(NSString*)name highlighted:(NSString*)highlighted{
    UIImage* image=[UIImage imageNamed:name];
    [_image setImage:image];
    [_image setHighlightedImage:[UIImage imageNamed:highlighted]];
    _image.bounds = CGRectMake(0,0,image.size.width,image.size.height);
}
-(void)setIconSize:(NSInteger)size{
    _image.bounds = CGRectMake(0,0,size,size);
}

-(NSInteger)iconSize{
    return _image.bounds.size.width;
}
/**
 *  设置文字颜色
 *
 *  @param color       <#color description#>
 *  @param highlighted <#highlighted description#>
 */
-(void)setTextColor:(UIColor*)color highlighted:(UIColor*)highlighted{
    [_label setTextColor:color];
    [_label setHighlightedTextColor:highlighted];
}


/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

@end
