package com.google.android.youtube.player;

import com.google.android.youtube.player.internal.d;
import com.google.android.youtube.player.internal.w;

import android.view.KeyEvent;
import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.content.res.Configuration;
import com.google.android.youtube.player.internal.t;
import com.google.android.youtube.player.internal.aa;
import com.google.android.youtube.player.internal.y;
import android.os.Build;
import java.util.HashSet;
import com.google.android.youtube.player.internal.ab;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Bundle;
import com.google.android.youtube.player.internal.n;
import com.google.android.youtube.player.internal.s;
import android.view.View;
import java.util.Set;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

public final class YouTubePlayerViewX extends ViewGroup implements YouTubePlayer.Provider
{
    private a a;
    private Set<View> b;
    private b c;
    private com.google.android.youtube.player.internal.b d;
    private s e;
    private View f;
    private n g;
    private YouTubePlayer.Provider h;
    private Bundle i;
    private YouTubePlayer.OnInitializedListener j;
    private boolean k;
    private boolean l;

    public YouTubePlayerViewX(final Context context) {
        this(context, null);
    }

    public YouTubePlayerViewX(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public YouTubePlayerViewX(Context context, final AttributeSet set, final int n) {
        this(context, set, n, null);
    }

    YouTubePlayerViewX(final Context context, final AttributeSet set, final int n, final b b) {
        super((Context)ab.a(context, "context cannot be null"), set, n);
        this.c = ab.a(b, "listener cannot be null");
        if (this.getBackground() == null) {
            this.setBackgroundColor(-16777216);
        }
        this.setClipToPadding(false);
        this.requestTransparentRegion((View)(this.g = new n(context)));
        this.addView((View)this.g);
        this.b = new HashSet<View>();
        this.a = new a();
    }

    public final void initialize(final String s, final YouTubePlayer.OnInitializedListener onInitializedListener) {
        ab.a(s, (Object)"Developer key cannot be null or empty");
        this.c.a(this, s, onInitializedListener);
    }

    @Deprecated
    final void a(final boolean k) {
        if (k && Build.VERSION.SDK_INT < 14) {
            y.a("Could not enable TextureView because API level is lower than 14", new Object[0]);
            this.k = false;
            return;
        }
        this.k = k;
    }

    final void a(final FragmentActivity activity, final YouTubePlayer.Provider provider, final String s, final YouTubePlayer.OnInitializedListener onInitializedListener, final Bundle i) {
        if (this.e != null || this.j != null) {
            return;
        }
        ab.a(activity, "activity cannot be null");
        this.h = ab.a(provider, "provider cannot be null");
        this.j = ab.a(onInitializedListener, "listener cannot be null");
        this.i = i;
        this.g.b();
        (this.d = aa.a().a(this.getContext(), s, new t.a() {
            @Override
            public final void a() {
                if (YouTubePlayerViewX.this.d != null) {
                    YouTubePlayerViewX.a(YouTubePlayerViewX.this, activity);
                }
                YouTubePlayerViewX.this.d = null;
            }

            @Override
            public final void b() {
                if (!YouTubePlayerViewX.this.l && YouTubePlayerViewX.this.e != null) {
                    YouTubePlayerViewX.this.e.f();
                }
                YouTubePlayerViewX.this.g.a();
                if (YouTubePlayerViewX.this.indexOfChild((View)YouTubePlayerViewX.this.g) < 0) {
                    YouTubePlayerViewX.this.addView((View)YouTubePlayerViewX.this.g);
                    YouTubePlayerViewX.this.removeView(YouTubePlayerViewX.this.f);
                }
                YouTubePlayerViewX.this.f = null;
                YouTubePlayerViewX.this.e = null;
                YouTubePlayerViewX.this.d = null;
            }
        }, new t.b() {
            @Override
            public final void a(final YouTubeInitializationResult youTubeInitializationResult) {
                YouTubePlayerViewX.this.a(youTubeInitializationResult);
                YouTubePlayerViewX.this.d = null;
            }
        })).e();
    }

    private void a(final YouTubeInitializationResult youTubeInitializationResult) {
        this.e = null;
        this.g.c();
        if (this.j != null) {
            this.j.onInitializationFailure(this.h, youTubeInitializationResult);
            this.j = null;
        }
    }

    final void a() {
        if (this.e != null) {
            this.e.b();
        }
    }

    final void b() {
        if (this.e != null) {
            this.e.c();
        }
    }

    final void c() {
        if (this.e != null) {
            this.e.d();
        }
    }

    final void d() {
        if (this.e != null) {
            this.e.e();
        }
    }

    final void b(final boolean b) {
        if (this.e != null) {
            this.e.b(b);
            this.c(b);
        }
    }

    final void c(final boolean b) {
        this.l = true;
        if (this.e != null) {
            this.e.a(b);
        }
    }

    private void a(final View view) {
        if (view != this.g && (this.e == null || view != this.f)) {
            throw new UnsupportedOperationException("No views can be added on top of the player");
        }
    }

    public final void setPadding(final int n, final int n2, final int n3, final int n4) {
    }

    public final void setClipToPadding(final boolean b) {
    }

    public final void addView(final View view) {
        this.a(view);
        super.addView(view);
    }

    public final void addView(final View view, final int n) {
        this.a(view);
        super.addView(view, n);
    }

    public final void addView(final View view, final int n, final ViewGroup.LayoutParams layoutParams) {
        this.a(view);
        super.addView(view, n, layoutParams);
    }

    public final void addView(final View view, final int n, final int n2) {
        this.a(view);
        super.addView(view, n, n2);
    }

    public final void addView(final View view, final ViewGroup.LayoutParams layoutParams) {
        this.a(view);
        super.addView(view, layoutParams);
    }

    protected final void onMeasure(final int n, final int n2) {
        if (this.getChildCount() > 0) {
            final View child;
            (child = this.getChildAt(0)).measure(n, n2);
            this.setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
            return;
        }
        this.setMeasuredDimension(0, 0);
    }

    protected final void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.getChildCount() > 0) {
            this.getChildAt(0).layout(0, 0, n3 - n, n4 - n2);
        }
    }

    public final void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.e != null) {
            this.e.a(configuration);
        }
    }

    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getViewTreeObserver().addOnGlobalFocusChangeListener((ViewTreeObserver.OnGlobalFocusChangeListener)this.a);
    }

    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.getViewTreeObserver().removeOnGlobalFocusChangeListener((ViewTreeObserver.OnGlobalFocusChangeListener)this.a);
    }

    public final void clearChildFocus(final View view) {
        if (this.hasFocusable()) {
            this.requestFocus();
            return;
        }
        super.clearChildFocus(view);
    }

    public final void requestChildFocus(final View view, final View view2) {
        super.requestChildFocus(view, view2);
        this.b.add(view2);
    }

    public final void focusableViewAvailable(final View view) {
        super.focusableViewAvailable(view);
        this.b.add(view);
    }

    public final void addFocusables(final ArrayList<View> list, final int n) {
        final ArrayList<View> c = new ArrayList<View>();
        super.addFocusables((ArrayList)c, n);
        list.addAll(c);
        this.b.clear();
        this.b.addAll(c);
    }

    public final void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final ArrayList<View> c = new ArrayList<View>();
        super.addFocusables((ArrayList)c, n, n2);
        list.addAll(c);
        this.b.clear();
        this.b.addAll(c);
    }

    public final boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        if (this.e != null) {
            if (keyEvent.getAction() == 0) {
                return this.e.a(keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyEvent(keyEvent);
            }
            if (keyEvent.getAction() == 1) {
                return this.e.b(keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyEvent(keyEvent);
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    final Bundle e() {
        if (this.e == null) {
            return this.i;
        }
        return this.e.h();
    }

    static /* synthetic */ void a(final YouTubePlayerViewX YouTubePlayerViewX, final FragmentActivity activity) {
        d a;
        try {
            a = aa.a().a(activity, YouTubePlayerViewX.d, YouTubePlayerViewX.k);
        }
        catch (w.a a2) {
            y.a("Error creating YouTubePlayerViewX", a2);
            YouTubePlayerViewX.a(YouTubeInitializationResult.INTERNAL_ERROR);
            return;
        }
        YouTubePlayerViewX.e = new s(YouTubePlayerViewX.d, a);
        YouTubePlayerViewX.addView(YouTubePlayerViewX.f = YouTubePlayerViewX.e.a());
        YouTubePlayerViewX.removeView((View)YouTubePlayerViewX.g);
        YouTubePlayerViewX.c.a(YouTubePlayerViewX);
        if (YouTubePlayerViewX.j != null) {
            boolean a3 = false;
            if (YouTubePlayerViewX.i != null) {
                a3 = YouTubePlayerViewX.e.a(YouTubePlayerViewX.i);
                YouTubePlayerViewX.i = null;
            }
            YouTubePlayerViewX.j.onInitializationSuccess(YouTubePlayerViewX.h, YouTubePlayerViewX.e, a3);
            YouTubePlayerViewX.j = null;
        }
    }

    private final class a implements ViewTreeObserver.OnGlobalFocusChangeListener
    {
        public final void onGlobalFocusChanged(final View view, final View view2) {
            if (YouTubePlayerViewX.this.e != null && YouTubePlayerViewX.this.b.contains(view2) && !YouTubePlayerViewX.this.b.contains(view)) {
                YouTubePlayerViewX.this.e.g();
            }
        }
    }

    interface b
    {
        void a(final YouTubePlayerViewX p0, final String p1, final YouTubePlayer.OnInitializedListener p2);

        void a(final YouTubePlayerViewX p0);
    }
}