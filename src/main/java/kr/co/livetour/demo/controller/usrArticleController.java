package kr.co.livetour.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	//http://localhost:8080/usr/article/doAdd?title=제목11&body=내용11
	public ResultData<Object> doAdd(String title, String body) {
		if ( Ut.empty(title) ) {
			return ResultData.from("F-1", "title(을)를 입력해 주세요");
		}
		if ( Ut.empty(body) ) {
			return ResultData.from("F-1", "body(을)를 입력해 주세요");
		}
		
		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body);
		int id = writeArticleRd.getData1();
		
		
		Article article = articleService.getArticle(id);
		//return ResultData.from(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), article);
		return ResultData.newData(writeArticleRd, article);
		
	}
	
	@RequestMapping("/usr/article/getArticles")
	@ResponseBody
	public ResultData<List<Article>> getArticles() {
		List<Article> articles = articleService.getArticles();
		return ResultData.from("S-1", "게시물 리스트 입니다", articles);
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
		return ResultData.from("S-1", Ut.f("%d번 게시물 입니다", id), article);
	}
	
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData<Integer> doDelete(int id) {
		Article article = articleService.getArticle(id);
		
		if ( article == null ) {
			//return id + "번 게시물이 존재하지 않습니다";
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다", id));
		}
		
		articleService.deleteArticle(id);
		
		//return id + "번 게시물을 삭제하였습니다";
		return ResultData.from("F-1", Ut.f("%d번 게시물을 삭제하였습니다", id), id);
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData<Integer> doModify(int id, String title, String body) {
		Article article = articleService.getArticle(id);
		
		if ( article == null ) {
			//return id + "번 게시물이 존재하지 않습니다";
			return ResultData.from("F-1", Ut.f("%d번 게시물이 존재하지 않습니다", id));
		}
		
		articleService.modifyArticle(id, title, body);
		
		//return id + "번 게시물을 수정하였습니다";
		return ResultData.from("F-1", Ut.f("%d번 게시물을 수정하였습니다", id), id);
	}



	
}
