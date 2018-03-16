//
//  SafeInsuredWithRelationCell.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredWithRelationCell.h"
#import "SafeRelationView.h"

@interface SafeInsuredWithRelationCell()
{
    
   
    SafeRelationView* _relation;
}
@property (weak, nonatomic) IBOutlet UILabel *_label;

@end

@implementation SafeInsuredWithRelationCell

- (IBAction)onDel:(id)sender {
    
    [_delegate onRemove:self.data];
}

-(void)awakeFromNib{
    [super awakeFromNib ];
   
    _relation = [self viewWithTag:20];
}

-(void)setData:(SafeContact *)data{
    [super setData:data];
    _relation.data = data;
    __label.text = [NSString stringWithFormat:@"被保险人%ld",(long)data.index];
}


-(NSString*)validate:(SafeContact*)data{
    
    if(_name.text.length==0){
        return [NSString stringWithFormat:@"请输入被保险人%ld姓名",(long)data.index ];
    }
    
    
    if(_idCard.text.length==0){
        return [NSString stringWithFormat:@"请输入被保险人%ld身份证号",(long)data.index];
    }
    
    if(![ValidateUtil isIdCard:_idCard.text]){
        //这里需要判断在哪里
        return [NSString stringWithFormat:@"被保险人%ld身份证号不正确",(long)data.index];
    }
    
    

    
    id v = _relation.val;
    if(!v){
        return [NSString stringWithFormat:@"请选择与被保险人%ld关系",(long)data.index];
    }
    data.name = _name.text;
    data.idCard = _idCard.text;

    data.relation = [v integerValue];
    
    return nil;
    
}
@end
