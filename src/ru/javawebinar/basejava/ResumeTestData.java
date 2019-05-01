package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;

public class ResumeTestData {

    private Resume getResumeWithTestData() {
        Resume resume = new Resume("Григорий Кислин");

        resume.addContact(new Contact(ContactTypesEnum.PHONE, "+7(921) 855-0482", null));
        resume.addContact(new Contact(ContactTypesEnum.SKYPE, "grigory.kislin", "skype:grigory.kislin"));
        resume.addContact(new Contact(ContactTypesEnum.EMAIL, "gkislin@yandex.ru", "mailto:gkislin@yandex.ru"));
        resume.addContact(new Contact(ContactTypesEnum.LINKEDIN, "Профиль LinkedIn", "https://www.linkedin.com/in/gkislin"));
        resume.addContact(new Contact(ContactTypesEnum.GITHUB, "Профиль GitHub", "https://github.com/gkislin"));
        resume.addContact(new Contact(ContactTypesEnum.STACKOVERFLOW, "Профиль Stackoverflow", "https://stackoverflow.com/users/548473"));
        resume.addContact(new Contact(ContactTypesEnum.HOMESITE, "Домашняя страница", "http://gkislin.ru/"));

        resume.addSection(new SectionSimple(SectionsEnum.OBJECTIVE,
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям",
                true)
        );
        resume.addSection(new SectionSimple(SectionsEnum.PERSONAL,
                "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.",
                false)
        );
        resume.addSection(new SectionListed(SectionsEnum.ACHIEVEMENT,
                        new SectionItemSimple("С 2013 года: разработка проектов \"Разработка Web приложения\","
                                + "\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). "
                                + "Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн "
                                + "стажировок и ведение проектов. Более 1000 выпускников."),
                        new SectionItemSimple("Реализация двухфакторной аутентификации для онлайн платформы "
                                + "управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, "
                                + "Jira, Zendesk."),
                        new SectionItemSimple("Налаживание процесса разработки и непрерывной интеграции ERP "
                                + "системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения "
                                + "управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO "
                                + "аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java "
                                + "сервера."),
                        new SectionItemSimple("Реализация c нуля Rich Internet Application приложения на стеке "
                                + "технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock "
                                + "для алгоритмического трейдинга."),
                        new SectionItemSimple("Создание JavaEE фреймворка для отказоустойчивого взаимодействия "
                                + "слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). "
                                + "Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. "
                                + "Реализация онлайн клиента для администрирования и мониторинга системы по JMX "
                                + "(Jython/ Django)."),
                        new SectionItemSimple("Реализация протоколов по приему платежей всех основных платежных "
                                + "системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) "
                                + "и Никарагуа.")
                )
        );
        resume.addSection(new SectionListed(SectionsEnum.QUALIFICATIONS,
                        new SectionItemSimple("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, "
                                + "WebLogic, WSO2"),
                        new SectionItemSimple("Version control: Subversion, Git, Mercury, ClearCase, Perforce"),
                        new SectionItemSimple("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), "
                                + "H2, Oracle,"),
                        new SectionItemSimple("MySQL, SQLite, MS SQL, HSQLDB"),
                        new SectionItemSimple("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, "
                                + "Groovy,"),
                        new SectionItemSimple("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,"),
                        new SectionItemSimple("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor"
                                + ", MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink)"
                                + ", Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, "
                                + "Eclipse SWT, JUnit, Selenium (htmlelements)."),
                        new SectionItemSimple("Python: Django."),
                        new SectionItemSimple("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js"),
                        new SectionItemSimple("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka"),
                        new SectionItemSimple("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, "
                                + "JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, "
                                + "AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT."),
                        new SectionItemSimple("Инструменты: Maven + plugin development, Gradle, настройка Ngnix,"),
                        new SectionItemSimple("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, "
                                + "JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer."),
                        new SectionItemSimple("Отличное знание и опыт применения концепций ООП, SOA, шаблонов"),
                        new SectionItemSimple("проектрирования, архитектурных шаблонов, UML, функционального"),
                        new SectionItemSimple("программирования"),
                        new SectionItemSimple("Родной русский, английский \"upper intermediate\"")
                )
        );
        resume.addSection(new SectionListed(SectionsEnum.EXPERIENCE,
                        new SectionItemPeriodContact("Автор проекта.",
                                LocalDate.of(2013, 10, 1), null,
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Java Online Projects",
                                        "http://javaops.ru/"),
                                "Создание, организация и проведение Java онлайн проектов и стажировок."),
                        new SectionItemPeriodContact("Старший разработчик (backend)",
                                LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Wrike",
                                        "https://www.wrike.com/"),
                                "Проектирование и разработка онлайн платформы управления проектами Wrike "
                                        + "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). "
                                        + "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."),
                        new SectionItemPeriodContact("Java архитектор",
                                LocalDate.of(2012, 4, 1), LocalDate.of(2014, 10, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "RIT Center",
                                        ""),
                                "Организация процесса разработки системы ERP для разных окружений: "
                                        + "релизная политика, версионирование, ведение CI (Jenkins), миграция базы "
                                        + "(кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA "
                                        + "via SSO. Архитектура БД и серверной части системы. Разработка "
                                        + "интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего "
                                        + "назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN "
                                        + "для online редактирование из браузера документов MS Office. Maven + "
                                        + "plugin development, Ant, Apache Commons, Spring security, Spring MVC, "
                                        + "Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell "
                                        + "remote scripting via ssh tunnels, PL/Python"),
                        new SectionItemPeriodContact("Ведущий программист",
                                LocalDate.of(2010, 12, 1), LocalDate.of(2012, 4, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Luxoft (Deutsche Bank)",
                                        "http://www.luxoft.ru/"),
                                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, "
                                        + "Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и "
                                        + "серверной части CRM. Реализация RIA-приложения для администрирования, "
                                        + "мониторинга и анализа результатов в области алгоритмического трейдинга. "
                                        + "JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."),
                        new SectionItemPeriodContact("Ведущий специалист",
                                LocalDate.of(2008, 6, 1), LocalDate.of(2010, 12, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Yota",
                                        "https://www.yota.ru/"),
                                "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные "
                                        + "Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, "
                                        + "JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и "
                                        + "мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, "
                                        + "Django, ExtJS)"),
                        new SectionItemPeriodContact("Разработчик ПО",
                                LocalDate.of(2007, 3, 1), LocalDate.of(2008, 6, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Enkata",
                                        "http://enkata.com/"),
                                "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, "
                                        + "Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."),
                        new SectionItemPeriodContact("Разработчик ПО",
                                LocalDate.of(2005, 1, 1), LocalDate.of(2007, 2, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Siemens AG",
                                        "https://www.siemens.com/ru/ru/home.html"),
                                "Разработка информационной модели, проектирование интерфейсов, реализация "
                                        + "и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."),
                        new SectionItemPeriodContact("Инженер по аппаратному и программному тестированию",
                                LocalDate.of(1997, 9, 1), LocalDate.of(2005, 1, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Alcatel",
                                        "http://www.alcatel.ru/"),
                                "Тестирование, отладка, внедрение ПО цифровой телефонной станции "
                                        + "Alcatel 1000 S12 (CHILL, ASM).")
                )
        );
        resume.addSection(new SectionListed(SectionsEnum.EDUCATION,
                        new SectionItemPeriodContact("\"Functional Programming Principles in "
                                + "Scala\" by Martin Odersky",
                                LocalDate.of(2013, 3, 1), LocalDate.of(2013, 5, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Coursera",
                                        "https://www.coursera.org/course/progfun"),
                                ""),
                        new SectionItemPeriodContact("Курс \"Объектно-ориентированный анализ ИС. "
                                + "Концептуальное моделирование на UML.\"",
                                LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Luxoft",
                                        "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"),
                                ""),
                        new SectionItemPeriodContact("3 месяца обучения мобильным IN сетям (Берлин)",
                                LocalDate.of(2005, 1, 1), LocalDate.of(2005, 4, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Siemens AG",
                                        "http://www.siemens.ru/"),
                                ""),
                        new SectionItemPeriodContact("6 месяцев обучения цифровым телефонным сетям (Москва)",
                                LocalDate.of(1997, 9, 1), LocalDate.of(1998, 3, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Alcatel",
                                        "http://www.alcatel.ru/"),
                                ""),
                        new SectionItemPeriodContact("Аспирантура (программист С, С++)",
                                LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Санкт-Петербургский национальный исследовательский университет "
                                                + "информационных технологий, механики и оптики",
                                        "http://www.ifmo.ru/"),
                                ""),
                        new SectionItemPeriodContact("Закончил с отличием",
                                LocalDate.of(1984, 9, 1), LocalDate.of(1987, 6, 1),
                                new Contact(ContactTypesEnum.HOMESITE,
                                        "Заочная физико-техническая школа при МФТИ",
                                        "http://www.school.mipt.ru/"),
                                "")
                )
        );
        return resume;
    }

    public static void main(String[] args) {
        Resume resume = new ResumeTestData().getResumeWithTestData();
        System.out.println(resume.getAsHtml());
    }
}
