//
//  ViewUtil.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ViewUtil.h"

@implementation ViewUtil

+(__kindof UIView*)findView:(UIView*)view class:(Class)clazz{
    
    for (UIView* subView in view.subviews) {
        if([subView isKindOfClass:clazz]){
            return subView;
        }
    }
    return nil;
    
}
+(BOOL)isLongScreen{
    
    CGRect rect = [UIScreen mainScreen].bounds;
    CGFloat rate = rect.size.width / 320;
    CGFloat height = rect.size.height / rate;
    return  height > 480;
}

+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView{
    UILabel* view = [[UILabel alloc]initWithFrame:CGRectZero];
    view.text = label;
    view.font = [UIFont fontWithName:@"HelveticaNeue" size:12];
    view.numberOfLines = 0;
     view.backgroundColor = [UIColor clearColor];
    [view sizeToFit];
    [parentView addSubview:view];
    return view;
}
+(void)createLoading:(NSInteger)tag container:(UIView*)container{
    /*UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake((container.bounds.size.width-32)/2, (container.bounds.size.height-32)/2, 32, 32)];
    view.tag = tag;
    [container addSubview:view];
    [view setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleGray];
    [view startAnimating];*/
    //增加约束
    UIView* view = [ViewUtil createViewFormNibName:@"LoadingView" owner:nil];
    view.tag = tag;
    [container addSubview:view];
    
}
+(void)setNineBg:(UIView*)view image:(NSString*)image{
    UIImage *backgroundImage = [UIImage imageNamed:image];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
    UIImageView* imageView = [[UIImageView alloc]initWithFrame:view.bounds];
    imageView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    imageView.image = backgroundImage;
    [view insertSubview:imageView atIndex:0];
}

+(void)setButtonBg:(UIButton*)button{
    UIImage *backgroundImage = [UIImage imageNamed:@"ic_btn_1"];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
    [button setBackgroundImage:backgroundImage forState:UIControlStateNormal];
    backgroundImage = [UIImage imageNamed:@"ic_btn_2"];
    backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
    [button setBackgroundImage:backgroundImage forState:UIControlStateHighlighted];
    
    button.layer.masksToBounds = YES;
    button.layer.cornerRadius = 5;
}


+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView size:(float)size bold:(BOOL)bold gray:(BOOL)gray{
    UILabel* view = [[UILabel alloc]initWithFrame:CGRectZero];
    view.text = label;
    if(bold){
        view.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:size];
    }else{
        view.font = [UIFont fontWithName:@"HelveticaNeue" size:size];
    }
    
    if(gray){
        view.textColor = [UIColor darkGrayColor];

    }
    view.numberOfLines = 0;
    view.backgroundColor = [UIColor clearColor];
    [view sizeToFit];
    [parentView addSubview:view];
    return view;
}

+(id)createViewFormNibName:(NSString*)nibName  owner:(id)owner{
    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:nibName owner:owner options:nil];
    return [nib objectAtIndex:0];
}
+(UIButton*)createButton:(CGRect)rect label:(NSString*)label normalColor:(UIColor*)normalColor highColor:(UIColor*)highColor{
    UIButton* btnBind = [[UIButton alloc]initWithFrame:rect];
    [btnBind setTitle:label forState:UIControlStateNormal];
    [btnBind setTitle:label forState:UIControlStateHighlighted];
    [btnBind setTitleColor:normalColor forState:UIControlStateNormal];
    [btnBind setTitleColor:highColor forState:UIControlStateSelected];
    [btnBind.titleLabel setFont:[UIFont fontWithName:@"HelveticaNeue" size:14]];
    return btnBind;

}
+(UIButton*)createButton:(CGRect)rect normal:(NSString*)normal high:(NSString*)high label:(NSString*)label
{
    UIButton* btnBind = [[UIButton alloc]initWithFrame:rect];
    [btnBind setBackgroundImage:[UIImage imageNamed:normal] forState:UIControlStateNormal];
    [btnBind setBackgroundImage:[UIImage imageNamed:high] forState:UIControlStateHighlighted];
    [btnBind setTitle:label forState:UIControlStateNormal];
    [btnBind setTitle:label forState:UIControlStateHighlighted];
    return btnBind;
}
+(UIButton*)createTabButton:(CGRect)rect normal:(NSString*)normal high:(NSString*)high label:(NSString*)label colorNormal:(UIColor*)colorNormal colorHigh:(UIColor*)colorHigh{
    UIButton* btnBind = [[UIButton alloc]initWithFrame:rect];
    [btnBind setBackgroundImage:[UIImage imageNamed:normal] forState:UIControlStateNormal];
    [btnBind setBackgroundImage:[UIImage imageNamed:high] forState:UIControlStateSelected];
    [btnBind setTitle:label forState:UIControlStateNormal];
    [btnBind setTitle:label forState:UIControlStateHighlighted];
    [btnBind setTitleColor:colorNormal forState:UIControlStateNormal];
    [btnBind setTitleColor:colorHigh forState:UIControlStateSelected];
    return btnBind;
}
@end
