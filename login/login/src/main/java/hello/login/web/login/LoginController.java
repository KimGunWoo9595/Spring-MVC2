package hello.login.web.login;

import hello.login.domain.member.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.login.form.LoginForm;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form){
        return "login/loginForm";
    }

    // @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult result,HttpServletResponse response){

        if(result.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember==null){  //service에서 password가 불일치시 null로 리턴되게 제작
            //글로벌 오류 (글로벌 오류는 객체(or필드)만으로 볼 수 없는 오류이다)
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리(쿠키넣기)
        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료) value는 String으로 넘겨주어야 한다.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //서버에서 응답을 보낼때 response에 넣어서 보내줘야한다.
        response.addCookie(idCookie);

        return "redirect:/";
    }

    // 세션적용
   // @PostMapping("/login")
    public String loginv3(@Validated @ModelAttribute LoginForm form, BindingResult result,HttpServletRequest request){

        if(result.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember==null){  //service에서 password가 불일치시 null로 리턴되게 제작
            //글로벌 오류 (글로벌 오류는 객체(or필드)만으로 볼 수 없는 오류이다)
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //ㄹ그인 성공처리(로그인에 성공이되면session에 담아줄 것이다)
        //세션이 있으면 있는 세션 반환, 없으면 신규세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원정보 보관 (로그인된 member객체를 세션에 키:값으로 담아준다)
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:/";



        /*request.getSession(true)
        세션이 있으면 기존 세션을 반환한다.
        세션이 없으면 새로운 세션을 생성해서 반환한다.
        request.getSession(false)
        세션이 있으면 기존 세션을 반환한다.
        세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다*/
    }

    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult result,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request){

        if(result.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember==null){  //service에서 password가 불일치시 null로 리턴되게 제작
            //글로벌 오류 (글로벌 오류는 객체(or필드)만으로 볼 수 없는 오류이다)
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //ㄹ그인 성공처리(로그인에 성공이되면session에 담아줄 것이다)
        //세션이 있으면 있는 세션 반환, 없으면 신규세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원정보 보관 (로그인된 member객체를 세션에 키:값으로 담아준다)
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:"+redirectURL;

    }





    @PostMapping("/logout")
    public String logoutv3(HttpServletRequest request){
        //false 우선 있는 세션을 가져오고 세션이 없더라도 만들지말고 null을 반환하라
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }


    // 세션적용
    //@PostMapping("/login")
    public String loginv2(@Validated @ModelAttribute LoginForm form, BindingResult result,HttpServletResponse response){

        if(result.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember==null){  //service에서 password가 불일치시 null로 리턴되게 제작
            //글로벌 오류 (글로벌 오류는 객체(or필드)만으로 볼 수 없는 오류이다)
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }







    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
        }

    //@PostMapping("/logout")
    public String logoutv2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }





}