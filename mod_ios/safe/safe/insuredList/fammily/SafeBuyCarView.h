//
//  SafeBuyCarView.h
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import <DMLib/DMLib.h>
@interface SafeBuyCarView : UIView<DMFormElement>


@property (weak, nonatomic) IBOutlet UITextField *carNo;
@property (weak, nonatomic) IBOutlet UITextField *carFrame;
@end
