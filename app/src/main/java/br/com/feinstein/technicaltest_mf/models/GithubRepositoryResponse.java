package br.com.feinstein.technicaltest_mf.models;

import java.util.List;

public class GithubRepositoryResponse {
    private long total_count;
    private boolean incomplete_results;
    private List<GithubRepository> items;

    public long getTotalCount() {
        return total_count;
    }

    public void setTotalCount(long totalCount) {
        this.total_count = totalCount;
    }

    public boolean isIncompleteResults() {
        return incomplete_results;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incomplete_results = incompleteResults;
    }

    public List<GithubRepository> getItems() {
        return items;
    }

    public void setItems(List<GithubRepository> items) {
        this.items = items;
    }
}
