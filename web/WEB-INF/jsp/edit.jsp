<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="ru.javawebinar.basejava.model.Organization.Position" %>
<%@ page import="ru.javawebinar.basejava.model.Organization" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.BulletedTextListSection" %>
<%@ page import="ru.javawebinar.basejava.model.AbstractSection" %>
<%@ page import="ru.javawebinar.basejava.model.SimpleTextSection" %>
<%@ page import="ru.javawebinar.basejava.model.ContactTypes" %>
<%@ page import="ru.javawebinar.basejava.model.SectionTypes" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type", content="text/html;charset=utf-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${HtmlUtil.toEntityValue(resume.fullName)}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactTypes.values()%>">
            <dl>
                <dt>${type.getPrefix()}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${HtmlUtil.toEntityValue(resume.getContacts().get(type).getName())}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.getSections()}">
            <c:set var="sectionType" value="${sectionEntry.getKey()}"/>
            <c:set var="section" value="${sectionEntry.getValue()}"/>
            <tr>
                <td colspan="2"><h2>${sectionType.getTitle()}</h2></td>
            </tr>
            <c:if test="${sectionType.equals(SectionTypes.OBJECTIVE)}">
            <tr>
                <td colspan="2"><input type="text" size=2000 name="${sectionType.name()}" value="${HtmlUtil.toEntityValue(section.getContent())}"><br/></td>
            </tr>
            </c:if>
            <c:if test="${sectionType.equals(SectionTypes.PERSONAL)}">
            <tr>
                <td colspan="2"><input type="text" size=2000 name="${sectionType.name()}" value="${HtmlUtil.toEntityValue(section.getContent())}"><br/></td>
            </tr>
            </c:if>
            <c:if test="${sectionType.equals(SectionTypes.ACHIEVEMENT) || sectionType.equals(SectionTypes.QUALIFICATIONS)}">
            <tr>
                <td colspan="2">
                    <ul>
                        <c:forEach var="listItem" items="${section.getItems()}">
                                <li><input type="text" size=2000 name="${sectionType.name()}" value="${HtmlUtil.toEntityValue(listItem)}"></li>
                        </c:forEach>
                        <li><input type="text" size=2000 name="${sectionType.name()}" value=""></li>
                    </ul>
                </td>
            </tr>
            </c:if>
            <c:if test="${sectionType.equals(SectionTypes.EXPERIENCE) || sectionType.equals(SectionTypes.EDUCATION)}">
                <c:forEach var="organization" items="${section.getOrganizations()}">
                    <tr>
                        <td colspan="2"><h3>${organization.getContact().toHtml()}</h3></td>
                    </tr>
                    <c:forEach var="position" items="${organization.getHistory()}">
                        <tr>
                            <td width="15%" style="vertical-align: top">${DateUtil.to(position.getStartDate())} - ${DateUtil.to(position.getEndDate())}</td>
                            <td><b>${position.getTitle()}</b><br>${position.getDescription()}</td>
                        </tr>
                    </c:forEach>
                </c:forEach>
            </c:if>
        </c:forEach>
        </table>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
