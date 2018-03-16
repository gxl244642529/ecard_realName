//
//  Form.h
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "FormModel.h"
#import "FormScrollView.h"
#import "ObjectJsonTask.h"


@protocol IFormSubmit <NSObject>

-(void)initForm:(FormModel*)model contentView:(UIView*)contentView;
-(void)onSubmitComplete:(NSDictionary*)submitData result:(id)result;

@optional
-(BOOL)beforeSubmit:(NSMutableDictionary*)data contentView:(UIView*)contentView;
@end
/**
 *  表单
 */
@interface Form : NSObject<IFormListener,IRequestResult>
{
    FormScrollView* _scrollView;
    FormModel* _model;
    id _contentView;
    __weak UIView* _parentView;
}
-(id)initWithParent:(UIView*)parent contentNibName:(NSString*)contentNibName frame:(CGRect)frame api:(NSString*)api listener:(NSObject<IFormSubmit>*)listener;

@property (nonatomic,retain) NSString* submitMessage;

@end
