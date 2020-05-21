package com.spot_the_ballgame.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.spot_the_ballgame.Fragments.My_Contest_Fragment;
import com.spot_the_ballgame.Game_Details_Screen_Act;
import com.spot_the_ballgame.Game_Two_Act;
import com.spot_the_ballgame.Interface.Factory;
import com.spot_the_ballgame.Model.Carousel_Model;
import com.spot_the_ballgame.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class View_Pager_Adapter extends PagerAdapter {
    private List<Carousel_Model.Banner_Contest> banner_contests;
    private Context context;

    private String str_rule_id;
    private String str_status_onclick;
    private String str_end_game;
    private ArrayList<String> stringArrayList = new ArrayList<>();

    private String str_imagepath;
    private String str_correct_ans, str_wrong_ans, str_skip;
    private long lng_seconds;
    private long different_milli_seconds;
    private String str_seconds, str_2x_powerup, str_contest_id, str_entry_fees;
    private String str_categories;
    private String str_question_type;
    private String str_get_type;
    private String str_url;
    String str_team_a_path;
    String str_local_host;
    int height = 0;
    String str_status;
    private LayoutInflater inflater;
    FragmentManager fragmentManager;

    public View_Pager_Adapter(ArrayList<Carousel_Model.Banner_Contest> arrayList, FragmentActivity activity, FragmentManager support_fragmentManager) {
        this.banner_contests = arrayList;
        this.context = activity;
        this.fragmentManager = support_fragmentManager;
    }

    @Override
    public int getCount() {
        return banner_contests.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.album_card, container, false);
        RoundedImageView imageView;
        CardView card_view_in_album_card;
        imageView = view.findViewById(R.id.image);
        card_view_in_album_card = view.findViewById(R.id.card_view_in_album_card);


        str_local_host = Factory.BASE_URL_FOR_IMAGE_LOCAL_HOST;
        str_status = banner_contests.get(position).play_status;
        str_categories = banner_contests.get(position).categories;
        str_question_type = banner_contests.get(position).question_type;
        str_get_type = banner_contests.get(position).get_type;
        str_url = banner_contests.get(position).url;


        str_imagepath = str_local_host + banner_contests.get(position).image_url;
//        Log.e("str_local_host", str_local_host);
//        Log.e("banner_contests", banner_contests.get(position).image_url);
//        Log.e("str_imagepath", str_imagepath);


        Glide.with(context).load(str_imagepath)
                .thumbnail(Glide.with(context).load(str_imagepath))
                .into(imageView);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {

            final String t2 = banner_contests.get(position).end_date_time;
            stringArrayList.add(t2);

//            Log.e("t22222", t2);
//        Collections.sort(stringArrayList);

            /*Getting Current Time*/
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String strDate = mdformat.format(calendar.getTime());


            Date current_system_time = mdformat.parse(strDate);
            Date end_date_api = sdf.parse(t2);
            different_milli_seconds = end_date_api.getTime() - current_system_time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }


        String fees_type = banner_contests.get(position).fee_type;

        card_view_in_album_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_status = banner_contests.get(position).play_status;
                str_get_type = banner_contests.get(position).get_type;
                str_url = banner_contests.get(position).url;
//                Log.e("str_statussss_banner", str_status);
                str_correct_ans = banner_contests.get(position).correct_mark;
                str_wrong_ans = banner_contests.get(position).wrong_mark;
                str_skip = banner_contests.get(position).skip;
                Carousel_Model.Banner_Contest s1 = banner_contests.get(position);
                str_categories = banner_contests.get(position).categories;
                str_question_type = banner_contests.get(position).question_type;
                str_imagepath = banner_contests.get(position).categories_image;
                str_rule_id = banner_contests.get(position).rules_id;
                str_status_onclick = banner_contests.get(position).play_status;
                str_end_game = banner_contests.get(position).end_date_time;
                String prize_type = banner_contests.get(position).prize_type;

                if (str_status.equalsIgnoreCase("2")) {
                    Fragment fragment = new My_Contest_Fragment();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    if (str_get_type.equals("static")) {
                        Log.e("static_url", str_url);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(str_url));
                        context.startActivity(intent);
                    } else {

                        /*
                         *
                         * 0-->Text
                         * 1-->Image
                         * 2-->Audio
                         * 3-->Prediction
                         * 4-->STB
                         *
                         * */
                    /*if (str_categories.equalsIgnoreCase("Trivia_Image")
                            || str_categories.equalsIgnoreCase("Trivia")
                            || str_categories.equalsIgnoreCase("Prediction")) {*/
                        if (str_question_type.equals("1")
                                || str_question_type.equals("0")
                                || str_question_type.equals("3")) {

                            //  Toast.makeText(context, "Rule ID :" + " " + str_rule_id, Toast.LENGTH_SHORT).show();
                            String fees_type = banner_contests.get(position).fee_type;
                            if (fees_type.equalsIgnoreCase("0")) {
                                str_entry_fees = "Free";
                            } else {
                                str_entry_fees = banner_contests.get(position).entry_fee;
                            }
                            Intent intent1 = new Intent(context, Game_Two_Act.class);
                            str_seconds = banner_contests.get(position).seconds;
                            str_2x_powerup = banner_contests.get(position).powerup_count;
                            str_contest_id = banner_contests.get(position).contest_id;
                            intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                            intent1.putExtra("game_name", banner_contests.get(position).contest_name);
                            intent1.putExtra("prize_amount", banner_contests.get(position).price);
                            intent1.putExtra("end_time", str_end_game);
                            intent1.putExtra("count_down_seconds", str_seconds);
                            intent1.putExtra("str_2x_powerup", str_2x_powerup);
                            intent1.putExtra("str_contest_id", str_contest_id);
                            intent1.putExtra("str_entry_fees", str_entry_fees);
                            intent1.putExtra("str_rule_id", str_rule_id);
                            intent1.putExtra("str_status_onclick", str_status_onclick);
                            intent1.putExtra("str_imagepath", str_imagepath);
                            intent1.putExtra("str_categories", str_categories);
                            intent1.putExtra("str_question_type", str_question_type);

                            intent1.putExtra("str_correct_ans", str_correct_ans);
                            intent1.putExtra("str_wrong_ans", str_wrong_ans);
                            intent1.putExtra("str_skip", str_skip);
                            intent1.putExtra("prize_type", prize_type);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent1);
                        }

                        /*
                         *
                         * 0-->Text
                         * 1-->Image
                         * 2-->Audio
                         * 3-->Prediction
                         * 4-->STB
                         *
                         * */

//                  } else if (str_categories.equalsIgnoreCase("Spot the ball")) {
                        else if (str_question_type.equals("4")) {
                            String fees_type = banner_contests.get(position).fee_type;
                            if (fees_type.equalsIgnoreCase("0")) {
                                str_entry_fees = "Free";
                            } else {
                                str_entry_fees = banner_contests.get(position).entry_fee;
                            }
                            str_seconds = banner_contests.get(position).seconds;
                            str_2x_powerup = banner_contests.get(position).powerup_count;
                            str_contest_id = banner_contests.get(position).contest_id;
//                    Log.e("str_secondsdsds", str_seconds);
                            Intent intent1 = new Intent(context, Game_Details_Screen_Act.class);
                            intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                            intent1.putExtra("game_name", banner_contests.get(position).contest_name);
                            intent1.putExtra("prize_amount", banner_contests.get(position).price);
                            intent1.putExtra("end_time", str_end_game);
                            intent1.putExtra("count_down_seconds", str_seconds);
                            intent1.putExtra("str_2x_powerup", str_2x_powerup);
                            intent1.putExtra("str_contest_id", str_contest_id);
                            intent1.putExtra("str_entry_fees", str_entry_fees);
                            intent1.putExtra("str_rule_id", str_rule_id);
                            intent1.putExtra("str_status_onclick", str_status_onclick);
                            intent1.putExtra("str_imagepath", str_imagepath);
                            intent1.putExtra("str_categories", str_categories);
                            intent1.putExtra("str_question_type", str_question_type);
                            intent1.putExtra("int_onclcik_value", String.valueOf(s1));
                            intent1.putExtra("str_correct_ans", str_correct_ans);
                            intent1.putExtra("str_wrong_ans", str_wrong_ans);
                            intent1.putExtra("str_skip", str_skip);
                            intent1.putExtra("prize_type", prize_type);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent1);
                        }
                    }
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "" + banner_contests.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
