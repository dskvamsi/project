package banking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import jakarta.servlet.*;

@WebServlet(
		urlPatterns = { "/register","/loginpage" }, 
				initParams= {
						@WebInitParam(name="username",value="root"),
						@WebInitParam(name="password",value="root"),
						@WebInitParam(name="url",value="jdbc:mysql://localhost:3306/banking")
		})

public class BankServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletConfig config=getServletConfig();
		String url=config.getInitParameter("url");
		String username=config.getInitParameter("username");
		String password=config.getInitParameter("password");
		String path=request.getServletPath();
		if(path.equals("/register")) {
			String bankname=request.getParameter("bankname");
			int accountno=Integer.parseInt(request.getParameter("accountno"));
			String accountname=request.getParameter("accountname");
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection(url, username, password);
				PreparedStatement st=con.prepareStatement("select * from bank where accountno=?");
				st.setInt(1,accountno);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
//					response.getWriter().println("user is already existed");
					request.setAttribute("error", "User is already Existed");
					RequestDispatcher rd=request.getRequestDispatcher("register.jsp");
					rd.forward(request, response);
				}
				else {
					PreparedStatement st1=con.prepareStatement("insert into bank values(?,?,?)");
					st1.setString(1, bankname);
					st1.setInt(2, accountno);
					st1.setString(3, accountname);
					int rows=st1.executeUpdate();
					if(rows==1) {
//						response.sendRedirect("login.html");
						RequestDispatcher rd=request.getRequestDispatcher("login.html");
						rd.forward(request, response);
					}
					else {
//						response.sendRedirect("register.html");
						RequestDispatcher rd=request.getRequestDispatcher("register.jsp");
						rd.forward(request, response);
					}
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			
		}
		else if(path.equals("/loginpage")) {
			int accountno=Integer.parseInt(request.getParameter("accountno"));
			String accountname=request.getParameter("accountname");
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection(url, username, password);
				PreparedStatement st=con.prepareStatement("select * from bank where accountno=? and accountname=?");
				st.setInt(1, accountno);
				st.setString(2, accountname);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
//					response.sendRedirect("home.html");
					RequestDispatcher rd=request.getRequestDispatcher("home.html");
					// Request Dispatcher is used to transfer the data or retrieve the
					// data from one end to another end
					rd.forward(request, response);
				}
				else {
//					response.sendRedirect("login.html");
					RequestDispatcher rd=request.getRequestDispatcher("login.html");
					rd.forward(request, response);
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
