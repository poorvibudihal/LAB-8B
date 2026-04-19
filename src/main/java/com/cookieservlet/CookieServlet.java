package com.cookieservlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("userName");

        String existingUser = null;
        int visitCount = 0;

        // Step 1: Read cookies
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    existingUser = c.getValue();
                }
                if (c.getName().equals("count")) {
                    visitCount = Integer.parseInt(c.getValue());
                }
            }
        }

        // Step 2: First-time login
        if (userName != null && !userName.isEmpty()) {
            existingUser = userName;
            visitCount = 0;

            Cookie userCookie = new Cookie("user", userName);
            userCookie.setMaxAge(30); // expires in 30 seconds

            Cookie countCookie = new Cookie("count", "0");
            countCookie.setMaxAge(30);

            response.addCookie(userCookie);
            response.addCookie(countCookie);
        }

        out.println("<html><body>");

        // Step 3: Returning user
        if (existingUser != null) {

            visitCount++;

            Cookie countCookie = new Cookie("count", String.valueOf(visitCount));
            countCookie.setMaxAge(30);
            response.addCookie(countCookie);

            out.println("<h2 style='color:blue'>Welcome back, " + existingUser + "!</h2>");
            out.println("<h3>You have visited this page " + visitCount + " times</h3>");

            out.println("<p style='color:red;'>Cookie expires in 30 seconds</p>");

            // Step 4: Display all cookies
            out.println("<h3>List of Cookies:</h3>");
            if (cookies != null) {
                out.println("<table border='1'>");
                out.println("<tr><th>Cookie Name</th><th>Value</th></tr>");

                for (Cookie c : cookies) {
                    out.println("<tr>");
                    out.println("<td>" + c.getName() + "</td>");
                    out.println("<td>" + c.getValue() + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            // Step 5: Logout button (manual expiry)
            out.println("<br><form action='CookieServlet' method='post'>");
            out.println("<input type='submit' value='Delete Cookies'>");
            out.println("</form>");
        }

        // Step 6: New user
        else {
            out.println("<h2 style='color:red'>New User - Enter Name</h2>");
            out.println("<form action='CookieServlet' method='get'>");
            out.println("Name: <input type='text' name='userName' required>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
        }

        out.println("</body></html>");
    }

    // Step 7: Delete cookies (expiry demo)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cookie userCookie = new Cookie("user", "");
        userCookie.setMaxAge(0);

        Cookie countCookie = new Cookie("count", "");
        countCookie.setMaxAge(0);

        response.addCookie(userCookie);
        response.addCookie(countCookie);

        response.sendRedirect("CookieServlet");
    }
}