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
//Сервлет для регистрации пользователя.
@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String secpass = request.getParameter("secpass");
        UserProfile profile = accountService.getUserByLogin(login);
        accountService.addNewUser(new UserProfile(login, pass));
        accountService.addSession(request.getSession().getId(), profile);


        if (login == null || login.length() == 0 || pass == null || pass.length() == 0 || secpass == null || secpass.length() == 0) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Enter correct data");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String insert = "INSERT INTO userstable (login, pass) values ('" + login + "', '" + pass + "' )";

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
            Statement stm = con.prepareStatement(insert);
            stm.executeUpdate(insert);

        } catch (SQLException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (!pass.equals(secpass)) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Passwords are different");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            response.setContentType("text/html;charset=utf-8");
            response.sendRedirect("http://localhost:8080/apps.html");
            response.setStatus(HttpServletResponse.SC_OK);

            }
        }
    }