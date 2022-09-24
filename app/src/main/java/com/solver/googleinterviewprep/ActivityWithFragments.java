package com.solver.googleinterviewprep;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ActivityWithFragments extends AppCompatActivity {
    // private FragmentContainerView fragView;
    private SampleFragment fragment1;
    private SampleFragment2 fragment2;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragments);
    }

    public void clickOnViewFrag1(View view) {
        if (this.fragment1 == null) {
            this.fragment1 = new SampleFragment();
        }

        if (this.currentFragment != this.fragment1) {

            getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
                )
                .replace(R.id.fragmentContainerView, this.fragment1).commitNow();
            this.currentFragment = this.fragment1;
        }
    }

    public void clickOnViewFrag2(View view) {
        if (this.fragment2 == null) {
            this.fragment2 = new SampleFragment2();
        }

        if (this.currentFragment != this.fragment2) {
            getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
                ).replace(R.id.fragmentContainerView, this.fragment2).commitNow();
            this.currentFragment = this.fragment2;
        }


    }
}