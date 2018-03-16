//
//  DMFixTableView.h
//  DMLib
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IDataAdapterListener.h"
#import "DMValue.h"
@interface DMFixTableView : UIView<DMValue>

@property (nonatomic,retain) IBInspectable NSString* bundleName;
@property (nonatomic,retain) IBInspectable NSString* nibName;
@property (nonatomic,assign) IBInspectable NSInteger rowHeight;

-(void)setListener:(NSObject<IDataAdapterListener>*)listener;
-(void)registerNib:(NSString*)nibName bundleName:(NSString*)bundleName rowHeight:(CGFloat)rowHeight;

-(void)addItem:(id)data;

-(NSMutableArray*)array;

//期望高度
-(CGFloat)expHeight;


@end
