package com.xhtek.hxaccount.util;

import com.xhtek.hxaccount.http.BaseRequest;

public interface BaseDAOListener {
	public void loadSuccess(BaseRequest dao);
	public void loadFailure(BaseRequest dao);
}
