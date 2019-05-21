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
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h3>Редактирование резюме</h3>
    <form method="post" action="resume?action=edit" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Полное имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${HtmlUtil.toEntityValue(resume.fullName)}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactTypes.values()%>">
            <dl>
                <dt>${type.getPrefix()}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${HtmlUtil.toEntityValue(resume.getContacts().get(type).getName())}"></dd>
            </dl>
        </c:forEach>
        <c:forEach var="sectionType" items="${SectionTypes.values()}">
            <c:set var="section" value="${resume.getSections().get(sectionType)}"/>
            <div>
                <h2>${sectionType.getTitle()}</h2>
                <div>

                    <c:if test="${sectionType.equals(SectionTypes.OBJECTIVE) || sectionType.equals(SectionTypes.PERSONAL)}">
                        <div id="${sectionType.name()}">
                            <textarea size="2000" rows="1" name="${sectionType.name()}"><c:if test="${section != null}">${HtmlUtil.toEntityValue(section.getContent())}</c:if></textarea>
                        </div>
                    </c:if>

                    <c:if test="${sectionType.equals(SectionTypes.ACHIEVEMENT) || sectionType.equals(SectionTypes.QUALIFICATIONS)}">
                        <c:if test="${empty section.getItems()}">
                          <c:set var="section" value="${BulletedTextListSection.getEmpty()}"/>
                        </c:if>
                        <ul>
                            <c:set var="counter" value="0"/>
                            <c:forEach var="listItem" items="${section.getItems()}">
                                <c:set var="counter" value="${counter + 1}"/>
                                <li id="${sectionType.name()}.${counter}">
                                    <textarea size="2000" rows="1" name="${sectionType.name()}" class="w95">${HtmlUtil.toEntityValue(listItem)}</textarea>
                                    <a class="delete del:${sectionType.name()}" href=""><img src="img/delete.png"></a>
                                </li>
                            </c:forEach>
                        </ul>
                        <div class="row">${sectionType.getTitle()}: <a class="add:${sectionType.name()}" href="">добавить строку</a></div>
                    </c:if>

                    <c:if test="${sectionType.equals(SectionTypes.EXPERIENCE) || sectionType.equals(SectionTypes.EDUCATION)}">
                        <div>
                            <c:if test="${empty section.getOrganizations()}">
                              <c:set var="section" value="${OrganizationSection.getEmpty()}"/>
                            </c:if>
                            <c:set var="counterOrg" value="0"/>
                            <c:forEach var="organization" items="${section.getOrganizations()}">
                                <c:set var="counterOrg" value="${counterOrg + 1}"/>
                                <div id="${sectionType.name()}-org.${counterOrg}">
                                    <hr/>
                                    <div class="col-15">Организация:</div>
                                    <div class="col-85">
                                        <input type="text" size="2000" name="${sectionType.name()}.name" class="w95" value="${organization.getContact().getName()}">
                                        <a class="delete del:${sectionType.name()}-org" href=""><img src="img/delete.png"></a>
                                    </div>
                                    <div class="row">
                                        <div class="col-15">URL:</div>
                                        <div class="col-85">
                                            <textarea size="2000" rows="1" name="${sectionType.name()}.url">${organization.getContact().getUrl()}</textarea>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <c:set var="counterHist" value="0"/>
                                        <c:forEach var="position" items="${organization.getHistory()}">
                                            <c:set var="counterHist" value="${counterHist + 1}"/>
                                            <div id="${sectionType.name()}-period.${counterOrg}.${counterHist}">
                                                <div class="row">
                                                    <div class="col-15"></div>
                                                    <div class="col-15">Период:</div>
                                                    <div class="col-70">
                                                        <input type="date" name="${sectionType.name()}.startDate" value="${position.getStartDate()}"> - <input type="date" name="${sectionType.name()}.endDate" value="${position.getEndDate()}">
                                                        <a class="delete del:${sectionType.name()}-period" href=""><img src="img/delete.png"></a>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-15"></div>
                                                    <div class="col-15">Должность:</div>
                                                    <div class="col-70">
                                                        <input type="text" size="250" name="${sectionType.name()}.title" value="${position.getTitle()}">
                                                    </div>
                                                </div>
                                                <c:if test="${sectionType.equals(SectionTypes.EXPERIENCE)}">
                                                    <div class="row">
                                                        <div class="col-15"></div>
                                                        <div class="col-15">Обязанности:</div>
                                                        <div class="col-70">
                                                            <textarea size="2000" name="${sectionType.name()}.description">${position.getDescription()}</textarea>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="row">${sectionType.getTitle()}.Период: <a class="add:${sectionType.name()}-period" href="">добавить строку</a></div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="row">${sectionType.getTitle()}: <a class="add:${sectionType.name()}-org" href="">добавить строку</a></div>
                    </c:if>

                </div>
            </div>
        </c:forEach>
        <hr>
        <button type="submit">Создать</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<script type="text/javascript" src="js/some.js"></script>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
