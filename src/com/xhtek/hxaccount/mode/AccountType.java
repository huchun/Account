package com.xhtek.hxaccount.mode;

public class AccountType {

	     public long userId;
	     public int     userGroupId;
	     public long  updateTime;
	     public String deviceId;
	     public String token ;
	
	     public static  AccountType  mInstance;

		public synchronized static AccountType getInstance() {
			if (mInstance == null) {
				  mInstance = new AccountType();
			}
			return mInstance;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public int getUserGroupId() {
			return userGroupId;
		}

		public void setUserGroupId(int userGroupId) {
			this.userGroupId = userGroupId;
		}

		public long getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(long updateTime) {
			this.updateTime = updateTime;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}	     
}
