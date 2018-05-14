package br.com.feinstein.technicaltest_mf.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.threeten.bp.Clock;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import br.com.feinstein.technicaltest_mf.R;
import br.com.feinstein.technicaltest_mf.models.GithubPullRequest;
import br.com.feinstein.technicaltest_mf.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GithubPullRequestsRecyclerViewAdapter
        extends RecyclerView.Adapter<GithubPullRequestsRecyclerViewAdapter.ViewHolder>  {

    private final Context context;
    private final List<GithubPullRequest> pullRequests;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pullRequestURL = (String) view.getTag();

            Utils.openWebPage(pullRequestURL, context);
        }
    };

    private Clock clock = Clock.systemDefaultZone(); // TODO: Inject with Dagger 2

    public GithubPullRequestsRecyclerViewAdapter(List<GithubPullRequest> pullRequests, Context context) {
        this.pullRequests = pullRequests;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pull_request_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GithubPullRequest pullRequest = pullRequests.get(position);

        holder.title.setText(pullRequest.getTitle());
        holder.body.setText(pullRequest.getBody());
        holder.username.setText(pullRequest.getUser().getUsername());
        holder.date.setText(pullRequest.getCreatedAtAsZonedDateTime()
                                       .withZoneSameInstant(clock.getZone())
                                       .format(DateTimeFormatter
                                       .ofPattern("dd/MM/yyyy")));
        Picasso.get()
                .load(pullRequest.getUser().getAvatarUrl())
                .noFade()
                .error(R.drawable.ic_error_black_24dp)
                .into(holder.avatarImage);

        holder.itemView.setTag(pullRequest.getUrl());
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return pullRequests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) CircleImageView avatarImage;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.body) TextView body;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.date) TextView date;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
