//
//  InsuredContactView.h
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/DMLib.h>



@protocol ContactSelectDelegate <NSObject>

-(void)onSelect:(UIButton*)button;

@end

@interface InsuredContactView : UIView<DMJobDelegate>


-(void)setData:(NSArray*)data;

-(void)startLoad;

-(NSInteger)currentHeight;


@property (nonatomic,weak) id<ContactSelectDelegate> tabDelegate;

@end
