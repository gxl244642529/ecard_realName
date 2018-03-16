//
//  JobTypes.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum _JobTypes{
    JobType_Api,
    JobType_NormalHttp,
    JobType_Image,
    JobType_Upload,
}JobTypes;


typedef enum _DataTypes{
    DataType_Image,
    DataType_Json,
    DataType_Raw,
    DataType_Text,
    DataType_File,
    DataType_ApiArray,
    DataType_ApiObject,
    DataType_ApiPage
}DataTypes;
