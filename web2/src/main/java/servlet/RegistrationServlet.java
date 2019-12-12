package servlet;

import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.getWriter().println(PageGenerator.getPageGenerator().getPage("registerPage.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = UserService.getUserService();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            User user = new User(email, password);
            if (userService.addUser(user)) {
                // Веруть страницу успешной регестрации
                response.getWriter().println("Good Job");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Карта пользователей не должна содержать пользователей с одинаковым id.
                response.getWriter().println("Пользователь с таким ID уже есть");
            }
        }
    }
}
