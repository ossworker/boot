package com.workoss.boot.storage;

import com.amazonaws.http.IdleConnectionReaper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class StorageTemplate extends BaseStorageTemplate implements InitializingBean, DisposableBean {

	@Override
	public void destroy() throws Exception {
		super.destroy();
		IdleConnectionReaper.shutdown();
	}
}
