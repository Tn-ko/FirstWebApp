package servlets;
import accounts.AccountService;
import accounts.UserProfile;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
//Сервлет для авторизации пользователя и удаления аккаунта.
@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
    private final AccountService accountService;
    private String password;
    private String name;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();

        if (name == null || password == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else {
            accountService.deleteSession(sessionId);
            response.setContentType("text/html;charset=utf-8");
            response.sendRedirect("http://localhost:8080/index.html");
            response.getWriter().println("Goodbye!");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        String delete = "DELETE FROM  userstable WHERE login='" + name + "' AND pass ='" + password + "'";

            try {
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance());
                StringBuilder url = new StringBuilder();

                url.
                        append("jdbc:mysql://").        //db type
                        append("localhost:").           //host name
                        append("3306/").                //port
                        append("users?").          //db name
                        append("user=root&").          //login
                        append("password=pass");       //password

                System.out.println("URL: " + url + "\n");

                Connection con = DriverManager.getConnection(url.toString());
                Statement stm = con.prepareStatement(delete);
                stm.executeUpdate(delete);

            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        UserProfile profile = accountService.getUserByLogin(login);
        accountService.addSession(request.getSession().getId(), profile);

        String select = "SELECT * FROM  userstable WHERE login='" + login + "' AND pass ='" + pass + "'";
        ResultSet resultSet;
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance());
            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("users?").          //db name
                    append("user=root&").          //login
                    append("password=pass");       //password

            System.out.println("URL: " + url + "\n");
            Connection con = DriverManager.getConnection(url.toString());
            Statement stm = con.prepareStatement(select);
            resultSet = stm.executeQuery(select);
            while (resultSet.next()) {
                this.password = resultSet.getString("pass");
                this.name = resultSet.getString("login");
            }
        } catch (SQLException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (!pass.equals(password) && login.equals(name) ||  login.equals(name) && pass.length()==0 ) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Wrong password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (login.length() == 0 || pass.length() == 0) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Enter correct data");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!login.equals(name) && !pass.equals(password)  ) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("You don't registreted on the site");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (pass.equals(password) && login.equals(name)) {
            response.setContentType("text/html;charset=utf-8");
            response.sendRedirect("http://localhost:8080/authoruzed.html"); // используем редирект вместо переадресации, при ней пользователь остаётся на /signin.
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
