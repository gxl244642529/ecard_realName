//
//  FormItem.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol DMFormElement 

-(NSString*)validate:(NSMutableDictionary*)data;



@end



//这里需要考虑协议
@protocol DMFormFile <DMFormElement>


-(NSString*)validate:(NSMutableArray*)files;

@end