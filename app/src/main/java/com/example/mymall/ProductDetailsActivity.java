package com.example.mymall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager productImagesViewPager;
    private ViewPager productDetailsViewPager;
    private TableLayout productDetailsTabLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        productImagesViewPager=findViewById(R.id.product_images_viewpager);
        productDetailsViewPager =findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout=findViewById(R.id.product_details_tablayout);
        List<Integer>productImages=new ArrayList<>();
        productImages.add(R.drawable.logo);
        productImages.add(R.drawable.cart);
        productImages.add(R.drawable.baseline_home_24);
        productImages.add(R.drawable.background);

        ProductImagesAdapter productImagesAdapter=new ProductImagesAdapter(productImages);
        productImagesViewPager.setAdapter(productImagesAdapter);

productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTabLayout.getTabCount()));
productDetailsTabLayout.addOnTa
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection
            int id = item.getItemId();

            if (id == android.R.id.home) {
                finish();
                return true;
            } else if (id == R.id.main_cart_icon) {
                //cart
                return true;
            }
            else if (id == R.id.main_search_icon) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

}