package cn.itcast.core.pojo.entity;

import java.io.Serializable;

public class OrderEntity implements Serializable {
    private String goods_name;
    private Integer sl;
    private Long price;

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "goods_name='" + goods_name + '\'' +
                ", sl=" + sl +
                ", price=" + price +
                '}';
    }
}