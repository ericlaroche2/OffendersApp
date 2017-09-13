package com.example.ericlaroche.myapplication;

import java.io.Serializable;

/**
 * Created by Eric Laroche on 6/29/2017.
 */

public class Offender  implements Serializable{
    //private variables
    String _id;
    String _name;
    String _lastName;
    String _location;
    String _date;
    // Empty constructor
    public Offender(){

    }
    // constructor
    public Offender(String id, String name,String lastName, String location, String date){
        this._id = id;
        this._name = name;
        this._lastName = lastName;
        this._location = location;
        this._date = date;
    }

    // constructor
    public Offender(String id,String name){
        this._id = id;
        this._name = name;
    }
    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    public String getLastName(){
        return this._lastName;
    }

    // setting name
    public void setLastName(String lastName){
        this._lastName = lastName;
    }

    // getting phone number
    public String getLocation(){
        return this._location;
    }

    // setting phone number
    public void setLocation(String location){
        this._location = location;
    }


    // getting phone number
    public String getDate(){
        return this._date;
    }

    // setting phone number
    public void setDate(String date){
        this._date = date;
    }

    @Override
    public String toString(){return "ID:" +_id+ "  Name: "+ _name+"  Location: "+ _location + "  Date: "+_date;}

}
