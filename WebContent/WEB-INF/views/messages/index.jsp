<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name ="content">
        <h2>メッセージ一覧</h2>
        <ul>
            <c:forEach var = "message" items ="${messages}">
                <li>
                    <a href="${pageContext.request.contextPath}/show?id=${message.id}">
                        <c:out value="${message.id}"/>
                    </a>
                    :<c:out value="${message.title}"></c:out>&gt;<c:out value="${message.content}" />
                </li>
            </c:forEach>
        </ul>
        
        <p><a href="${pageContext.request.contextPath}/new">新規メッセージの投稿</a></p>

    </c:param>
</c:import>