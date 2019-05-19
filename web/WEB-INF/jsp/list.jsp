<%@ page import="ru.javawebinar.basejava.model.ContactTypes" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type", content="text/html;charset=utf-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume"/>
            <tr>
                <td><a href="?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${resume.contacts.get(ContactTypes.EMAIL).toHtml(ContactTypes.EMAIL)}</td>
                <td><a href="?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a></td>
                <td><a href="?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
