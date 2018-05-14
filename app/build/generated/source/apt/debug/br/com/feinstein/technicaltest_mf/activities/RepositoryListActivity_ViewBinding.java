// Generated code from Butter Knife. Do not modify!
package br.com.feinstein.technicaltest_mf.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import br.com.feinstein.technicaltest_mf.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RepositoryListActivity_ViewBinding implements Unbinder {
  private RepositoryListActivity target;

  @UiThread
  public RepositoryListActivity_ViewBinding(RepositoryListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RepositoryListActivity_ViewBinding(RepositoryListActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.repository_list, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.coordinatorLayout = Utils.findRequiredViewAsType(source, R.id.root, "field 'coordinatorLayout'", CoordinatorLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RepositoryListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.coordinatorLayout = null;
  }
}
