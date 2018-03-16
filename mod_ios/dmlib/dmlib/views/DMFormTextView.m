//
//  DMFormTextView.m
//  DMLib
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormTextView.h"
#import "UIView+ViewNamed.h"

@implementation DMFormTextView


-(id)val{
    NSString* value = self.text;
    if([value isEqualToString:@""]){
        return nil;
    }
    return value;
}

-(NSString*)validate:(NSMutableDictionary*)data{
    NSString* value = [self val];
    if(_required){
        if(!value){
            return self.fieldName;
        }
    }
    data[self.viewName]=value;
    return nil;
}

@end
