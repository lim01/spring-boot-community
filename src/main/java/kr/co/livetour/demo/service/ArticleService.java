package kr.co.livetour.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.livetour.demo.repository.ArticleRepository;
import kr.co.livetour.demo.util.Ut;
import kr.co.livetour.demo.vo.Article;
import kr.co.livetour.demo.vo.ResultData;

@Service
public class ArticleService {

	private ArticleRepository articleRepository;
	
	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}
	

	public ResultData writeArticle(int memberId, String title, String body) {
		articleRepository.writeArticle(memberId, title, body);
		//return articleRepository.getLastInsertId();
		int id = articleRepository.getLastInsertId();
		
		return ResultData.from("S-1", Ut.f("%d번 게시물이 생성되었습니다", id), id);
	}

	public List<Article> getArticles() {
		return articleRepository.getArticles();
	}

	public Article getArticle(int id) {
		return articleRepository.getArticle(id);
	}

	public void deleteArticle(int id) {
		articleRepository.deleteArticle(id);
	}

	public void modifyArticle(int id, String title, String body) {
		articleRepository.modifyArticle(id, title, body);
		
	}


}
