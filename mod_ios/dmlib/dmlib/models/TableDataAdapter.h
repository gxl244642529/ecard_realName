//
//  TableDataAdapter.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DMDataAdapter.h"


#define createTableDataAdapter(AdapterName,TableViewName,CellName,Height) AdapterName = [[TableDataAdapter alloc]init]; \
[AdapterName setScrollView:TableViewName]; \
[AdapterName registerCell:@#CellName height:Height bundle:nil];\
[AdapterName setListener:self];

@interface TableDataAdapter : DMDataAdapter<UITableViewDataSource,UITableViewDelegate>
-(void)setLineStyle:(UITableViewCellSeparatorStyle)style;
-(void)registerCell:(NSString*)nibName bundle:(NSBundle*)bundle;
-(void)registerCell:(NSString*)cellName height:(NSInteger)height  bundle:(NSBundle*)bundle;
@end


@interface TableDataAdapterWithSectionHeader : TableDataAdapter
-(void)setSectionHeader:(UIView*)header;
@end

@interface TableDataAdapterWithFooter : TableDataAdapter{
    
}

@property (nonatomic,assign) NSInteger space;

@end
