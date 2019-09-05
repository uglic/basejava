package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.generator.ResumeGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ResumeServlet extends javax.servlet.http.HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getSqlStorage();
        setupResumeControl(15);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getParameter("action");
        String uuid = request.getParameter("uuid");
        switch (action) {
            case "delete":
                doMakeDelete(uuid, request, response);
                return;
            case "view":
                doMakeView(uuid, request, response);
                return;
        }
        Resume resume;
        String fullName = request.getParameter("fullName");
        switch (action) {
            case "add":
                resume = new Resume(fullName);
                break;
            case "edit":
                resume = storage.get(uuid);
                resume.setFullName(fullName);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        for (ContactTypes type : ContactTypes.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, new Contact(value, type));
            } else {
                resume.getContacts().remove(type);
            }
        }
        Map<String, Set<String>> paramsSplitted = splitRequestKeys(request);
        for (SectionTypes type : SectionTypes.values()) {
            String simpleKey;
            String simpleValue;
            Set<String> values;
            switch (type) {
                case OBJECTIVE:
                case PERSONAL:
                    values = paramsSplitted.get(type.name());
                    if (values.isEmpty()) {
                        resume.getSections().remove(type);
                        continue;
                    }
                    simpleKey = type.name() + values.iterator().next();
                    simpleValue = request.getParameter(simpleKey);
                    if (simpleValue != null && simpleValue.trim().length() != 0) {
                        resume.addSection(type, new SimpleTextSection(simpleValue));
                    }
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    values = paramsSplitted.get(type.name());
                    if (values.isEmpty()) {
                        resume.getSections().remove(type);
                        continue;
                    }
                    List<String> list = new ArrayList<>();
                    for (String listKey : values) {
                        simpleKey = type.name() + "." + listKey;
                        simpleValue = request.getParameter(simpleKey);
                        list.add(simpleValue);
                    }
                    resume.addSection(type, new BulletedTextListSection(list));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    Organization organization;
                    Organization.Position position;
                    List<Organization> organizations = new ArrayList<>();
                    for (String orgNameKey : paramsSplitted.get(type.name() + "-name")) {
                        List<Organization.Position> history = new ArrayList<>();
                        for (String historyKey : paramsSplitted.get(type.name() + "-title")) {
                            if (historyKey.startsWith(orgNameKey + ".")) {
                                LocalDate startDate = DateUtil.parse(request.getParameter(type.name() + "-startDate" + "." + historyKey));
                                LocalDate endDate = DateUtil.parse(request.getParameter(type.name() + "-endDate" + "." + historyKey));
                                String title = request.getParameter(type.name() + "-title" + "." + historyKey);
                                String description = request.getParameter(type.name() + "-description" + "." + historyKey);
                                if (!title.isEmpty()) {
                                    position = new Organization.Position(startDate, endDate, title, description);
                                    history.add(position);
                                }
                            }
                        }
                        if (history.size() > 0) {
                            String orgName = request.getParameter(type.name() + "-name" + "." + orgNameKey);
                            String orgUrl = request.getParameter(type.name() + "-url" + "." + orgNameKey);
                            if (!orgName.isEmpty()) {
                                organization = new Organization(new Contact(orgName, orgUrl), history);
                                organizations.add(organization);
                            }
                        }
                    }
                    if (organizations.size() > 0) {
                        resume.addSection(type, new OrganizationSection(organizations));
                    }
                    break;
                default:
            }
        }
        if (action.equals("add")) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect(request.getRequestURI());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "delete":
                doMakeDelete(uuid, request, response);
                return;
            case "add":
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("WEB-INF/jsp/add.jsp").forward(request, response);
                return;
            case "view":
                doMakeView(uuid, request, response);
                return;
            case "edit":
                doGetEdit(uuid, request, response);
                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    private void doMakeDelete(String uuid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        storage.delete(uuid);
        response.sendRedirect(request.getRequestURI());
    }

    private void doMakeView(String uuid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetViewEdit(uuid, "view", request, response);
    }

    private void doGetEdit(String uuid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetViewEdit(uuid, "edit", request, response);
    }

    private void doGetViewEdit(String uuid, String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Resume resume = storage.get(uuid);
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "WEB-INF/jsp/view.jsp" : "WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private Map<String, Set<String>> splitRequestKeys(HttpServletRequest request) {
        Map<String, String[]> sourceMap = request.getParameterMap();
        Map<String, Set<String>> result = new HashMap<>();
        for (String key : sourceMap.keySet()) {
            int index = key.indexOf(".");
            String prefix;
            String numbers;
            if (index != -1) {
                numbers = key.substring(index + 1);
                prefix = key.substring(0, index);
            } else {
                numbers = "";
                prefix = key;
            }
            Set<String> numberSet = result.computeIfAbsent(prefix, k -> new HashSet<>());
            numberSet.add(numbers);
        }
        sortRequestSubKeys(result);
        return result;
    }

    private void sortRequestSubKeys(Map<String, Set<String>> result) {
        for (String key : result.keySet()) {
            Set<String> old = result.get(key);
            if (old.size() > 0) {
                String[] subkeys = new String[old.size()];
                old.toArray(subkeys);
                Arrays.sort(subkeys, this::compareSubKeys);
                result.put(key, new LinkedHashSet<>(Arrays.asList(subkeys)));
            }
        }
    }

    private int compareSubKeys(String key1, String key2) {
        if (key1 == null && key2 == null) return 0;
        if (key1 == null) return -1;
        if (key2 == null) return 1;

        int compare = getSubKeyAsInt(key1, 0) - getSubKeyAsInt(key2, 0);
        if (compare != 0) return compare;
        return getSubKeyAsInt(key1, 1) - getSubKeyAsInt(key2, 1);
    }

    private int getSubKeyAsInt(String subkey, int position) {
        int index;
        int section;
        index = subkey.indexOf(".");
        try {
            if (index == -1) {
                if (position <= 0) {
                    section = Integer.parseInt(subkey);
                } else {
                    section = 0;
                }
            } else {
                if (position <= 0) {
                    section = Integer.parseInt(subkey.substring(0, index));
                } else {
                    section = getSubKeyAsInt(subkey.substring(index + 1), position - 1);
                }
            }
        } catch (NumberFormatException e) {
            section = 0;
        }
        return section;
    }

    protected void setupResumeControl(final int resumeMinCount) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            int addCount = resumeMinCount - storage.size();
            if (addCount > 0) {
                for (int i = 0; i < addCount; i++) {
                    IGeneratorParameter gp = new IsManGeneratorParam(ThreadLocalRandom.current().nextBoolean());
                    final Resume resume = ResumeGenerator.getInstance().getRandom(gp);
                    List<String> names = new ArrayList<>();
                    storage.getAllSorted().forEach(r -> {
                        names.add(r.getFullName());
                    });
                    if (!names.contains(resume.getFullName())) {
                        storage.save(resume);
                    }
                }
            }
        }, 10, 20, TimeUnit.SECONDS);
    }
}
