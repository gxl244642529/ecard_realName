//
//  QuestionController.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "QuestionController.h"
#import "DMBorderView.h"



@interface QuestionController ()
{
    UIScrollView* _scrollView;
    NSInteger _height;
}
@end

@implementation QuestionController

-(void)dealloc{
    _scrollView = NULL;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"常见问题解答"];
    self.view.backgroundColor = RGB_WHITE(f2);
    
    _scrollView = [[UIScrollView alloc]initWithFrame:self.view.bounds];
    [self.view addSubview:_scrollView];
    
    NSArray* arr = @[
                     @[@"e通卡除了在指定网点现金充值外，还有其它什么充值模式？",
                     @"您好！e通卡已实现在中国农业银行自助终端机、e通卡自助终端机实现自助充值业务；也可实现网上充值服务，用户仅需购买e通卡充付终端登录厦门易通卡官网通过银行借记卡、账户卡完成网上充值服务。e通卡充付终端定价为188元/台，在易通卡客服中心文灶和软件园营业厅、都都宝官网及淘宝网都都宝旗舰店均有销售。" ],
                     @[@"e通卡在哪里购买？怎么收费？",
                       @"您好！目前市场上销售的易通卡主要分为两类，一类以押金形式销售的普通卡，每张80元（其中押金30元，充值额50元），卡片不使用可办理退卡；另一类为纪念卡，有特定主题、限量发行、多种工艺，不同主题及工艺的纪念卡销售价格不等，纪念卡可长期使用不办理退卡。易通卡客服中心、邮局、BRT站点等均有销售普通卡和纪念卡。"],
                     @[@"e通卡不能刷卡，应该怎么处理？换新卡需要收费？",
                       @"您好！卡片无法刷卡时，请携带卡片至易通卡客服中心进行检测处理；\n•	普通卡、纪念卡（无押金卡，除读报金卡、银鹭U利卡、电信VIP卡、海峡导报卡、中国消费卡外）及专用e通卡，若属卡本身质量问题损坏的予以免费更换；\n•	普通卡因打孔、分层、断裂等原因换卡的，持卡人需支付换卡工本费15元/张（厦价商[2011]10号）\n•	纪念卡（无押金卡，除读报金卡、银鹭U利卡、电信VIP卡、海峡导报卡、中国消费卡外）、专用e通卡因打孔、分层、断裂等原因损坏换卡的，持卡人可自备卡新卡、购买普通卡、无押金卡或支付工本费15元换取通用型纪念卡；\n•	读报金卡、银鹭U利卡、电信VIP卡、海峡导报卡、中国消费卡，若卡片发生故障无法使用，换卡时持卡人可自备卡新卡或购买普通卡、无押金卡；\n•	原故障卡卡内余额需经过易通卡公司核查确定，持卡人在7天后凭换卡凭证至易通卡客服中心办理转值；"],
                     @[@"卡片换卡为什么需要7个工作日才能办理转值？",@"您好！e通卡刷卡属于脱机离线的交易方式，为准确核查卡片的最终余额，需经后台核销清算准确金额。"],
                     @[@"要离开厦门，如何办理退卡？",@"您好！普通卡卡片无污损、性能完好且卡内余额小于100元以下可至e通卡客服中心办理退卡业务，人为损坏导致普通卡无法正常使用，用户可支付15元工本费后办理退卡。若普通卡卡内余额超过（含）100元，根据中国人民银行颁发的《支付机构预付卡业务管理办法》规定，不办理退卡业务。\n纪念卡及专用e通卡不办理退卡。"],
                     @[@"优惠卡要怎么办理？",@"您好!符合条件的优惠卡群体持相关证件至易通卡客服中心办理。各类优惠卡业务办理需携带的相关证件可在厦门易通卡官网查询或拨打968870咨询。"],
                     @[@"卡片丢失后是否可办理挂失？卡内余额是否可以办理转存？",@"您好！由于e通卡不记名、不挂失，拾获e通卡的人，也可继续消费使用。使用e通卡支付产生的交易金额，易通卡公司按照规范的流程，通过备付金存管银行专用账户将资金结算给相关单位（如公交公司、轮渡、小额消费商户等）。持卡用户暂时没有消费的金额，易通卡公司均严格按照人民银行2011年颁发的，关于第三方支付机构《支付机构客户备付金存管暂行办法》管理规定要求，存储在备付金托管银行的专用账户内，由人民银行进行严格监管。"],
                     @[@"我的卡坐车没有享受优惠，要怎么办理？",@"您好！优惠卡群体享受乘车优惠需定期参与年审，具体年审如下：\n学生卡--本地户籍9年义务内的学生可办理9年义务内的年审，超过9年义务外、外地户口和本科以内的学生每年8.9月份需参加年审；\n老人卡—年满65至69周岁的用户首次办卡优惠有效期截止70周岁当月，超过70周岁的用户办卡之日起每2年年审一次；\n劳模卡和烈属卡--办卡之日起每3年年审1次；\n保障卡--优惠有效期为1年，到期时需至户籍所在地的居委会/社区递交资料审核，审核通过后由各区民政局将已审核通过的数据发送到易通卡客服中心进行批量导入，用户才可办理年审。"]
                     ];
    CGFloat rate = self.view.bounds.size.width / 320;
    UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ecardlibbundle.bundle/bg_service.jpg"]];
    imageView.frame = CGRectMake(0, 0, 320 * rate, 170 * rate);
    [_scrollView addSubview:imageView];
    _height = 170 * rate;
    
    _height+=10;
    
    
    UIColor* color = RGB(fc, 8d, 03);
    
    for (NSArray* tmp in arr) {
        [self create:@"问:" content:tmp[0] color:color];
        [self create:@"答:" content:tmp[1] color:nil];
    }
    
    _height+=5;
    
    DMBorderView* bg = [[DMBorderView alloc]initWithFrame:CGRectMake(5, 175, 320 * rate - 10, _height-175)];
    bg.cornerRadius = 5;
    bg.borderColor = RGB_WHITE(f2);
    [_scrollView insertSubview:bg atIndex:0];
 
    _scrollView.contentSize = CGSizeMake(0, _height+100);
}

-(void)create:(NSString*)title content:(NSString*)content color:(UIColor*)color{
    UILabel* l1 = [[UILabel alloc]initWithFrame:CGRectMake(10, _height, 20, 20)];
    [_scrollView addSubview:l1];
    [l1 setFont:[UIFont fontWithName:@"Helvetica" size:12]];
    [l1 setTextColor:[UIColor darkGrayColor]];
    l1.text = title;
    [l1 sizeToFit];
    
    
    UILabel* l2 = [[UILabel alloc]initWithFrame:CGRectMake(30, _height, self.view.frame.size.width-40, 20)];
    [_scrollView addSubview:l2];
    [l2 setFont:[UIFont fontWithName:@"Helvetica" size:12]];
    l2.numberOfLines = 0;
    l2.text = content;
    [l2 sizeToFit];
    if(color){
        l2.textColor = color;
    }
    _height += l2.frame.size.height + 5;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
