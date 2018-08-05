package br.com.feinstein.githubapp.models;

/**
 * DTO representando um Repositorio do Github
 */
public class GithubRepository {
    private long id;
    private String name;
    private String description;
    private int forks_count;
    private int stargazers_count;
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getForksCount() {
        return forks_count;
    }

    public void setForksCount(int forksCount) {
        this.forks_count = forksCount;
    }

    public int getStarsCount() {
        return stargazers_count;
    }

    public void setStarsCount(int starsCount) {
        this.stargazers_count = starsCount;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
