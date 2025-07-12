package com.example.mymall;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_ADD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            case 4:
                return HomePageModel.SPONSOR_GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View BannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_add_layout, parent, false);
                return new BannerSliderViewHolder(BannerSliderView);
            case HomePageModel.STRIP_ADD_BANNER:
                View StripAddView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_add_layout, parent, false);
                return new StripAddBannerViewHolder(StripAddView);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(horizontalProductView);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridProductView);
            case HomePageModel.SPONSOR_GRID_PRODUCT_VIEW:
                View sponsorGridProductView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsored_grid_layout, parent, false);
                return new SponsorGridProductViewHolder(sponsorGridProductView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.STRIP_ADD_BANNER:
                int resource=homePageModelList.get(position).getResource();
                String color=homePageModelList.get(position).getBackgroundColor();
                ((StripAddBannerViewHolder)holder).setStripAd(resource,color);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String horizontalLayoutTitle=homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel>horizontalProductScrollModelList=homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewHolder)holder).setHorizontalProductLayout(horizontalProductScrollModelList,horizontalLayoutTitle);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridLayouttitle=homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel>gridProductModelList=homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewHolder)holder).setGridProductLayout(gridProductModelList,gridLayouttitle);
                break;
            case HomePageModel.SPONSOR_GRID_PRODUCT_VIEW:
                String SponsorGridLayoutTitle=homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel>sponsorGridProductModelList=homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((SponsorGridProductViewHolder)holder).setSponsorGridProductLayout(sponsorGridProductModelList,SponsorGridLayoutTitle);
                break;
                default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;
        private int currentPage = 2;
        private Timer timer;
        final private long delay_Time = 3000;
        final private long period_Time = 3000;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);

        }


        private void setBannerSliderViewPager(List<SliderModel> sliderModelList) {

            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    currentPage = i;

                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(sliderModelList);
                    }

                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideShow(sliderModelList);

            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(sliderModelList);
                    stopBannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(sliderModelList);
                    }
                    return false;
                }
            });


        }

        private void pageLooper(List<SliderModel> sliderModelList) {

            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);

            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);

            }
        }

        private void startBannerSlideShow(List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {

                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, delay_Time, period_Time);
        }

        private void stopBannerSlideShow() {
            timer.cancel();
        }
    }

    public class StripAddBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAddBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_add_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(int resource, String color) {
            stripAdImage.setImageResource(resource);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder{
        private TextView horizontalLayoutTitle;
        private Button horizontalViewAllBtn;
        private  RecyclerView horizontalRecyclerview;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle =itemView.findViewById(R.id.hr_scroll_title);
            horizontalViewAllBtn =itemView.findViewById(R.id.hr_scroll_btn);
            horizontalRecyclerview=itemView.findViewById(R.id.hr_scroll_recyclerview);
        }
        private void setHorizontalProductLayout(List<HorizontalProductScrollModel>horizontalProductScrollModelList,String title){

            horizontalLayoutTitle.setText(title);

            if (horizontalProductScrollModelList.size()>8){
                horizontalViewAllBtn.setVisibility(View.VISIBLE);
            }else {
                horizontalViewAllBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter=new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(itemView.getContext());
            linearLayoutManager .setOrientation(linearLayoutManager.HORIZONTAL);
            horizontalRecyclerview.setLayoutManager(linearLayoutManager);
            horizontalRecyclerview.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder{
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridView gridView;
        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);

             gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
             gridLayoutViewAllBtn =itemView.findViewById(R.id.grid_product_layout_view_all);
             gridView= itemView.findViewById(R.id.grid_product_layout_grid_view);

        }
        private void setGridProductLayout(List<HorizontalProductScrollModel>horizontalProductScrollModelList,String title){
            gridLayoutTitle.setText(title);
            gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
        }
    }
    public class SponsorGridProductViewHolder extends RecyclerView.ViewHolder{
        private TextView sponsorGridLayoutTitle;

        private GridView sponsorGridView;
        public SponsorGridProductViewHolder(@NonNull View itemView) {
            super(itemView);

            sponsorGridLayoutTitle = itemView.findViewById(R.id.grid_sponsored_product_layout_title);
            sponsorGridView= itemView.findViewById(R.id.grid_sponsored_product_layout_grid_view);

        }
        private void setSponsorGridProductLayout(List<HorizontalProductScrollModel>horizontalProductScrollModelList,String title){
            sponsorGridLayoutTitle.setText(title);
            sponsorGridView.setAdapter(new GridSponsoredProductLayoutAdapter(horizontalProductScrollModelList));
        }
    }
}
