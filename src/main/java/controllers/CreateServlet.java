package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import models.validators.MessageValidator;
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
            
            //バリデーションを実行してエラーがあったら新規登録のフォームに戻る
            List<String> errors = MessageValidator.validate(m);
            if(errors.size() > 0) {
                em.close();
                
                //フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("message", m);
                request.setAttribute("errors", errors);
                
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/messages/new.jsp");
                rd.forward(request, response);
            }else {
              //DBに保存
                em.persist(m);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "登録が完了しました。");
                em.close();
                
                //indexのページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/index");
            }
            
                        
            
        }
    }

}
