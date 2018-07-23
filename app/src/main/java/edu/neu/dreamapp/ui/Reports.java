package edu.neu.dreamapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.model.News;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Reports extends BaseFragment {
    private static final String ASSET_PATH = "file:///android_asset/";
    private static final String CLASS_TAG = "Reports";

    @BindView(R.id.news_list)
    RecyclerView rv;

    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.reports_main;
    }

    @Override
    protected String getTAG() {
        return CLASS_TAG;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /* Set Recycler View */
        rv.setLayoutManager(new LinearLayoutManager(context));

        /* Setup RefreshLayout Listener, When Page Refreshes, Load Again */
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                /* Load Data */
                loadData();
            }
        });

        /* Auto Refreshes The Layout In-Order To Fetch The NEWS */
        refreshLayout.autoRefresh();
    }

    /**
     * Load Data
     */
    private void loadData() {
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);

        /* Get Responses */
        Set<String> set = prefs.getStringSet("SR_RESP_SET_PRE", new HashSet<String>());
        Log.i("*****", String.valueOf(set.size()));
        if (0 < set.size()) {
            List<News> newsRecords = new ArrayList<>();

            /* Iterate Surveys */
            for (final String value : set) {
                String[] values = value.split(";");

                News n = new News();
                n.setHeader("Survey Response");
                n.setAuthor("Date: " + values[0]);
                n.setContent("Total Students: " + values[1].split(",").length
                        + "\n"
                        + "Students Present: " + values[2].split(",").length);
                newsRecords.add(n);
            }
            rv.setAdapter(new Reports.NewsListAdapter(newsRecords));
            refreshLayout.finishRefresh();
        }
    }

    /**
     * News List Adapter
     */
    class NewsListAdapter extends RecyclerView.Adapter<Reports.NewsViewHolder> {
        private final List<News> news;

        NewsListAdapter(List<News> news) {
            this.news = news;
        }

        @Override
        public Reports.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View v = layoutInflater.inflate(R.layout.news_card, viewGroup, false);
            return new Reports.NewsViewHolder(v);
        }

        private byte[] loadHTMLasByteArray(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            return out.toByteArray();
        }

        @Override
        @SuppressWarnings("all")
        public void onBindViewHolder(Reports.NewsViewHolder newsViewHolder, final int i) {
            /* Set Attributes */
            newsViewHolder.newsImage.setWebViewClient(new InAppBrowser());
            newsViewHolder.newsImage.getSettings().setLoadsImagesAutomatically(true);
            newsViewHolder.newsImage.getSettings().setJavaScriptEnabled(true);
            newsViewHolder.newsImage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            /* Read Chart HTML */
            String fileContent = "";
            try {
                AssetManager assetManager = getActivity().getAssets();
                InputStream in = assetManager.open("piechart.html");
                byte[] bytes = loadHTMLasByteArray(in);
                fileContent = new String(bytes, "UTF-8");
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Failed To Load Google Charts!", Toast.LENGTH_LONG).show();
            }
            final String content = fileContent;
            String[] nums = news.get(i).getContent().split("\n");
            final String formattedContent = content.replace("[#DataTable#]", "['Attendance','Percentage'],['Present',"
                    + Integer.parseInt(nums[0].split(": ")[1])
                    + "],['Absent',"
                    + Integer.parseInt(nums[1].split(": ")[1]) + "]");
            newsViewHolder.newsImage.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "UTF-8", null);
            newsViewHolder.newsHeader.setText(news.get(i).getHeader());
            newsViewHolder.newsAuthor.setText(news.get(i).getAuthor());
            newsViewHolder.newsContent.setText(news.get(i).getContent());

            /* Add Listener */
            newsViewHolder.holder.setBackground(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.selector_new_card_white));
            newsViewHolder.newsHeader.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));
            newsViewHolder.newsAuthor.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));
            newsViewHolder.newsContent.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.app_black));

            newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail("agrawroh@bu.edu", "Report", formattedContent.replace("./loader.js", "https://www.gstatic.com/charts/loader.js"));
                }
            });
        }

        /**
         * Send Email
         *
         * @param recipient Recipient
         * @param subject   Subject
         * @param body      Body
         */
        protected void sendEmail(String recipient, String subject, String body) {
            String[] recipients = {recipient.toString()};
            Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
            email.setType("text/html");

            email.putExtra(Intent.EXTRA_EMAIL, recipients);
            email.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
            email.putExtra(Intent.EXTRA_TEXT, body.toString());

            try {
                startActivity(Intent.createChooser(email, "Choose Email Client..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity().getApplicationContext(), "No Email Client Installed!",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public int getItemCount() {
            return news.size();
        }
    }

    /**
     * News View Holder
     */
    class NewsViewHolder extends RecyclerView.ViewHolder {
        private WebView newsImage;
        private TextView newsHeader;
        private TextView newsAuthor;
        private TextView newsContent;
        private View holder;

        NewsViewHolder(View itemView) {
            super(itemView);
            holder = itemView.findViewById(R.id.holder);
            newsImage = itemView.findViewById(R.id.news_image);
            newsHeader = itemView.findViewById(R.id.news_header);
            newsAuthor = itemView.findViewById(R.id.news_author);
            newsContent = itemView.findViewById(R.id.news_body);
        }
    }

    /**
     * InApp Browser
     */
    private class InAppBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
