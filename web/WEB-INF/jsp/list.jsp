<%@ page import="ru.javawebinar.basejava.model.ContactTypes"%>
<%@ page import="ru.javawebinar.basejava.model.Resume"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=utf-8" language="java"%>
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
        </tr>
        <%
            for(Resume resume : (List<Resume>) request.getAttribute("resumes")) {
        %><tr>
            <td><a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a></td>
            <td><%=resume.getContacts().get(ContactTypes.EMAIL).getName()%></td>
        </tr>
        <%
            }
        %></table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
