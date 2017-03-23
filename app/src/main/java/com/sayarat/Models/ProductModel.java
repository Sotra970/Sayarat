package com.sayarat.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ahmed on 2/22/2017.
 */

public class ProductModel implements Serializable {
    String likes , views , type , title , make , year ,advertisertype,usingstatus,pricetype,pricevalue,details,vehicleset,vehiclestatus,odemetertype,odemetervaue,Guarantee,advertiserID,date,automatic,product_id ,Address , phone  , active;
    UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    ArrayList<String> imgs = new ArrayList<>() ;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAdvertisertype() {
        return advertisertype;
    }

    public void setAdvertisertype(String advertisertype) {
        this.advertisertype = advertisertype;
    }

    public String getUsingstatus() {
        return usingstatus;
    }

    public void setUsingstatus(String usingstatus) {
        this.usingstatus = usingstatus;
    }

    public String getPricetype() {
        return pricetype;
    }

    public void setPricetype(String pricetype) {
        this.pricetype = pricetype;
    }

    public String getPricevalue() {
        return pricevalue;
    }

    public void setPricevalue(String pricevalue) {
        this.pricevalue = pricevalue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVehicleset() {
        return vehicleset;
    }

    public void setVehicleset(String vehicleset) {
        this.vehicleset = vehicleset;
    }

    public String getVehiclestatus() {
        return vehiclestatus;
    }

    public void setVehiclestatus(String vehiclestatus) {
        this.vehiclestatus = vehiclestatus;
    }

    public String getOdemetertype() {
        return odemetertype;
    }

    public void setOdemetertype(String odemetertype) {
        this.odemetertype = odemetertype;
    }

    public String getOdemetervaue() {
        return odemetervaue;
    }

    public void setOdemetervaue(String odemetervaue) {
        this.odemetervaue = odemetervaue;
    }

    public String getGuarantee() {
        return Guarantee;
    }

    public void setGuarantee(String guarantee) {
        Guarantee = guarantee;
    }

    public String getAdvertiserID() {
        return advertiserID;
    }

    public void setAdvertiserID(String advertiserID) {
        this.advertiserID = advertiserID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAutomatic() {
        return automatic;
    }

    public void setAutomatic(String automatic) {
        this.automatic = automatic;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

}
