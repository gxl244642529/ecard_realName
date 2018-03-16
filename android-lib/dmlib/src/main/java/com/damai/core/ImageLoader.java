package com.damai.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.ThreadUtil;
import com.damai.auto.LifeManager;

public class ImageLoader extends DefaultJobDelivery<HttpJobImpl> implements IMessageListener ,JobHandler<JobImpl>{
	private Set<HttpJobImpl> set;
	private Map<ImageView, HttpJobImpl> loadingMap ;
	private ImageCache imageCache;
	private JobManagerImpl manager;
	SimpleJobQueue jobQueue;
	public ImageLoader(JobManagerImpl manager) {
		super(manager);
		this.manager = manager;
		set = new HashSet<HttpJobImpl>();
		loadingMap = new HashMap<ImageView, HttpJobImpl>();
		imageCache = new ImageCacheImpl();
		jobQueue = new SimpleJobQueue(this);
		jobQueue.start();
	}
	@Override
	public void onJobSuccess(HttpJobImpl job) {
		if(set.size()==0){
			MessageUtil.sendMessage(0, this, 200);
		}
		set.add((HttpJobImpl)job);
	}
	
	
	@Override
	public void onMessage(int message) {
		for (HttpJobImpl job : set) {
			ImageView imageView = job.getExtra();
			if(job.isCanceled()){
				loadingMap.remove(imageView);
				continue;
			}
			Bitmap data = job.getData();
			imageCache.put(job.url, data);
			imageView.setImageBitmap(data);
			loadingMap.remove(imageView);
			life.onRemoveJob(job);
		}
		set.clear();
	}
	
	public void onDestroy(Object obj){
		for (Entry<ImageView, HttpJobImpl> entry : loadingMap.entrySet()) {
			if(entry.getValue().getRelated() == obj){
				loadingMap.remove(entry.getKey());
			}
		}
	}
	
	class LoadingLocalImage extends JobImpl{
		String url;
		ImageView imageView;
		public LoadingLocalImage(String url, ImageView imageView){
			this.url=url;
			this.imageView = imageView;
		}
	}
	
	public void loadImage(String url, ImageView imageView) {
		if(url==null){
			return;
		}
		
		if(url.startsWith("/")){
			jobQueue.add(new LoadingLocalImage(url,imageView));
			return;
		}
	
		Bitmap bitmap = imageCache.get(url);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			return;
		}
		/**
		 * 判断本图片对应的下载有没有
		 */
		HttpJobImpl job = loadingMap.get(imageView);
		if(job!=null){
			if(url.equals(job.url)){
				return;
			}
			job.cancel();
		}
		
		job = new HttpJobImpl();
		job.extra = imageView;
		loadingMap.put(imageView, job);
		job.cachePolicy = CachePolicy.CachePolity_Permanent;
		job.dataType = DataTypes.DataType_Image;
		job.setHandlerType(HandlerTypes.HandlerType_Http);
		job.setDeliverType(DeliverTypes.DeliverType_Image);
		job.setRelated(LifeManager.getCurrent());
		job.url = url;
		//清空掉
		imageView.setImageDrawable(null);
		manager.addJob(job);
	}
	
	@Override
	public void handleJob(JobImpl job) {
		synchronized (ImageDataParser.lock) {
			
			final LoadingLocalImage loadingLocalImage = (LoadingLocalImage) job;
			final Bitmap bitmap = BitmapFactory.decodeFile(loadingLocalImage.url);
			ThreadUtil.post(new Runnable() {
				
				@Override
				public void run() {
					loadingLocalImage.imageView.setImageBitmap(bitmap);
				}
			});
			
		}
	}
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
}
