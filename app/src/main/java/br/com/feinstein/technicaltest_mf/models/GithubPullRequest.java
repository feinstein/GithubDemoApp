package br.com.feinstein.technicaltest_mf.models;

import org.threeten.bp.ZonedDateTime;

/**
 * DTO que representa um Pull Request de um Repositorio do Github.
 */
public class GithubPullRequest {
    private String title;
    private String created_at;
    private String body;
    private User user;
    private String html_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getCreatedAtAsZonedDateTime() {
        return ZonedDateTime.parse(created_at);
    }

    public String getUrl() {
        return html_url;
    }

    public void setUrl(String url) {
        this.html_url = url;
    }
}
