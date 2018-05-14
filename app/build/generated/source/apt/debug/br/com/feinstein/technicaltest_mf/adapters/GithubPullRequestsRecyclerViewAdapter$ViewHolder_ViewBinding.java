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

public class GithubPullRequestsRecyclerViewAdapter$ViewHolder_ViewBinding implements Unbinder {
  private GithubPullRequestsRecyclerViewAdapter.ViewHolder target;

  @UiThread
  public GithubPullRequestsRecyclerViewAdapter$ViewHolder_ViewBinding(GithubPullRequestsRecyclerViewAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.avatarImage = Utils.findRequiredViewAsType(source, R.id.avatar, "field 'avatarImage'", CircleImageView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.body = Utils.findRequiredViewAsType(source, R.id.body, "field 'body'", TextView.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.username, "field 'username'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.date, "field 'date'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GithubPullRequestsRecyclerViewAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.avatarImage = null;
    target.title = null;
    target.body = null;
    target.username = null;
    target.date = null;
  }
}
