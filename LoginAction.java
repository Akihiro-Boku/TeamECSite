package com.internousdev.neptune.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.neptune.dao.CartInfoDAO;
import com.internousdev.neptune.dao.MCategoryDAO;
import com.internousdev.neptune.dao.UserInfoDAO;
import com.internousdev.neptune.dto.CartInfoDTO;
import com.internousdev.neptune.dto.MCategoryDTO;
import com.internousdev.neptune.dto.UserInfoDTO;
import com.internousdev.neptune.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;


public class LoginAction extends ActionSupport implements SessionAware{

	private String categoryId;
	private String userId;
	private String password;
	private boolean savedUserIdFlg;
	private List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
	private List<String> userIdErrorMessageList = new ArrayList<String>();
	private List<String> passwordErrorMessageList = new ArrayList<String>();
	private List<String> databaseAcountIncorrectMessageList = new ArrayList<String>();
	private Map<String,Object>session;

	@SuppressWarnings("unchecked")
	public String execute(){
		if(session.isEmpty() || userId == null){
			return "sessionErr";
		}
		String result=ERROR;

		if(savedUserIdFlg==true){
			session.put("savedUserIdFlg", true);
			session.put("savedUserId",userId);
		}else{
			session.put("savedUserIdFlg",false);
			session.remove("userId");
			session.remove("savedUserId");
		}
		InputChecker inputChecker = new InputChecker();
		userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false,  false, true, false, false, false);
		passwordErrorMessageList =inputChecker.doCheck("パスワード", password, 1, 16, true, false,  false, true, false, false, false);

		if(userIdErrorMessageList.size()!=0
		|| passwordErrorMessageList.size()!=0){
			session.put("logined",0);
			return ERROR;
		}

		UserInfoDAO userInfoDAO = new UserInfoDAO();
		if(userInfoDAO.isExistsUserInfo(userId, password)){
			if(userInfoDAO.login(userId, password) > 0){

				List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

				cartInfoDTOList = (List<CartInfoDTO>)session.get("cartInfoDTOList");
				if(cartInfoDTOList != null){
					changeCartInfo(cartInfoDTOList);
				}

				if(session.containsKey("cartFlg")){
					session.remove("cartFlg");
					result = "cart";
				}else{
					result = SUCCESS;
				}
				UserInfoDTO userInfoDTO = userInfoDAO.getUserInfo(userId,password);
				session.put("userId", userInfoDTO.getUserId());
				session.put("logined", 1);
			}
		}else{
			databaseAcountIncorrectMessageList.add("ユーザーIDまたはパスワードが異なります。");
		}
		if(!session.containsKey("mCategoryDTOList")){
			MCategoryDAO mCategoryDAO = new MCategoryDAO();
			session.put("mCategoryDTOList",mCategoryDAO.getMCategoryList());
		}
		return result;
	}

	private void changeCartInfo(List<CartInfoDTO>cartInfoDTOList){
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		int count = 0;
		String tempUserId = session.get("tempUserId").toString();


		for(CartInfoDTO dto : cartInfoDTOList){
			if(cartInfoDAO.isExistsCartInfo(userId, dto.getProductId())){
				count += cartInfoDAO.updateProductCount(userId, dto.getProductId(), dto.getProductCount());
				cartInfoDAO.delete(String.valueOf(dto.getProductId()) , tempUserId);
			}else{
				count += cartInfoDAO.linkToUserId(tempUserId,userId,dto.getProductId());
			}
		}

		if(count == cartInfoDTOList.size()){
			List<CartInfoDTO> newCartInfoDTOList = cartInfoDAO.getCartInfoDTOList(userId);
			Iterator<CartInfoDTO> iterator = newCartInfoDTOList.iterator();
			if(!(iterator.hasNext())){
				newCartInfoDTOList = null;
			}
			session.put("cartInfoDTOList", newCartInfoDTOList);
		}
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSavedUserIdFlg() {
		return savedUserIdFlg;
	}
	public void setSavedUserIdFlg(boolean savedUserIdFlg) {
		this.savedUserIdFlg = savedUserIdFlg;
	}
	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}
	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}
	public List<String> getPasswordErrorMessageList() {
		return passwordErrorMessageList;
	}
	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList) {
		this.passwordErrorMessageList = passwordErrorMessageList;
	}
	public List<String> getDatabaseAcountIncorrectMessageList() {
		return databaseAcountIncorrectMessageList;
	}
	public void setDatabaseAcountIncorrectMessageList(List<String> databaseAcountIncorrectMessageList) {
		this.databaseAcountIncorrectMessageList = databaseAcountIncorrectMessageList;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}