//
//  FormLabel.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormLabel.h"
#import "UIView+ViewNamed.h"
@implementation DMFormLabel

-(void)dealloc{
}


-(NSString*)validate:(NSMutableDictionary *)data{
    
    if(_required){
        if([self.text isEqualToString:@""]){
            return _hint;
        }
    }
    
    data[self.viewName] = self.text;
    
    return nil;
    
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
