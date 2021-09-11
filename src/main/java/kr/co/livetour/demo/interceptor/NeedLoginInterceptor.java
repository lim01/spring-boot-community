package kr.co.livetour.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.livetour.demo.vo.Rq;

@Component
public class NeedLoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		//System.out.println("로그인 필요!!!");
		Rq rq = (Rq) req.getAttribute("rq");
		
		if ( !rq.isLogined() ) {
			rq.printHistoryBackJs("로그인 후 이용해주세요");
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
	
}
