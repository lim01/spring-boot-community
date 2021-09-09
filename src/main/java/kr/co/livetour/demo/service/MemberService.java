package kr.co.livetour.demo.service;

import org.springframework.stereotype.Service;

import kr.co.livetour.demo.repository.MemberRepository;
import kr.co.livetour.demo.util.Ut;
import kr.co.livetour.demo.vo.Member;
import kr.co.livetour.demo.vo.ResultData;

@Service
public class MemberService {

	private MemberRepository memberRepository;
	
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		// 아이디 중복 체크
		Member oldMember = memberRepository.getMemberByLoginId(loginId);
		if (oldMember != null) {
			//return -1;
			return ResultData.from("F-7", Ut.f("해당 로그인 아이디(%s)는 이미 사용중입니다", loginId));
		}
		// 이름+이메일 중복 체크
		oldMember = memberRepository.getMemberByNameAndEmail(name, email);
		if (oldMember != null) {
			//return -2;
			return ResultData.from("F-8", Ut.f("해당 이름(%s)과 이메일(%s)이 이미 사용중입니다", name, email));
		}
		
		memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		int id = memberRepository.getLastInsertId();
		return ResultData.from("S-1", "회원 가입이 완료되었습니다", "id", id);
	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

}
