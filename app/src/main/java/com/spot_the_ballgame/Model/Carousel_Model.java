package com.spot_the_ballgame.Model;

import java.util.ArrayList;

public class Carousel_Model {
    public String message, status;
    public ArrayList<Date_Contest> date_contest;
    public ArrayList<Normal_Contest> normal_contest;

    public static class Date_Contest {
    }

    public static class Normal_Contest {
        public String id, name, contest_id, image_url, get_type, enabled, created_at, updated_at, categories;
        /*This is for get_contest.php*/


        public String contest_name;
        public String categories_id;


        public String categories_image;
        public String sub_categories_name;
        public String team_a;
        public String team_a_name;
        public String team_b;
        public String team_b_name;

        public String price;
        public String timed;
        public String seconds;
        public String fee_type;
        public String entry_fee;
        public String powerup_2x;
        public String powerup_count;
        public String start_date_time;
        public String end_date_time;

        public String correct_mark;
        public String wrong_mark;
        public String skip;
        public String rules_id;
        public String status;
        public String play_status;
        public String rules_name;
    }
}
