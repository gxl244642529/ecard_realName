//
//  FormTextField.m
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormTextField.h"
#import "UIView+ViewNamed.h"
#import "DMFormValidateUtil.h"
#import "CommonUtil.h"


@interface DMFormTextField()
{
    BOOL isPassword;
}

@end


@implementation DMFormTextField

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(void)dealloc{
#ifdef DEBUG
    NSLog(@"%@ %@ dealloc",NSStringFromClass([self class]),self.viewName);
#endif
    self.fieldName = nil;
}

-(void)awakeFromNib{
    self.returnKeyType = UIReturnKeyDone;
    [self addTarget:self action:@selector(onEnd:) forControlEvents:UIControlEventEditingDidEndOnExit];
    isPassword =self.secureTextEntry;
   // self addTarget:self action:@selector(onDone) forControlEvents:<#(UIControlEvents)#>
}

-(void)onEnd:(id)sender{
    [sender resignFirstResponder];
}

-(void)setVal:(id)val{
    if(val == [NSNull null]){
        self.text = nil;
    }else{
         self.text = val;
    }
   
}

-(id)val{
    NSString* value = self.text;
    if([value isEqualToString:@""]){
        return nil;
    }
    if(isPassword){
        
        value = [CommonUtil md5:value];
        
    }
    return value;
}

-(NSString*)validate:(NSMutableDictionary*)data{
    NSString* value = [self val];
    if(_required){
        if(!value){
            return self.placeholder;
        }
    }
    
    if(self.validate){
        NSString* error = nil;
        if(![DMFormValidateUtil validate:value rule:self.validate error:&error]){
            return error;
        }
        
    }
    
    
    data[self.viewName]=value;
    return nil;
}


@end
