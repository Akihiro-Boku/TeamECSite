<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="header.jsp" >
  <jsp:param name="title" value="ログイン画面" />
</jsp:include>
      <div id="container">
      	<div id="page-title">
			<h1>Login</h1>
			<p>ログイン</p>
		</div>

			<s:form action="LoginAction">
				<div id="error">
					<s:if test="userIdErrorMessageList != null">
					<s:if test="!userIdErrorMessageList.isEmpty()">
						<div class="error-box">
								<s:iterator value="userIdErrorMessageList" >
									<p><s:property /></p>
								</s:iterator>
						</div>
					</s:if>
					</s:if>

					<s:if test="passwordErrorMessageList != null">
					<s:if test="!passwordErrorMessageList.isEmpty()">
						<div class="error-box">
								<s:iterator value="passwordErrorMessageList" >
									<p><s:property /></p>
								</s:iterator>
						</div>
					</s:if>
					</s:if>

					<s:if test="databaseAcountIncorrectMessageList!= null">
					<s:if test="!databaseAcountIncorrectMessageList.isEmpty()">
						<div class="error-box">
								<s:iterator value="databaseAcountIncorrectMessageList" >
									<p><s:property /></p>
								</s:iterator>
						 </div>
					</s:if>
					</s:if>
				</div>
				<div id="nep-vertical-table">
				 	<table>
						<tr>
							<th><s:label value="ユーザーID"/></th>
							<td>
								<s:if test="#session.savedUserId != null">
								<div id="form-text">
									<s:textfield name="userId" placeholder="ユーザーID" id="right-top-raduis" value='%{#session.savedUserId}' autocomplete="off"/>
								</div>
								</s:if>
								<s:else>
								<div id="form-text">
									<s:textfield name="userId" placeholder="ユーザーID" value='%{userId}' autocomplete="off"/>
								</div>
								</s:else>
							</td>
						</tr>
						<tr>
							<th><s:label value="パスワード"/></th>
							<td>
							 	<div id="form-text">
									<s:password name="password" placeholder="パスワード" autocomplete="off"/>
							 	</div>
							</td>
						</tr>
					</table>
				</div>
				<div id="user-save">
					<p>
						<s:if test="#session.savedUserIdFlg == false || #session.savedUserIdFlg == null">
							<s:checkbox name="savedUserIdFlg" />
						</s:if>
						<s:else>
							<s:checkbox name="savedUserIdFlg" checked="checked" />
						</s:else>
					ユーザーID保存
					</p>
				</div>
					<div id="submit">
						<div id="submit-btn">
							<s:submit value="ログイン" onclick="goLoginAction();"/>
						</div>
					</div>

				</s:form>
				<s:form action="CreateUserAction">
					<div id="submit">
						<div id="submit-btn">
							<s:submit value="新規ユーザー登録"/>
						</div>
					</div>
				</s:form>
				<s:form action="ResetPasswordAction">
					<div id="submit">
						<div id="submit-btn">
							<s:submit value="パスワード再設定"/>
						</div>
					</div>
				</s:form>
			 </div>
</body>
</html>