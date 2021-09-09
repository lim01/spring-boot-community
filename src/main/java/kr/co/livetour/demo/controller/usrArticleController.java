package kr.co.livetour.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.livetour.demo.service.ArticleService;
import kr.co.livetour.demo.util.Ut;
import kr.co.livetour.demo.vo.Article;
import kr.co.livetour.demo.vo.ResultData;

@Controller
public class usrArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	// http://localhost:8080/usr/article/doAdd?title=제목11&body=내용11
	public ResultData<Object> doAdd(HttpSession httpsession, String title, String body) {
		boolean isLogined = false;
		int loginedMemberId = 0;
		
		if ( httpsession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpsession.getAttribute("loginedMemberId");
		}
		if ( isLogined == false ) {
			return ResultData.from("F-A", "로그인 후 이용해주세요");
		}
		
		
		if ( Ut.empty(title) ) {
			return ResultData.from("F-1", "title(을)를 입력해주세요");
		}
		if ( Ut.empty(body) ) {
			return ResultData.from("F-1", "body(을)를 입력해주세요");
		}
		
		ResultData<Integer> writeArticleRd = articleService.writeArticle(loginedMemberId, title, body);
		int id = writeArticleRd.getData1();
		
		
		Article article = articleService.getArticle(id);
		//return ResultData.from(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), article);
		return ResultData.newData(writeArticleRd, "article", article);
		
	}
	
	@RequestMapping("/usr/article/list")
	public String showList(Model model) {
		List<Article> articles = articleService.getArticles();
		
		model.addAttribute("articles", articles);
		
		return "usr/article/list";
	}
	
	@RequestMapping("/usr/article/detail")
	public String showDetail(Model model, int id) {
		Article article = articleService.getArticle(id);
		
		model.addAttribute("article", article);
		
		return "usr/article/detail";
	}
	
	@RequestMapping("/usr/article/getArticle")
	@ResponseBody
	public ResultData<Article> getArticle(int id) {
		Article article = articleService.getArticle(id);
		
		if ( article == null ) {
			//return id + "번 게시물이 존재하지 않습니다";
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다", id));
		}
		
		//return article;
		return ResultData.from("S-1", Ut.f("%d번 게시물 입니다", id), "article", article);
	}
	
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	// http://localhost:8080/usr/article/doDelete?id=1
	public ResultData<Integer> doDelete(HttpSession httpsession, int id) {
		boolean isLogined = false;
		int loginedMemberId = 0;
		
		if ( httpsession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpsession.getAttribute("loginedMemberId");
		}
		if ( isLogined == false ) {
			return ResultData.from("F-A", "로그인 후 이용해주세요");
		}
		
		Article article = articleService.getArticle(id);
		
		if ( article.getId() != loginedMemberId ) {
			return ResultData.from("F-2", "권한이 없습니다");
		}
		
		if ( article == null ) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다", id));
		}
		
		articleService.deleteArticle(id);
		
		return ResultData.from("F-1", Ut.f("%d번 게시물을 삭제하였습니다", id), "id", id);
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData<Article> doModify(HttpSession httpsession, int id, String title, String body) {
		boolean isLogined = false;
		int loginedMemberId = 0;
		
		if ( httpsession.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) httpsession.getAttribute("loginedMemberId");
		}
		if ( isLogined == false ) {
			return ResultData.from("F-A", "로그인 후 이용해주세요");
		}
		
		Article article = articleService.getArticle(id);
		
		if ( article == null ) {
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다", "id", id));
		}
		
		ResultData actorCanModifyRd = articleService.actorCanModify(loginedMemberId, article);
		
		if ( actorCanModifyRd.isFail() ) {
			return actorCanModifyRd;
		}
		
		return articleService.modifyArticle(id, title, body);
	}



	
}
