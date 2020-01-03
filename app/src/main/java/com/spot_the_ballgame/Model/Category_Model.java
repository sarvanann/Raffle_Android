package com.spot_the_ballgame.Model;

import java.util.ArrayList;

/*This single model class used for all api's when the api calls categories*/
public class Category_Model {
    //    public Datum datum;
    public ArrayList<Data> data;
    public String message, code;

    /*This is for get_ranking.php*/
    public String your_rank;

    /*This is for app_settings.php*/
    public String wallet_coins;
    public String wallet_rupees;
    public String initial_coins;
    public String reward_point;
    public String min_withdraw_amt;
    public String max_withdraw_amt;

    /*This is for wallet_balance.php*/
    public String current_amt;
    public String wallet1;
    public String wallet2;



    public static class Data {
        /*This is for get_contest.php*/
        public String id;
        public String contest_id;
        public String contest_name;
        public String categories_id;
        public String categories;

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
        public String enabled;

        public String correct_mark;
        public String wrong_mark;
        public String skip;
        public String rules_id;
        public String status;
        public String play_status;

        /*This is for get_prize_distribution.php*/
        public String rank;
        public String prize_amount;
        public String no_rank;
        public String no_prize_amount;
        public String total_rank;
        public String total_amount;

        /*This is for get_question.php*/
        public String question;
        public String image_photo;
        public String audio;
        public String option_a;
        public String option_b;
        public String option_c;
        public String option_d;
        public String answer;
        public String question_countl;

        /*This is for rules_name.php*/
        public String rules_name;
        public String rules;


        /*This is user_wallet_details.php*/
        public String wallet1;
        public String wallet2;


        /*This is user_wallet_history.php*/
        public String date;
        public String time;
        public String playby;
        public String values_in;
        public String values_out;


        /*This is for result.php*/
        public String total_onclick_time;
        public String total_onclick_answer_values;

        /*This is for redeem.php*/
        public String email;
        public String phoneno;
        public String coins;
        public String amount;
        public String payment_type;

        /*This is for redeem_history.php*/
        public String request_status;
        public String created_at;
        public String updated_at;

        /*This is for get_ranking.php*/
        public String first_name;

        /*This is for get_played_records.php*/
        public String hint;
        public String question_count;

        /*This is for get_played_records.php*/
        public String contest_answer;
        public String onclick_time;
        public String click_points_correct;
        public String click_points_skip;
        public String click_points_wrong;


        /*This is for contest_history.php*/
        public String winning_prize;
        public String user_rank;
    }
}