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


@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public UpdateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            
            //セッションスコープからメッセージのIDを取得して
            //該当のIDのメッセージ1件のみのDBから取得
            Message m = em.find(Message.class, (Integer)(request.getSession().getAttribute("message_id")));
            
            //フォームの内容を各フィールドに上書き
            String title = request.getParameter("title");
            m.setTitle(title);
            
            String content = request.getParameter("content");
            m.setContent(content);
            
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            m.setUpdated_at(currentTime);  //更新日時のみ上書き
            
            
            //バリデーションを実行してエラーがあったら編集画面のフォームに戻る
            List<String> errors = MessageValidator.validate(m);
            if(errors.size() > 0) {
                em.close();
                
                //フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("message", m);
                request.setAttribute("errors", errors);
                
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/messages/edit.jsp");
                rd.forward(request, response);
            }else {
            //DBを更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            request.getSession().setAttribute("flush", "更新が完了しました。");            
            em.close();
            
            //セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("message_id");
            
            //indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
            }
        }
    }

}
