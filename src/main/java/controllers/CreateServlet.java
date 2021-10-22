package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public CreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();
            
            Message m = new Message();
            
            String title = request.getParameter("title");
            m.setTitle(title);
            
            String content = request.getParameter("content");
            m.setContent(content);
            
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            m.setCreated_at(currentTime);
            m.setUpdated_at(currentTime);
            
            em.persist(m);
            em.getTransaction().commit();
            em.close();
            
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
