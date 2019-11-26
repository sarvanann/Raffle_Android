package com.spot_the_ballgame.Model;

import java.util.ArrayList;

public class User_Wallet_Details_Model {
    public Datum datum;
    ArrayList<UserModel.Datum> data;
    public String message, code;

    public static class Datum {
        public String wallet1;
        public String wallet2;
    }

}
