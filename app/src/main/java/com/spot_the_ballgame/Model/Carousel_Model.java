package com.spot_the_ballgame.Model;

import java.util.ArrayList;

public class Carousel_Model {
    public String message, status;
    public ArrayList<Banner_Contest> banner_contest;

    public static class Banner_Contest {
        public String id;
        public String name;
        public String contest_id;

        public String image_url;


        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }


        public String url;
        public String get_type;
        public String enabled;
        public String created_at;
        public String updated_at;
        public String categories;
        public String contest_name;
        public String categories_id;
        public String categories_image;
        public String sub_categories_name;
        public String team_a;
        public String team_a_name;
        public String team_b;
        public String team_b_name;
        public String prize_type;
        public String price;
        public String entry_amt_type;
        public String language_type;
        public String timed;
        public String seconds;
        public String rewarded_points;
        public String fee_type;
        public String adformat;
        public String entry_fee;
        public String coins;
        public String powerup_2x;
        public String powerup_count;
        public String correct_mark;
        public String wrong_mark;
        public String skip;
        public String rules_id;
        public String rules_name;
        public String start_date_time;
        public String end_date_time;
        public String question_type;
        public String play_status;
        public String status;


        public Banner_Contest(String image) {
            this.image_url = image;
        }
    }
}