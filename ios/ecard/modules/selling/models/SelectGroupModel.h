//
//  SelectGroupModel.h
//  ecard
//
//  Created by randy ren on 15-1-31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <ecardlib/ecardlib.h>
#import <UIKit/UIKit.h>

/**
 *  管理可选接口
 */
@protocol SelectGroupModelDelegate<NSObject>

/**
 *  选择发生改变
 *
 *  @param sender       <#sender description#>
 *  @param selectedView <#selectedView description#>
 *  @param index        <#index description#>
 */
@required
-(void)onSelChanged:(id)sender selectedView:(UIView<ISelectable>*)selectedView index:(NSInteger)index;

@end

@interface SelectGroupModel : NSObject


-(id)initWithParent:(UIView*)view;


-(void)setSelectTag:(int)tag;




@property (nonatomic,weak) id<SelectGroupModelDelegate> delegate;

@end
