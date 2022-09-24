package com.solver.googleinterviewprep;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityWithFragments extends AppCompatActivity {
    // private FragmentContainerView fragView;
    private SampleFragment fragment1;
    private SampleFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragments);

        // this.fragView = findViewById(R.id.fragmentContainerView);
    }

    public void clickOnViewFrag1(View view) {
        if (this.fragment1 == null) {
            this.fragment1 = new SampleFragment();
        }
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainerView, this.fragment1).commitNow();
    }

    public void clickOnViewFrag2(View view) {
        if (this.fragment2 == null) {
            this.fragment2 = new SampleFragment2();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
            this.fragment2).commitNow();
    }
}