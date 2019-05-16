package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.BulletedTextListSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SimpleTextSection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter w = response.getWriter();
        String uuid = request.getParameter("uuid");
        if (uuid == null) {
            response.getWriter().write(getResumeListPage());
        } else {
            try {
                Resume resume = Config.get().getSqlStorage().get(uuid);
                response.getWriter().write(getResumePage(resume));
            } catch (NotExistStorageException e) {
                response.getWriter().write(getErrorResumePage(uuid));
            }
        }
    }

    private String getResumePage(Resume resume) {
        final StringBuilder builder = new StringBuilder();
        buildHtmlHead(builder, "[" + resume.getFullName() + "] резюме человека и гражданина");
        builder
                .append("<a href=\"?\">Вернуться к списку всех резюме</a>")
                .append("<h1>")
                .append(resume.getFullName())
                .append("</h1>\r\n")

                .append("<h5>")
                .append("uuid: ")
                .append(resume.getUuid())
                .append("</h5>\r\n")
                .append("<br>\r\n")

                .append("<h3>Contacts</h3>\r\n")
                .append("<table>\r\n")
                .append("<tr><th>Type</th><th>Name</th><th>Url</th></tr>\r\n");

        resume.getContacts().forEach((type, contact) -> builder
                .append("<tr>\r\n")
                .append("<td>")
                .append(type.name())
                .append("</td>\r\n")
                .append("<td>")
                .append(contact.getName())
                .append("</td>\r\n")
                .append("<td>")
                .append("<a href=\"")
                .append(contact.getUrl())
                .append("\">")
                .append(contact.getUrl())
                .append("</a>")
                .append("</td>\r\n")
                .append("</tr>\r\n"));

        builder.append("</table>\r\n")
                .append("<br>\r\n");

        resume.getSections().forEach((type, section) -> {
            builder.append("<h3>");
            builder.append(type.getTitle());
            builder.append("</h3>\r\n");
            switch (type) {
                case OBJECTIVE:
                case PERSONAL:
                    SimpleTextSection section1 = (SimpleTextSection) section;
                    builder.append("<p>");
                    builder.append(section1.getContent());
                    builder.append("</p>\r\n");
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    builder.append("<ul>");
                    BulletedTextListSection section2 = (BulletedTextListSection) section;
                    section2.getItems().forEach(item -> {
                        builder.append("<li>");
                        builder.append(item);
                        builder.append("</li>\r\n");
                    });
                    builder.append("</ul>");
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    throw new StorageException("TODO::Unknown section: " + type);
                default:
                    throw new StorageException("Unknown section: " + type);
            }
        });

        buildHtmlTail(builder);
        return builder.toString();
    }

    private String getResumeListPage() {
        final StringBuilder builder = new StringBuilder();
        buildHtmlHead(builder, "Список всех резюме");

        builder.append("<table>\r\n");
        builder.append("<tr><th>Full name</th><th>Uuid</th></tr>\r\n");
        Config.get().getSqlStorage().getAllSorted().forEach(
                resume -> {
                    builder.append("<tr>\r\n");
                    builder.append("<td>");
                    builder.append(resume.getFullName());
                    builder.append("</td>\r\n");
                    builder.append("<td>");
                    builder.append("<a href=\"?uuid=");
                    builder.append(resume.getUuid());
                    builder.append("\">");
                    builder.append(resume.getUuid());
                    builder.append("</a>");
                    builder.append("</td>\r\n");
                    builder.append("</tr>\r\n");
                }
        );
        builder.append("</table>\r\n");

        buildHtmlTail(builder);
        return builder.toString();
    }

    private String getErrorResumePage(String uuid) {
        final StringBuilder builder = new StringBuilder();
        buildHtmlHead(builder, "Ошибка: несуществующее резюме uuid = {" + uuid + "}");

        builder.append("<h2>Резюме с кодом ");
        builder.append(uuid);
        builder.append(" не существует</h2>");
        builder.append("<p>Ошибка природы ваш запрос.</p>");
        builder.append("<br>");
        builder.append("<a href=\"?\">Вернуться к списку всех резюме</a>");

        buildHtmlTail(builder);
        return builder.toString();
    }

    private StringBuilder buildHtmlHead(StringBuilder builder, String title) {
        builder.append("<!DOCTYPE html>\r\n");
        builder.append("<html lang=\"ru\">\r\n");
        builder.append("<head>\r\n");
        builder.append("<meta charset=\"utf-8\">\r\n");
        builder.append("<title>");
        String titleCleared = title
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        builder.append(titleCleared);
        builder.append("</title>\r\n");
        builder.append("<link media=\"all\" rel=\"stylesheet\" href=\"css/style.css\" />\r\n");
        builder.append("</head>\r\n");
        builder.append("<body>\r\n");
        return builder;
    }

    private StringBuilder buildHtmlTail(StringBuilder builder) {
        builder.append("</body>\r\n");
        builder.append("</html>\r\n");
        return builder;
    }
}
