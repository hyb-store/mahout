package com.hyb.pojo;

public class Item {
    private Long pid;
    private String name;
    private String types;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Item{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", types='" + types + '\'' +
                '}';
    }
}