//
//  DMCheckItem.h
//  ecard
//
//  Created by randy ren on 16/2/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <dmlib/dmlib.h>


/**
 其中有一个按钮,设置为不可用
 
 点击后,将切换按钮是否被选中
 */

@interface DMCheckItem : DMItem
{
    __weak UIButton* _button;
}
@end
