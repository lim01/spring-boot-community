package kr.co.livetour.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.livetour.demo.service.MemberService;
import kr.co.livetour.demo.util.Ut;
import kr.co.livetour.demo.vo.Member;
import kr.co.livetour.demo.vo.ResultData;

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
		return ResultData.newData(joinRd, member);
	}
	
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	//http://localhost:8080/usr/member/doLogin?loginId=user1&loginPw=user1
	public ResultData<Member> doLogin(HttpSession httpsession, String loginId, String loginPw) {
		boolean isLogined = false;
		
		if ( httpsession.getAttribute("loginedMemberId") != null ) {
			isLogined = true;
		}
		if ( isLogined ) {
			return ResultData.from("F-5", "이미 로그인되었습니다");
		}
		
		if ( Ut.empty(loginId) ) {
			return ResultData.from("F-1", "loginId(을)를 입력해주세요");
		}
		if ( Ut.empty(loginPw) ) {
			return ResultData.from("F-2", "loginPw(을)를 입력해주세요");
		}
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if ( member == null ) {
			return ResultData.from("F-3", "존재하지 않는 로그인아이디 입니다");
		}
		
		if ( member.getLoginPw().equals(loginPw) == false ) {
			return ResultData.from("F-4", "비밀번호가 일치하지 않습니다");
		}
		
		httpsession.setAttribute("loginedMemberId", member.getId());
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다", member.getNickname()));
		
	}

}
