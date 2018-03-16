package com.citywithincity.interfaces;

import java.io.File;

public interface IDownloadListener  extends IRequestResult<File>{
	void onProgress(int total,int current);
}
