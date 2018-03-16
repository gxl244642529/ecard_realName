//
//  SafeInsuredListCell.m
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredListCell.h"
#import "SafeDetailInfoView.h"

@implementation SafeInsuredListCell

- (void)awakeFromNib {
    // Initialization code
}

-(NSString*)validate:(NSMutableDictionary *)data{
    
    //这里需要更新
    for(SafeDetailInfoView* view in _tableView.subviews){
        NSString* error = [view validate];
        if(error){
            return error;
        }
    }
    
    NSMutableArray* result = [[NSMutableArray alloc]init];
    
    NSMutableArray* arr = [self getInsuredList];
    for (SafeContact* contact in arr) {
        
        [result addObject:@{
                            @"name":contact.name,
                            @"idCard":contact.idCard,
                            @"relation":[NSNumber numberWithInteger:contact.relation]
                            }];
        
    }
    
    data[@"insured"] = result;
    
    return nil;
}

-(NSMutableArray*)getInsuredList{
    NSMutableArray* arr = _tableView.array;
    NSInteger index = 0;
    //这里需要更新
    for(SafeDetailInfoView* view in _tableView.subviews){
        
        SafeContact* contact = arr[index];
        [view getInsured:contact];
        ++index;
        
    }
    
    return arr;
}

@end
