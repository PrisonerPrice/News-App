package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news){
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View ConvertView, ViewGroup parent){
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = ConvertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getNewsSection());

        TextView dateView = (TextView) listItemView.findViewById(R.id.published_date);
        dateView.setText(formatDate(currentNews.getPublishedDateAndTime()));

        TextView timeView = (TextView) listItemView.findViewById(R.id.published_time);
        timeView.setText(formatTime(currentNews.getPublishedDateAndTime()));

        return listItemView;
    }

    public String formatDate(String input){
        int position = input.indexOf("T");
        String output;
        if(position == -1){
            output = input;
        } else{
            output = input.substring(0,position);
        }
        return output;
    }

    public String formatTime(String input){
        int position = input.indexOf("T");
        int length = input.length();
        String output;
        if(position == -1){
            output = input;
        } else{
            output = input.substring(position + 1, length - 1);
        }
        return output;
    }
}
