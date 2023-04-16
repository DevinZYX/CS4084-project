package com.example.forum;

public class utils {

    public static int getDay(String day) {
        int j = 0;
        switch (day) {
            case "Monday":{
                j = 1;
                break;
            }
            case "Tuesday":{
                j = 2;
                break;
            }
            case "Wednesday":{
                j = 3;
                break;
            }
            case "Thursday":{
                j = 4;
                break;
            }
            case "Friday":{
                j = 5;
                break;
            }
            case "Saturday":{
                j = 6;
                break;
            }
            case "Sunday":{
                j = 7;
                break;
            }
        }
        return j;
    }

}
