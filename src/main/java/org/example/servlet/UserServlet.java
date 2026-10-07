package org.example.servlet;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = parseId(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        if (id == null || name == null || email == null || mobile == null) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    message("Some fields are missing"));
            return;
        }

        if (userService.getUserById(id) != null) {
            writeJson(response, HttpServletResponse.SC_CONFLICT,
                    message("User with this id already exists"));
            return;
        }

        userService.createUser(new User(id, name, email, mobile));

        writeJson(response, HttpServletResponse.SC_CREATED,
                message("User added successfully"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            List<User> users = userService.getAllUsers();
            writeJson(response, HttpServletResponse.SC_OK, usersToJson(users));
            return;
        }

        Integer id = parseId(idParam);
        if (id == null) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    message("Invalid id"));
            return;
        }

        User user = userService.getUserById(id);
        if (user == null) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    message("User not found"));
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK, userToJson(user));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = parseId(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        if (id == null || name == null || email == null || mobile == null) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    message("Some fields are missing"));
            return;
        }

        User updated = userService.updateUser(id, new User(id, name, email, mobile));
        if (updated == null) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    message("User not found"));
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK,
                message("User updated successfully"));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = parseId(request.getParameter("id"));
        if (id == null) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    message("Invalid id"));
            return;
        }

        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    message("User not found"));
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK,
                message("User deleted successfully"));
    }

    private Integer parseId(String idParam) {
        if (idParam == null) {
            return null;
        }
        try {
            return Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void writeJson(HttpServletResponse response, int status, String json) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private String message(String text) {
        return "{\n    \"message\": \"" + escape(text) + "\"\n}";
    }

    private String userToJson(User user) {
        return "{\n" +
                "    \"id\": " + user.getId() + ",\n" +
                "    \"name\": \"" + escape(user.getName()) + "\",\n" +
                "    \"email\": \"" + escape(user.getEmail()) + "\",\n" +
                "    \"mobile\": \"" + escape(user.getMobile()) + "\"\n" +
                "}";
    }

    private String usersToJson(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(userToJson(users.get(i)));
            if (i != users.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
