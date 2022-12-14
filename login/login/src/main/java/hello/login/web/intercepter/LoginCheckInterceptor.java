package hello.login.web.intercepter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(); // 세션의 유무로 로그인 인증을 진행할 것 이기 때문에
        if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false; // 중요! false여야 더이상 진행하지 않는다.
        }

        return true;
    }
}
