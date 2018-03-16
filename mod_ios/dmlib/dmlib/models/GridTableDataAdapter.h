//
//  GridTableDataAdapter.h
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "TableDataAdapter.h"

@interface GridTableDataAdapter : TableDataAdapter
{
     NSString* _cell;
     NSInteger _cols;
    
    NSBundle* _bundle;
}
-(void)setCols:(NSInteger)cols;

@end



@interface GridTableDataAdapterWithSectionHeader : GridTableDataAdapter

-(void)setSectionHeader:(UIView*)header;

@end
