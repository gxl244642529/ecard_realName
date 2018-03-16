//
//  DMDataSetter.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 * 为视图设置数据
 */
@protocol DMDataSetter <NSObject>

-(void)setValue:(id)data;

@end
@interface DMDataSetter : NSObject<DMDataSetter>
{
     NSString* _name;
}
-(id)initWithName:(NSString*)name;
@end
