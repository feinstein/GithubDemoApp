package br.com.feinstein.githubapp.models;

/**
 * DTO representando o dono de um Repositorio do Github.
 */

public class User {
    private long id;
    private String login;
    private String avatar_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return login;
    }

    public void setUsername(String username) {
        this.login = username;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatar_url = avatarUrl;
    }
}
