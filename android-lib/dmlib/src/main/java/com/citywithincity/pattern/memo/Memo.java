package com.citywithincity.pattern.memo;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录模式,支持undo、redo
 * @author Randy
 *
 * @param <T>
 */
public class Memo<T> {
	private List<T> datas;
	private int pointer;
	private IMemo<T> listener;
	
	public interface IMemo<T>{
		void releaseMemoData(T data);
	}
	
	public Memo(IMemo<T> listener){
		datas = new ArrayList<T>();
		pointer = 0;
		this.listener = listener;
	}
	
	
	/**
	 * 是否可以撤销
	 * @return
	 */
	public boolean canUndo(){
		return pointer > 1;
	}
	
	/**
	 * 是否可以重做
	 * @return
	 */
	public boolean canRedo(){
		return pointer < datas.size();
	}
	
	/**
	 * 重做
	 */
	public T undo(){
		pointer--;
		return datas.get(pointer-1);
	}
	
	/**
	 * 撤销
	 */
	public T redo(){
		pointer ++;
		return datas.get(pointer-1);
	}
	
	/**
	 * 获取上一个
	 * @return
	 */
	public T getPrevious(){
		if(pointer>=2){
			return datas.get(pointer-2);
		}
		return null;
		
	}
	
	public void clear(){
		int index = 0;
		for (T iterable_element : datas) {
			if(index!=pointer-1){
				listener.releaseMemoData(iterable_element);
			}
			index++;
		}
		pointer = 0;
		datas.clear();
	}
	
	/**
	 * 
	 * @param data
	 */
	public void add(T data){
		if(datas.size() > pointer){
			//remove
			for(int i = datas.size()-1; i > pointer; --i){
				T d = datas.remove(i);
				listener.releaseMemoData(d);
			}
		}
		datas.add(data);
		pointer = datas.size();
	}
}
