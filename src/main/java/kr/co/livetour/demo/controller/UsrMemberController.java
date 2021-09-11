package kr.co.livetour.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.livetour.demo.service.MemberService;
import kr.co.livetour.demo.util.Ut;
import kr.co.livetour.demo.vo.Member;
import kr.co.livetour.demo.vo.ResultData;
import kr.co.livetour.demo.vo.Rq;

@Controller
public class UsrMemberController {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	//http://localhost:8080/usr/member/doJoin?loginId=admin&loginPw=admin&authLevel=7&name=관리자&nickname=관리자&cellphoneNo=01011111111&email=lim01@naver.com
	public ResultData<Member> doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		
		if ( Ut.empty(loginId) ) {
			return ResultData.from("F-1", "loginId(을)를 입력해주세요");
		}
		if ( Ut.empty(loginPw) ) {
			return ResultData.from("F-2", "loginPw(을)를 입력해주세요");
		}
		if ( Ut.empty(name) ) {
			return ResultData.from("F-3", "name(을)를 입력해주세요");
		}
		if ( Ut.empty(nickname) ) {
			return ResultData.from("F-4", "nickname(을)를 입력해주세요");
		}
		if ( Ut.empty(cellphoneNo) ) {
			return ResultData.from("F-5", "cellphoneNo(을)를 입력해주세요");
		}
		if ( Ut.empty(email) ) {
			return ResultData.from("F-6", "email(을)를 입력해주세요");
		}
		
		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		
		if ( joinRd.isFail() ) {
			return (ResultData)joinRd;
		}
		
		Member member = memberService.getMemberById((int)joinRd.getData1());
		
		//return ResultData.from("S-1", "회원 가입이 완료되었습니다", member);
		return ResultData.newData(joinRd, "member" ,member);
	}
	
	@RequestMapping("/usr/member/login")
	public String showLogin(HttpSession httpsession) {
		return "usr/member/login";
	}
	
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	//http://localhost:8080/usr/member/doLogin?loginId=user1&loginPw=user1
	public String doLogin(HttpServletRequest req, String loginId, String loginPw) {
		Rq rq = (Rq) req.getAttribute("rq");
		
		if ( rq.isLogined() ) {
			return Ut.jsHistoryBack("이미 로그인되었습니다");
		}
		
		if ( Ut.empty(loginId) ) {
			return Ut.jsHistoryBack("loginId(을)를 입력해주세요");
		}
		if ( Ut.empty(loginPw) ) {
			return Ut.jsHistoryBack("loginPw(을)를 입력해주세요");
		}
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if ( member == null ) {
			return Ut.jsHistoryBack("존재하지 않는 로그인아이디 입니다");
		}
		
		if ( member.getLoginPw().equals(loginPw) == false ) {
			return Ut.jsHistoryBack("비밀번호가 일치하지 않습니다");
		}
		
		rq.login(member);
		
		return Ut.jsReplace(Ut.f("%s님 환영합니다", member.getNickname()), "/");
		
	}
	
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	// http://localhost:8080/usr/member/doLogout
	public String doLogout(HttpServletRequest req) {
		Rq rq = (Rq) req.getAttribute("rq");
		
		if ( !rq.isLogined() ) {
			return Ut.jsHistoryBack("이미 로그아웃 상태입니다");
		}

		rq.logout();
		
		return Ut.jsReplace("로그아웃 되었습니다", "/");
	}

}
