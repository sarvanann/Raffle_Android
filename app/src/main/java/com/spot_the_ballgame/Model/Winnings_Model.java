package com.spot_the_ballgame.Model;

public class Winnings_Model {
    public String id, contest_id, rank, prize_amount, no_rank, no_prize_amount, total_rank, total_amount, created_at, updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

    public String getNo_rank() {
        return no_rank;
    }

    public void setNo_rank(String no_rank) {
        this.no_rank = no_rank;
    }

    public String getNo_prize_amount() {
        return no_prize_amount;
    }

    public void setNo_prize_amount(String no_prize_amount) {
        this.no_prize_amount = no_prize_amount;
    }

    public String getTotal_rank() {
        return total_rank;
    }

    public void setTotal_rank(String total_rank) {
        this.total_rank = total_rank;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
