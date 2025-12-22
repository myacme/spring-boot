package com.example.boot.bean;


import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/4/8 16:32
 */
public class Variable {

    String key;
    String type;
    String sql;
    String value;
    String pictureName;
    ByteArrayOutputStream picture;
    List<String> head;
    List<LinkedHashMap<String, Object>> data;

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public ByteArrayOutputStream getPicture() {
        return picture;
    }

    public void setPicture(ByteArrayOutputStream picture) {
        this.picture = picture;
    }

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public List<LinkedHashMap<String, Object>> getData() {
        return data;
    }

    public void setData(List<LinkedHashMap<String, Object>> data) {
        this.data = data;
    }
}