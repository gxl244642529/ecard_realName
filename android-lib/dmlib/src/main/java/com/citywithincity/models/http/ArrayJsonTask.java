package com.citywithincity.models.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonTaskListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ThreadUtil;
import com.damai.core.ApiJob;
import com.damai.core.DMPage;

//数据使用key管理  key=api+position+条件
//version使用key管理
//缓存文件 = key + version

//相同的任务，加载不同的条件，不创建新的任务对象
//用内部的数据管理类来管理。

//数据分层:
//map { key: activity , value: map_of_task } 
//map_of_task  { key : api , value : task}
//array_task -> map_of_data , file
//map_of_data {key: api + position + 条件 , value:data_array);
//file : api+positon+条件+version

//detail_task -> 

/**
 * setApi
 * setPosition
 * 
 * loadFromCache  
 * 先从缓存获取map_of_data  [ api + position + 条件 ] PageData
 * 
 * 如果没有，则从文件获取,version
 * 
 * 
 * run() 
 */

/**
 * 分页任务,对于第一页，记录总的， 每一页，记录更新时间 第二页开始，比对更新时间，如果小于第一页的更新时间，则从网络访问
 * 
 * @author Randy
 * 
 * @param <T>
 */
public class ArrayJsonTask<T> extends AbsJsonTask<List<T>> implements IArrayJsonTask<T> {

	private IListRequsetResult<List<T>> listener;
	private IJsonParser<T> parser;

	protected int total;
	protected int page;
	protected int position;

	
	protected IJsonTaskListener<T> _onParseListener;
	// 第一次调用,如果是第二次调用，并且是首页，则从网络获取
	// 如果是第二次调用，不是首页，比对版本号,决定从网络获取
	protected boolean first;

	protected boolean visited;
	

	protected ArrayJsonTask(ApiJob job) {
		super(job);
        job.put("last",0);
		first = true;
		version = -1;
		position = LibConfig.StartPosition;
	}
	
	

	public IArrayJsonTask<T> setOnParseListener(IJsonTaskListener<T> listener){
		_onParseListener = listener;
		return this;
	}
	


	@Override
	public void destroy() {
		listener = null;
		parser = null;
		_onParseListener = null;
		super.destroy();
	}

	public IArrayJsonTask<T> setPosition(int position) {
		this.position = position;
		this.put("position", position);
		return this;
	}

	public IArrayJsonTask<T> setListener(IListRequsetResult<List<T>> listener) {
		this.listener = listener;
		this.errorListener = listener;
		return this;
	}

	public IArrayJsonTask<T> setParser(IJsonParser<T> parser) {
		this.parser = parser;
		return this;
	}

	@Override
	protected int parseResult(JSONObject json, boolean fromCache)
			throws JSONException {
		/*
		if(json.has("version")){
			version = json.getInt("version");
		}else{
			version = 0;
		}
		
		if(json.has("page")){
			page = json.getInt("page");
			total = json.getInt("total");
		}else{
			page = 1;
			total = 1;
		}
		List<T> returnResultArray = new ArrayList<T>();
		Object ret = null;
		if(_onParseListener!=null){
			ret = _onParseListener.beforeParseData();
		}
		
		if(!json.isNull(RESULT)){
			JSONArray array = json.getJSONArray(RESULT);
			int count = array.length();
			
			for (int i = 0; i < count; ++i) {
				try {
					T tmp = parseResult(array.getJSONObject(i));
					returnResultArray.add(tmp);
				} catch (JSONException ex) {
					ex.printStackTrace();
					return ParseResult.ERROR;
				}
			}
		}
		
		data = returnResultArray;
		if(_onParseListener!=null){
			_onParseListener.afterParseData(returnResultArray, ret);
		}
		return ParseResult.OK;*/
        return ParseResult.OK;
	}

	protected T parseResult(JSONObject json) throws JSONException{
		return parser.parseResult(json);
	}


    private List<T> parseList(){
        Object data = job.getData();
        List<JSONObject> array  =null;
        if(data instanceof DMPage){
            array = ((DMPage<JSONObject>)data).getList();
        }else if(data instanceof  List){
            array = (List<JSONObject>) data;
        }
        Object parseData = null;
        if(_onParseListener!=null){
            parseData = _onParseListener.beforeParseData();
        }

        List<T> list = new ArrayList<T>();
        if(parser!=null && array!=null){
            for(JSONObject obj : array){
                try{
                    list.add(parser.parseResult(obj));
                }catch (JSONException e){
                    e.printStackTrace();
                    listener.onRequestError("json解析错误",false);
                    return null;
                }

            }
        }
        if(_onParseListener!=null){
            _onParseListener.afterParseData(list,parseData);
        }

        return list;
    }

	@Override
	public void onSuccess() {
        List<T> list = null;
		if(listener!=null){
            list = parseList();
			listener.onRequestSuccess(list, page >= total);
		}

		/*
		if(waitMessage!=null){
			Alert.cancelWait();
		}*/
		
		if(isAutoNotify()){
            if(list==null){
                list = parseList();
            }
			Notifier.notifyObservers(job.getApi(),list,page>=total);
		}
		
	}

	public int getPosition() {
		return position;
	}

	/*
	@Override
	protected void makeCacheKey() {
		cacheKey = getDataKey().append(position).toString();
		if (conditions == null) {
			if (position <= LibConfig.StartPosition && visited) {
				forceToRefresh = true;
			} else {
				visited = true;
				forceToRefresh = false;
			}
		} 
	}*/

	@Override
	public void loadMore(int position) {
		setPosition(position);

		job.execute();

		//requestQueue.add(this);
	}
	
	
	@Override
	public void reload() {
		setPosition(LibConfig.StartPosition);
		super.reload();
	}

	@Override
	public void add(int index, T data) {
		//this.data.add(index, data);
		clearCache();
		postSuccess();
	}
	
	private void postSuccess(){
		ThreadUtil.post(new Runnable() {
			
			@Override
			public void run() {
				onSuccess();
			}
		});
	}

	@Override
	public void remove(T data) {
	//	this.data.remove(data);
		clearCache();
		postSuccess();
	}

	@Override
	public void update(T data) {
		clearCache();
		postSuccess();
	}



	@Override
	public List<T> array() {
        Object data = job.getData();

        if(data instanceof DMPage){
            return ((DMPage)data).getList();
        }else if(data instanceof  List){
            return (List<T>) data;
        }
        return null;
	}




}
