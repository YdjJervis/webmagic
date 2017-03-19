package com.zuozuo.spider.pojo;

public class Task {

    public String totalPrice;
    public String commission;
    public String publisher;
    public String requirements;
    public String operation;

    @Override
    public String toString() {
        return "Task{" +
                "totalPrice='" + totalPrice + '\'' +
                ", commission='" + commission + '\'' +
                ", publisher='" + publisher + '\'' +
                ", requirements='" + requirements + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
