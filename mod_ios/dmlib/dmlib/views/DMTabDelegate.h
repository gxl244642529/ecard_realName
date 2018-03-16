//
//  DMTabDelegate.h
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

//只要是组切换,统一使用这个
@protocol DMTabDelegate <NSObject>

-(void)tab:(id)tab didSelectIndex:(NSInteger)index;

@end


//只要是可以切换的,都可以使用这个
@protocol DMTab <NSObject>

-(void)setCurrentIndex:(NSInteger)index animated:(BOOL)animated;

@end
