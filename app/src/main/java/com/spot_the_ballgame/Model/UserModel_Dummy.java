package com.spot_the_ballgame.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel_Dummy implements Serializable {

    @SerializedName("status")
    public String status;
    public String message;
    public Data data;

    private class Data {
        @SerializedName("usname")
        @Expose
        public String usname;

        public String getUsname() {
            return usname;
        }

        public void setUsname(String usname) {
            this.usname = usname;
        }


        public String admin_name;
        public String logintoken;
        public String ph_no;
        public String enabled;
        public String mode;
    }
}

