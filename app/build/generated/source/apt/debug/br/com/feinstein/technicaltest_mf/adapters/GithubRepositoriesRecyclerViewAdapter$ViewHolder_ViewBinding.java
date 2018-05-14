// Generated code from Butter Knife. Do not modify!
package br.com.feinstein.technicaltest_mf.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import br.com.feinstein.technicaltest_mf.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GithubRepositoriesRecyclerViewAdapter$ViewHolder_ViewBinding implements Unbinder {
  private GithubRepositoriesRecyclerViewAdapter.ViewHolder target;

  @UiThread
  public GithubRepositoriesRecyclerViewAdapter$ViewHolder_ViewBinding(GithubRepositoriesRecyclerViewAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.avatarImage = Utils.findRequiredViewAsType(source, R.id.avatar, "field 'avatarImage'", CircleImageView.class);
    target.repoTitle = Utils.findRequiredViewAsType(source, R.id.repo_title, "field 'repoTitle'", TextView.class);
    target.repoDescription = Utils.findRequiredViewAsType(source, R.id.repo_description, "field 'repoDescription'", TextView.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.username, "field 'username'", TextView.class);
    target.starsCount = Utils.findRequiredViewAsType(source, R.id.stars_count, "field 'starsCount'", TextView.class);
    target.forksCount = Utils.findRequiredViewAsType(source, R.id.forks_count, "field 'forksCount'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GithubRepositoriesRecyclerViewAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.avatarImage = null;
    target.repoTitle = null;
    target.repoDescription = null;
    target.username = null;
    target.starsCount = null;
    target.forksCount = null;
  }
}
