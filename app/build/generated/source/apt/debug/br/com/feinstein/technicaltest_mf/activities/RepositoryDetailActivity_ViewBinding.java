// Generated code from Butter Knife. Do not modify!
package br.com.feinstein.technicaltest_mf.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import br.com.feinstein.technicaltest_mf.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RepositoryDetailActivity_ViewBinding implements Unbinder {
  private RepositoryDetailActivity target;

  @UiThread
  public RepositoryDetailActivity_ViewBinding(RepositoryDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RepositoryDetailActivity_ViewBinding(RepositoryDetailActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RepositoryDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
  }
}