package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends javax.servlet.http.HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume = storage.get(uuid);
        resume.setFullName(fullName);
        for (ContactTypes type : ContactTypes.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, new Contact(value, type));
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionTypes type : SectionTypes.values()) {
            if(!type.equals(SectionTypes.EXPERIENCE) && !type.equals(SectionTypes.EDUCATION)) {
                String [] values = request.getParameterValues(type.name());
                if(values == null || values.length == 0) {
                    resume.getSections().remove(type);
                    continue;
                }
                switch(type){
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new SimpleTextSection(values[0]));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> list = new ArrayList<>();
                        for (String value : values) {
                            if (value != null && value.trim().length() != 0) {
                                list.add(value);
                            }
                        }
                        resume.addSection(type, new BulletedTextListSection(list));
                        break;
                    default:
                }
            }
        }
        storage.update(resume);
        response.sendRedirect(request.getRequestURI());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect(request.getRequestURI());
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "WEB-INF/jsp/view.jsp" : "WEB-INF/jsp/edit.jsp")
        ).forward(request, response);

    }
}
