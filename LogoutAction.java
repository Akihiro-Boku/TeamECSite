package com.internousdev.neptune.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.neptune.dao.MCategoryDAO;
import com.internousdev.neptune.dao.UserInfoDAO;
import com.internousdev.neptune.dto.MCategoryDTO;
import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware{

	private String categoryId;
	private List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
	
	private Map<String,Object>session;

	public String execute(){

		String result = SUCCESS;
		UserInfoDAO userInfoDAO = new UserInfoDAO();
		String userId = String.valueOf(session.get("userId"));
		boolean savedUserIdFlg = Boolean.valueOf(String.valueOf(session.get("savedUserIdFlg")));
		int count = userInfoDAO.logout(userId);

		if(count > 0){
			session.clear();
			if(savedUserIdFlg){
				session.put("savedUserIdFlg", true);
				session.put("savedUserId", userId);	
			}
		}
		if(!session.containsKey("mCategoryList")){
			MCategoryDAO mCategoryDAO = new MCategoryDAO();
			mCategoryDTOList = mCategoryDAO.getMCategoryList();
			session.put("mCategoryDTOList",mCategoryDTOList);
		}
		return result;
	}

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public List<MCategoryDTO> getmCategoryDTOList() {
		return mCategoryDTOList;
	}
	public void setmCategoryDTOList(List<MCategoryDTO> mCategoryDTOList) {
		this.mCategoryDTOList = mCategoryDTOList;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
