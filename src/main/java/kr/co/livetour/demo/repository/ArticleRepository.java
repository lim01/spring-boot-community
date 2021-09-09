package kr.co.livetour.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.livetour.demo.vo.Article;

@Mapper
public interface ArticleRepository {
	//@Insert("insert into article (regDate, updateDate, title, body) values ( now(), now(), #{title}, #{body})")
	public void writeArticle(@Param("memberId") int memberId, @Param("title") String title, @Param("body") String body);
	
	//@Select("select * from article where id=#{id}")
	public Article getArticle(@Param("id") int id);
	
	//@Delete("delete from article where id=#{id}")
	public void deleteArticle(@Param("id") int id);
	
	//@Update("update article set title=#{title}, title=#{title}, updateDate=NOW() where id=#{id}")
	public void modifyArticle(@Param("id") int id, @Param("title") String title, @Param("body") String body);
	
	//@Select("select * from article order by id desc")
	public List<Article> getArticles();

	//@Select("select last_insert_id()")
	public int getLastInsertId();
}
