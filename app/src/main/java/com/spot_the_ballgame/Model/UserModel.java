package com.spot_the_ballgame.Model;
import java.util.ArrayList;
/*This class is used for all signup and signin api's calling*/
public class UserModel {
    public Datum datum;
    ArrayList<Datum> data;
    public String message, code;

    public static class Datum {
        public String id;
        public String phoneno;
        public String walet;
        public String money;
        public String active;
        public String verified;


        public String source_detail;
        public String first_name;
        public String last_name;
        public String email;
        public String app_id;
        public String image;
        public String username;
        public String password;
        public String token;
    }

}