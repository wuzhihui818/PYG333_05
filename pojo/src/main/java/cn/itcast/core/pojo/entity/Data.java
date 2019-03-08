package cn.itcast.core.pojo.entity;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private List<Object> month;
    private List<Object> totalFee;

    @Override
    public String toString() {
        return "Data{" +
                "month=" + month +
                ", totalFee=" + totalFee +
                '}';
    }

    public List<Object> getMonth() {
        return month;
    }

    public void setMonth(List<Object> month) {
        this.month = month;
    }

    public List<Object> getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(List<Object> totalFee) {
        this.totalFee = totalFee;
    }
}
