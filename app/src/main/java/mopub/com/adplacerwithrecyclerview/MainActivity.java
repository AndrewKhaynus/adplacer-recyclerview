package mopub.com.adplacerwithrecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.Locale;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private MoPubRecyclerAdapter mMoPubRecyclerAdapter;
    private static final String AD_UNIT_ID = "11a17b188668469fb0412708c3d16813"; // A sample MoPub native ad unit ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView.Adapter originalAdapter = new DemoRecyclerAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.native_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        mMoPubRecyclerAdapter = new MoPubRecyclerAdapter(this, originalAdapter, new MoPubNativeAdPositioning.MoPubClientPositioning().enableRepeatingPositions(5));

        ViewBinder mViewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(mViewBinder);

        mMoPubRecyclerAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);
        mRecyclerView.setAdapter(mMoPubRecyclerAdapter);
        mMoPubRecyclerAdapter.loadAds(AD_UNIT_ID);
    }

    private static class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {
        private static final int ITEM_COUNT = 75;

        @Override
        public DemoViewHolder onCreateViewHolder(final ViewGroup parent,
                                                 final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_list_item, parent, false);
            return new DemoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DemoViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            if (holder.textView.getText().equals(""))
                holder.textView.setText(String.format(Locale.US, "Content Item #%d", position));
        }

        @Override
        public long getItemId(final int position) {
            return (long) position;
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    private static class DemoViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public DemoViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMoPubRecyclerAdapter.destroy();
        mMoPubRecyclerAdapter.setAdLoadedListener(null);
        mRecyclerView = null;
    }
}