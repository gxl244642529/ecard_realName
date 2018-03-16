package com.citywithincity.models;

import com.citywithincity.interfaces.IDataProvider;
import com.citywithincity.interfaces.IDataProviderListener;

public abstract class AbsDataProvider<T> extends Observable<IDataProviderListener<T>> implements IDataProvider<T>{
	
	

}
