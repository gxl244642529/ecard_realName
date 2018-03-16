package com.citywithincity.interfaces;

public interface IFilter<T> {
	public static class AndFilter<T> implements IFilter<T>{
		private IFilter<T> filter1;
		private IFilter<T> filter2;
		public AndFilter(IFilter<T> filter1,IFilter<T> filter2){
			this.filter1 = filter1;
			this.filter2 = filter2;
		}
		@Override
		public boolean accept(T data) {
			return filter1.accept(data) && filter2.accept(data);
		}
	}
	public static class OrFilter<T> implements IFilter<T>{
		private IFilter<T> filter1;
		private IFilter<T> filter2;
		public OrFilter(IFilter<T> filter1,IFilter<T> filter2){
			this.filter1 = filter1;
			this.filter2 = filter2;
		}
		@Override
		public boolean accept(T data) {
			return filter1.accept(data) || filter2.accept(data);
		}
	}
	
	boolean accept(T data);
}
