//
//  InsuredContactView.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "InsuredContactView.h"
#import "SafeContact.h"
#import "InsuredSelectButton.h"

@interface InsuredContactView()
{
    
    NSInteger _currentHeight;
}

@end


@implementation InsuredContactView

-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        _currentHeight = frame.size.height;
        self.backgroundColor = [UIColor whiteColor];
        [self startLoad];
    }
    return  self;
}

-(void)dealloc{
 
}

-(void)onClick:(UIButton*)sender{

    [_tabDelegate onSelect:sender];
    
}


-(NSInteger)currentHeight{
    return _currentHeight;
}

-(void)jobSuccess:(DMApiJob*)request{
    [self setData:request.data];
}

-(void)setData:(NSArray*)data{
    NSInteger index = 0;
    for (SafeContact* dic in data) {
        InsuredSelectButton* btn = [[InsuredSelectButton alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        [btn setTitle:dic.name forState:UIControlStateNormal];
        [btn sizeToFit];
        btn.tag = index++;
        Control_AddTarget(btn, onClick);
        [self addSubview:btn];
    }
    CGFloat width = self.frame.size.width;
    static CGFloat padding = 5;
    CGFloat buttonHeight = 20;
    CGFloat start = 5;
    CGFloat startY = 5;
    index = 0;
    
    NSArray* arr = self.subviews;
    for(UIView* view in arr){
        view.frame = CGRectMake(start, startY, view.frame.size.width, view.frame.size.height);
        start += view.frame.size.width + padding;
        //如果下一个加这个>width
        if(index < arr.count-1){
            UIView* nextView = arr[index+1];
            if(start + nextView.frame.size.width + padding > width){
                
                //那么就应改环氧
                start = padding;
                startY += nextView.frame.size.height + padding ;
                
            }
        }
        buttonHeight = view.frame.size.height;
    }
    if(arr.count>0){
        _currentHeight = startY + buttonHeight + 5;
    }else{
        _currentHeight = 0;
    }
    
    //CGFloat rate = (CGFloat)lastHeight / _currentHeight;
    dispatch_async(dispatch_get_main_queue(), ^{
         [self setFrame:CGRectMake(0, self.frame.origin.y, self.frame.size.width, _currentHeight)];
    });
    
   /* self.transform = CGAffineTransformMakeScale(1, rate);
    
    [UIView animateWithDuration:0.3 animations:^{
         self.transform = CGAffineTransformMakeScale(1, 1);
    }];*/
    
}
-(void)startLoad{
    
}

@end
