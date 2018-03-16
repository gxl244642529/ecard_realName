//
//  TermLabel.m
//  ecard
//
//  Created by randy ren on 16/2/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "TermLabel.h"


@interface TermLabel()
{
    NSString* _noticeUrl;
    NSString* _protocalUrl;
    __weak UIViewController* _currentController;
}

@end


@implementation TermLabel


-(void)dealloc{
    _noticeUrl = nil;
    _protocalUrl = nil;
}


-(void)text:(NSString*)text noticeUrl:(NSString*)noticeUrl protocalUrl:(NSString*)protocalUrl{
    
    _noticeUrl = noticeUrl;
    _protocalUrl = protocalUrl;
    
    
    [self setText:text afterInheritingLabelAttributesAndConfiguringWithBlock:^ NSMutableAttributedString *(NSMutableAttributedString *mutableAttributedString){
    NSRange boldRange = [mutableAttributedString.string rangeOfString:@"《保险条款》" options:NSCaseInsensitiveSearch];
    
    UIFont *boldSystemFont = nil;
    
    boldSystemFont = [UIFont fontWithName:@"PingFangSC-Light" size:13];
    if(!boldSystemFont){
        boldSystemFont = [UIFont systemFontOfSize:13];
    }
    
    CTFontRef font = CTFontCreateWithName((__bridge CFStringRef)boldSystemFont.fontName, boldSystemFont.pointSize, NULL);
    //设置可点击文本的大小
    [mutableAttributedString addAttribute:(NSString *)kCTFontAttributeName value:(__bridge id)font range:boldRange];
    //设置可点击文本的颜色
    [mutableAttributedString addAttribute:(NSString*)kCTForegroundColorAttributeName value:(id)[[UIColor redColor] CGColor] range:boldRange];
    
    boldRange = [mutableAttributedString.string rangeOfString:@"《重要告知》" options:NSCaseInsensitiveSearch];
    
    //设置可点击文本的大小
    [mutableAttributedString addAttribute:(NSString *)kCTFontAttributeName value:(__bridge id)font range:boldRange];
    //设置可点击文本的颜色
    [mutableAttributedString addAttribute:(NSString*)kCTForegroundColorAttributeName value:(id)[[UIColor redColor] CGColor] range:boldRange];
    
    
    return mutableAttributedString;
}];
    self.highlightedTextColor = [UIColor redColor];
    NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
    [dic setValue:[UIColor redColor] forKey:(NSString*)kCTForegroundColorAttributeName];
    [dic setValue:[NSNumber numberWithBool:NO] forKey:(NSString*)kCTUnderlineStyleAttributeName];
    
    self.linkAttributes = dic;
    
    NSRange linkRange = [text rangeOfString:@"《保险条款》" options:NSCaseInsensitiveSearch];
    NSURL *url = [NSURL URLWithString:protocalUrl];
    //设置链接的url
    [self addLinkToURL:url withRange:linkRange];
    linkRange = [text rangeOfString:@"《重要告知》" options:NSCaseInsensitiveSearch];
    url = [NSURL URLWithString:noticeUrl];
    //设置链接的url
    [self addLinkToURL:url withRange:linkRange];
    
    self.numberOfLines = 0;
    [self sizeToFit];
    
    
}


- (void)attributedLabel:(TTTAttributedLabel *)label
   didSelectLinkWithURL:(NSURL *)url{
    
    if([url.absoluteString isEqualToString:_noticeUrl]){
         [_currentController.navigationController pushViewController:[[DMWebViewController alloc]initWithTitle:@"重要告知" url:url.absoluteString] animated:YES];
    }else{
        [_currentController.navigationController pushViewController:[[DMWebViewController alloc]initWithTitle:@"保险条款" url:url.absoluteString] animated:YES];
    }

    
}


-(void)awakeFromNib{
    [super awakeFromNib];
    
    _currentController = [DMJobManager sharedInstance].topController;
    self.delegate = self;
}


@end
