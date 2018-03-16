package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.models.vos.ExamAdvListVo;
import com.citywithincity.ecard.models.vos.ExamSchoolListVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.models.cache.CachePolicy;

import java.util.Map;


/**
 * 年审模型,定义网络接口
 * @author Randy
 *
 */
public interface ExamModel {
	
	String EXAM_ADV_LIST = "exam_adv_list";
	String QUERY = "exam_api/query";
	String UPLOAD = "exam_api/upload";
	String SCHOOL_LIST = "exam_api/school_list";
	String EXAM_POINT = "exam_point";
	String HIS = "exam_api/exam_his";
//	public static final String QUERY = "query"; 
//	public static final String UPLOAD = "upload";
String SCHOOL_HAS_CHANGED = "schoolHasChanged";
	
	/**
	 * 获取年审广告
	 * @param position
	 * @return
	 */
	@ApiArray(api=EXAM_ADV_LIST,clazz=ExamAdvListVo.class,paged=false)
	IArrayJsonTask<ExamAdvListVo> getExamAdvList(int position);
	
	
	
	/**
	 * 查询学校
	 * @param name
	 * @return
	 */
	@ApiArray(api=SCHOOL_LIST,paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=ExamSchoolListVo.class,params={"name"},waitingMessage="正在搜索...")
	IArrayJsonTask<ExamSchoolListVo> getSchoolList(String name);
	
	@ApiValue(api=UPLOAD, params = { "" },waitingMessage="正在提交信息...")
	void upload(Map<String, Object> map);

	

	
}
