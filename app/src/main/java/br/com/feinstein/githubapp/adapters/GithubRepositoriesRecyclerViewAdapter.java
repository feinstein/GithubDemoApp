package br.com.feinstein.githubapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.feinstein.githubapp.activities.RepositoryDetailActivity;
import br.com.feinstein.githubapp.activities.RepositoryListActivity;
import br.com.feinstein.githubapp.R;
import br.com.feinstein.githubapp.fragments.RepositoryDetailFragment;
import br.com.feinstein.githubapp.models.GithubRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter que permite endless scroll dos Repositorios do Github.
 */
public class GithubRepositoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<GithubRepositoriesRecyclerViewAdapter.ViewHolder> {

    private final AppCompatActivity parentActivity;
    private final List<GithubRepository> repositories;
    private final boolean isTwoPane;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GithubRepository repository = (GithubRepository) view.getTag();

            if (isTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RepositoryDetailFragment.ARG_REPOSITORY_NAME, repository.getName());
                arguments.putString(RepositoryDetailFragment.ARG_REPOSITORY_OWNER_NAME, repository.getOwner().getUsername());
                RepositoryDetailFragment fragment = new RepositoryDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.repository_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RepositoryDetailActivity.class);
                intent.putExtra(RepositoryDetailFragment.ARG_REPOSITORY_NAME, repository.getName());
                intent.putExtra(RepositoryDetailFragment.ARG_REPOSITORY_OWNER_NAME, repository.getOwner().getUsername());

                context.startActivity(intent);
            }
        }
    };

    public GithubRepositoriesRecyclerViewAdapter(RepositoryListActivity parent,
                                                 List<GithubRepository> repositories,
                                                 boolean twoPane) {
        this.repositories = repositories;
        parentActivity = parent;
        isTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repository_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GithubRepository repository = repositories.get(position);

        holder.repoTitle.setText(repository.getName());
        holder.repoDescription.setText(repository.getDescription());
        holder.username.setText(repository.getOwner().getUsername());
        holder.starsCount.setText(String.valueOf(repository.getStarsCount()));
        holder.forksCount.setText(String.valueOf(repository.getForksCount()));
        Picasso.get()
                .load(repository.getOwner().getAvatarUrl())
                .noFade()
                .error(R.drawable.ic_error_black_24dp)
                .into(holder.avatarImage);

        holder.itemView.setTag(repository);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) CircleImageView avatarImage;
        @BindView(R.id.repo_title) TextView repoTitle;
        @BindView(R.id.repo_description) TextView repoDescription;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.stars_count) TextView starsCount;
        @BindView(R.id.forks_count) TextView forksCount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
