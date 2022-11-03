package servlets;

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

/**
 *
 * @author Hanin
 */
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        UserService user_service = new UserService();
        RoleService user_role = new RoleService();
        List<User> users;
        List<Role> roles;
        int id = 0;
        try {
            HttpSession session = request.getSession();

            users = user_service.getAll();
            roles = user_role.getAll();
            request.setAttribute("users", users);
            for (User user : users) {
                id = user.getRole().getRoleId();

            }
            request.setAttribute("id", id);
            request.setAttribute("roles", roles);

        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
if (action == null){
     getServletContext().getRequestDispatcher("/WEB-INF/Manager.jsp")
                .forward(request, response);
     return;
}
        if (action != null && action.equals("edit")) {
            try {
                String email = request.getParameter("userEmail");
                 request.setAttribute("email", email);
                User user = user_service.getUser(email);
                request.setAttribute("selectedUser", user);
                  getServletContext().getRequestDispatcher("/WEB-INF/Manager.jsp")
                .forward(request, response);
                     return;
                
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action != null && action.equals("delete")) {
            try {
                String email = request.getParameter("userEmail");
                request.setAttribute("email", email);

                  user_service.delete(email);
                  response.sendRedirect("/");
                     


            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        UserService user_service = new UserService();
        RoleService user_role = new RoleService();

        String action = request.getParameter("action");
        String email = request.getParameter("email");
        List<User> users;
        List<Role> roles;
        String firstName = request.getParameter("firstName");
        request.setAttribute("firstName", firstName);
        String lastName = request.getParameter("lastName");
        request.setAttribute("lastName", lastName);
        String password = request.getParameter("password");
        request.setAttribute("password", password);
        int id = 0;
         
        String role = request.getParameter("role");
        if (role.equals("system admin")) {
            id = 1;
        } else {
            id = 2;
        }
        Role newRole = new Role(id, role);
        request.setAttribute("newRole", newRole);
        if (action != null && action.equals("edit")) {
            try {
                String UserEditEmail = request.getParameter("userEmail");
                request.setAttribute("UserEditEmail", UserEditEmail);
                User userUpdated = new User(UserEditEmail ,firstName,lastName,password, newRole );
                request.setAttribute("userUpdated", userUpdated);
                 user_service.update(userUpdated);
                 response.sendRedirect("/");
                 return; 

            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            

        }
        if (action.equals("add")) {
            User newUser = new User(email, firstName, lastName, password, newRole);
            request.setAttribute("newUser", newUser);
            try {
                user_service.insert(newUser);
                    response.sendRedirect("/");
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            
        }

    }

}
