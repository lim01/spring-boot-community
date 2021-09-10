package kr.co.livetour.demo.vo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;

public class Rq {
	@Getter
	private boolean isLogined;
	@Getter
	private int loginedMemberId;
	
	public Rq(HttpServletRequest req) {
		HttpSession httpsession = req.getSession();
		
		boolean isLogined = false;
		int loginedMemberId = 0;
		
		if ( httpsession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpsession.getAttribute("loginedMemberId");
		}
		
		this.isLogined = isLogined;
		this.loginedMemberId = loginedMemberId;
	}

}
