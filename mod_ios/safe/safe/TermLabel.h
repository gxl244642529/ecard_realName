//
//  TermLabel.h
//  ecard
//
//  Created by randy ren on 16/2/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "TTTAttributedLabel.h"
#import <DMLib/DMLib.h>


@interface TermLabel : TTTAttributedLabel<TTTAttributedLabelDelegate>

-(void)text:(NSString*)text noticeUrl:(NSString*)noticeUrl protocalUrl:(NSString*)protocalUrl;

@end
