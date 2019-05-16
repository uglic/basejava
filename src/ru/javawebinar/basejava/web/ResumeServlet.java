package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        response.getWriter().write(getResumeListPage());
    }

    private String getResumeListPage() {
        final StringBuilder builder = new StringBuilder();
        buildHtmlHead(builder, "Список всех резюме")
                .append("<table>\r\n")
                .append("<tr><th>uuid</th><th>fullName</th></tr>\r\n");
        Config.get().getSqlStorage().getAllSorted().forEach(
                resume -> builder
                        .append("<tr>\r\n")
                        .append("<td>")
                        .append(resume.getUuid())
                        .append("</td>\r\n")
                        .append("<td>")
                        .append(resume.getFullName())
                        .append("</td>\r\n")
                        .append("</tr>\r\n")
        );
        builder.append("</table>\r\n");
        buildHtmlTail(builder);
        return builder.toString();
    }

    private StringBuilder buildHtmlHead(StringBuilder builder, String title) {
        String titleCleared = title
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        return builder.append("<!DOCTYPE html>\r\n")
                .append("<html lang=\"ru\">\r\n")
                .append("<head>\r\n")
                .append("<meta charset=\"utf-8\">\r\n")
                .append("<title>")
                .append(titleCleared)
                .append("</title>\r\n")
                .append("</head>\r\n")
                .append("<body>\r\n");
    }

    private StringBuilder buildHtmlTail(StringBuilder builder) {
        return builder.append("</body>\r\n")
                .append("</html>\r\n");
    }
}
