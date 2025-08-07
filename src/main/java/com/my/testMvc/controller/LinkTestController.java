package com.my.testMvc.controller;

import com.my.testMvc.dto.Article;
import com.my.testMvc.dto.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/articles")
public class LinkTestController {

    // 샘플 데이터 생성 메서드
    private List<Article> generateArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1L, "/articles/list", "GET", "전체 게시글 읽기 성공", "list_all.html"));
        articles.add(new Article(2L, "/articles/{id}", "GET", "7번 게시글 읽어오기 성공", "list_one.html"));
        articles.add(new Article(3L, "/articles/create?name=tom&weight=80&height=180", "GET", "톰의 키는 180, 몸무게는 80 입니다", "new.html"));
        articles.add(new Article(4L, "/articles/update", "POST", "업데이트 성공 화면 출력", "update_ok.html"));
        articles.add(new Article(5L, "/articles/{id}/update", "GET", "7번 게시글 수정 폼 로딩 완료", "update.html"));
        articles.add(new Article(6L, "/articles/{id}/delete", "GET", "3번 글 삭제 완료", "delete_ok.html"));
        articles.add(new Article(7L, "/articles/{id}/articleComment", "POST", "11번 게시글의 모든 댓글보기 성공", "comment_view.html"));
        articles.add(new Article(8L, "/articles/{id}/articleComments/{articlecomment-id}/delete", "POST", "15번 게시글의 3번째 답글 삭제 완료", "delete_ok.html"));
        // 추가: 게시글 ID 11번, 15번
        articles.add(new Article(11L, "/articles/11", "GET", "11번 게시글입니다.", "list_one.html"));
        articles.add(new Article(15L, "/articles/15", "GET", "15번 게시글입니다.", "list_one.html"));

        return articles;
    }

    private List<Comment> generateComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, 11L, "11번 글의 첫 번째 댓글입니다."));
        comments.add(new Comment(2L, 11L, "11번 글의 두 번째 댓글입니다."));
        comments.add(new Comment(3L, 15L, "15번 글의 첫 번째 댓글입니다."));
        comments.add(new Comment(4L, 15L, "15번 글의 두 번째 댓글입니다."));
        comments.add(new Comment(5L, 15L, "15번 글의 세 번째 댓글입니다.")); // 이게 삭제될 댓글

        return comments;
    }

    @GetMapping("/main")
    public String main(Model model) {
        List<Article> articles = generateArticles();
        List<Comment> comments = generateComments();

        Map<Long, List<Comment>> commentsByArticleId =
                comments.stream().collect(Collectors.groupingBy(Comment::getArticleId));

        model.addAttribute("articles", articles);
        model.addAttribute("commentsByArticleId", commentsByArticleId);

        return "/articles/main";
    }

    @GetMapping("/list")
    public String listAll(Model model) {
        model.addAttribute("articles", generateArticles());
        return "/articles/list_all";
    }

    @GetMapping("/{id}")
    public String listOne(@PathVariable("id")int id, Model model) {
        Article targetArticle = (Article)
                generateArticles().stream()
                        .filter(article -> article.getId() == id)
                        .findFirst()
                        .orElse(null);
        if (targetArticle != null) {
            model.addAttribute("article", targetArticle);
            return "/articles/list_one";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String create(
            @RequestParam String name,
            @RequestParam int height,
            @RequestParam int weight,
            Model model
    ){
        String content = String.format(
                "%s의 키는 %d, 몸무게는 %d입니다.", name, height, weight
        );
        model.addAttribute("name", name);
        model.addAttribute("height", height);
        model.addAttribute("weight", weight);
        model.addAttribute("content", content);

        return "/articles/new";
    }

    @GetMapping("/update")
    public String updateOk(Model model) {
        model.addAttribute("articles", generateArticles());
        return "/articles/update_ok";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int id, Model model) {
        Article article = generateArticles().stream()
                .filter(a -> a.getId() == id)
                .findFirst().
                orElse(null);
        if (article != null) {
            model.addAttribute("article", article);
            return "/articles/update";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteOk(@PathVariable("id") int id, Model model) {
        model.addAttribute("message", id + "번 글 삭제 완료");
        return "/articles/delete_ok";
    }

    @GetMapping("/{id}/articleComment")
    public String commentView(@PathVariable("id") int id, Model model) {
        List<Comment> comments = generateComments().stream()
                .filter(c -> c.getArticleId().equals((long)id))
                .collect(Collectors.toList());
        model.addAttribute("articleId", id);
        model.addAttribute("comments", comments);
        model.addAttribute("message", id + "번 게시글의 모든 댓글보기 성공");
        return "/articles/comment_view";
    }

    @GetMapping("/{id}/articleComments/{commentIndex}/delete")
    public String deleteComment(
            @PathVariable("id") int id,
            @PathVariable("commentIndex") int commentIndex,
            Model model
    ) {
        List<Comment> comments = generateComments().stream()
                .filter(c -> c.getArticleId().equals((long) id))
                .collect(Collectors.toList());

        int index = commentIndex - 1;

        if (index >= 0 && index < comments.size()) {
            Comment targetComment = comments.get(index);

            model.addAttribute("articleId", id);
            model.addAttribute("comment", targetComment);
            model.addAttribute("message", id + "번 게시글의 " + commentIndex + "번째 답글 삭제 완료");
            return "/articles/delete_ok";
        } else {
            return "error/404";
        }
    }
}
