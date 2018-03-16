package com.damai.auto;

import android.app.Activity;
import android.content.Context;

import com.citywithincity.interfaces.IViewContainer;


/**
 * activity、fragment生命周期管理
 * @author Randy
 *
 */
public class LifeManager {
	private static IViewContainer current;
	//初始化栈大小
	private static final int INIT_STACK_SIZE = 10;
	
	//加载栈，通过加载栈管理
	private static Object[] stack;
	//元素多少
	private static int count;
	
	static{
		stack = new Object[INIT_STACK_SIZE];
	}

	public static Activity getActivity(){
		if(current!=null){
			return current.getActivity();
		}
		for(int i= count-1; i >=0 ; --i){
			if(stack[i] instanceof Activity){
				//从i起查找一个Activity
				return (Activity)stack[i];
			}
		}
		return null;

	}


	private static Context _context;
	
	public static void setApplicationContext(Context context){
		_context = context.getApplicationContext();
	}
	
	public static Context getApplicationContext(){
		return _context;
	}
	
	private static void expand(){
		Object[] newArray = new Object[stack.length*2];
		System.arraycopy(stack, 0, newArray, 0, stack.length);
		stack = newArray;
	}
	
	
	/**
	 * 这个只能在onCreate中或者onFinishInflate中调用，其他不应该调用这个
	 * @return
	 */
	public static IViewContainer getCurrent(){
		return current;
	}
	
	public static void onCreate(IViewContainer target){
		current = target;
		int nCount = count+1;
		if(nCount>stack.length){
			expand();
		}
		stack[count] = target;
		count = nCount;
	}
	
	
	public static void closeAll(){
		
		for(int i= count-1; i >=0 ; --i){
			if(stack[i] instanceof Activity){
				((Activity)stack[i]).finish();
			}
		}
		
	}
	
	
	public static Activity previous(){
		return findPrevious(getActivity());
	}
	
	public static void onResume(IViewContainer target){
		current = target;
	}
	
	public static void onDestroy(IViewContainer target){
		if(target==current){
			current = null;
		}
		for(int i= count-1; i >=0 ; --i){
			if(stack[i]==target){
				--count;
				stack[i] = null;
				if(i<count){
					//将后面的移动到前面
					move(i);
				}
				break;
			}
		}
	}
	
	private static void move(int position){
		int i = position;
		for(i=position; i < count; ++i){
			stack[i] = stack[i+1];
		}
		stack[i]=null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Activity> T popupTo(Class<T> clazz){
		for(int i= count-1; i >=0 ; --i){
			if(clazz.isAssignableFrom(stack[i].getClass())){
				//从i起查找一个Activity
				LifeManager.popupTo((Context)stack[i]);
				return (T)stack[i];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Activity> T findActivity(Class<T> clazz){
		for(int i= count-1; i >=0 ; --i){
			if(clazz.isAssignableFrom(stack[i].getClass())){
				//从i起查找一个Activity
				return (T)stack[i];
			}
		}
		return null;
	}
	
	/**
	 * 查找上一个Activity
	 * @param context
	 * @return
	 */
	public static Activity findPrevious(Context context){
		
		for(int i= count-1; i >=0 ; --i){
			if(stack[i]==context){
				//从i起查找一个Activity
				if(i>0){
					
					for(int j=i-1 ; j >=0 ; --j){
						if(stack[j] instanceof Activity){
							return (Activity)stack[j];
						}
					}
					
				}
				break;
			}
		}
		return null;
	}
	
	/**
	 * 将context之后面的activity全部销毁
	 * @param context
	 */
	public static void popupTo(Context context){
		for(int i= count-1; i >=0 ; --i){
			if(stack[i]==context){
				for(int j=count-1; j>i ; --j){
					if(stack[j] instanceof Activity){
						((Activity)stack[j]).finish();
					}
				}
				break;
			}
		}
	}
}
