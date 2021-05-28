package com.google.android.youtube.player;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.youtube.player.YouTubePlayerViewX.b;
import com.google.android.youtube.player.internal.ab;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class YouTubePlayerFragmentX extends Fragment implements Provider {
    private final YouTubePlayerFragmentX.a a = new YouTubePlayerFragmentX.a();
    private Bundle b;
    private YouTubePlayerViewX c;
    private String d;
    private YouTubePlayer.OnInitializedListener e;
    private boolean f;

    public static YouTubePlayerFragmentX newInstance() {
        return new YouTubePlayerFragmentX();
    }

    public YouTubePlayerFragmentX() {
    }

    public void initialize(String var1, YouTubePlayer.OnInitializedListener var2) {
        this.d = ab.a(var1, "Developer key cannot be null or empty");
        this.e = var2;
        this.a();
    }

    private void a() {
        if (this.c != null && this.e != null) {
            this.c.a(this.f);
            this.c.a(this.getActivity(), this, this.d, this.e, this.b);
            this.b = null;
            this.e = null;
        }

    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.b = var1 != null ? var1.getBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE") : null;
    }

    public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        this.c = new YouTubePlayerViewX(this.getActivity(), (AttributeSet)null, 0, this.a);
        this.a();
        return this.c;
    }

    public void onSaveInstanceState(Bundle var1) {
        super.onSaveInstanceState(var1);
        Bundle var2 = this.c != null ? this.c.e() : this.b;
        var1.putBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE", var2);
    }

    public void onStart() {
        super.onStart();
        this.c.a();
    }

    public void onResume() {
        super.onResume();
        this.c.b();
    }

    public void onPause() {
        this.c.c();
        super.onPause();
    }

    public void onStop() {
        this.c.d();
        super.onStop();
    }

    public void onDestroyView() {
        this.c.c(this.getActivity().isFinishing());
        this.c = null;
        super.onDestroyView();
    }

    public void onDestroy() {
        if (this.c != null) {
            FragmentActivity var1 = this.getActivity();
            this.c.b(var1 == null || var1.isFinishing());
        }

        super.onDestroy();
    }

    final class a implements b {
        private a() {
        }

        public final void a(YouTubePlayerViewX var1, String var2, YouTubePlayer.OnInitializedListener var3) {
            YouTubePlayerFragmentX.this.initialize(var2, YouTubePlayerFragmentX.this.e);
        }

        public final void a(YouTubePlayerViewX var1) {
        }
    }

}
