package kr.co.livetour.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import kr.co.livetour.demo.vo.Rq;

@Controller
public class usrArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	// http://localhost:8080/usr/article/doAdd?title=제목11&body=내용11
	public ResultData<Object> doAdd(HttpServletRequest req, String title, String body) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		if ( Ut.empty(title) ) {
			return ResultData.from("F-1", "title(을)를 입력해주세요");
		}
		if ( Ut.empty(body) ) {
			return ResultData.from("F-1", "body(을)를 입력해주세요");
		}
		
		ResultData<Integer> writeArticleRd = articleService.writeArticle(rq.getLoginedMemberId(), title, body);
		int id = writeArticleRd.getData1();
		
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		//return ResultData.from(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), article);
		return ResultData.newData(writeArticleRd, "article", article);
		
	}
	
	@RequestMapping("/usr/article/list")
	public String showList(HttpServletRequest req, Model model) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		List<Article> articles = articleService.getForPrintArticles(rq.getLoginedMemberId());
		
		model.addAttribute("articles", articles);
		
		return "usr/article/list";
	}
	
	@RequestMapping("/usr/article/detail")
	public String showDetail(HttpServletRequest req, Model model, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		model.addAttribute("article", article);
		
		return "usr/article/detail";
	}
	
	@RequestMapping("/usr/article/getArticle")
	@ResponseBody
	public ResultData<Article> getArticle(HttpServletRequest req, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
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
	public String doDelete(HttpServletRequest req, int id) {
		Rq rq = (Rq)req.getAttribute("rq");

		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if ( article == null ) {
			return Ut.jsHistoryBack(Ut.f("%d번 게시물이 존재하지 않습니다", id));
		}
		
		if ( article.getMemberId() != rq.getLoginedMemberId() ) {
			return Ut.jsHistoryBack("삭제 권한이 없습니다");
		}
		
		articleService.deleteArticle(id);
		
		return Ut.jsReplace(Ut.f("%d번 게시물을 삭제하였습니다", id), "../article/list");
	}
	
	@RequestMapping("/usr/article/modify")
	public String showModify(HttpServletRequest req, Model model, int id) {
		Rq rq = (Rq)req.getAttribute("rq");
		
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if ( article == null ) {
			return rq.historyBackJsOnView(Ut.f("%d번 게시물이 존재하지 않습니다", "id", id));
		}
		
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMemberId(), article);
		
		if ( actorCanModifyRd.isFail() ) {
			return rq.historyBackJsOnView(actorCanModifyRd.getMsg());
		}
		
		model.addAttribute("article", article);
		
		return "usr/article/modify";
		
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String title, String body) {
		Rq rq = (Rq)req.getAttribute("rq");

		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);
		
		if ( article == null ) {
			return Ut.jsHistoryBack(Ut.f("%d번 게시물이 존재하지 않습니다", "id", id));
		}
		
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMemberId(), article);
		
		if ( actorCanModifyRd.isFail() ) {
			return Ut.jsHistoryBack(actorCanModifyRd.getMsg());
		}
		
		articleService.modifyArticle(id, title, body);
		
		return Ut.jsReplace(Ut.f("%d번 글이 수정되었습니다", id), Ut.f("../article/detail?id=%d", id));
	}
	
}
