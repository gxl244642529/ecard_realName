//
//  IListRequestResult.h
//  ecard
//
//  Created by randy ren on 14-5-8.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "IRequestError.h"
#import "IJsonTask.h"
@protocol IArrayRequestResult <IRequestError>

-(void)task:(id)task result:(NSArray*)result position:(NSInteger)position isLastPage:(BOOL)isLastPage;


@end
