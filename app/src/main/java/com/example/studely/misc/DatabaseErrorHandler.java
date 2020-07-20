package com.example.studely.misc;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

public class DatabaseErrorHandler {

    public static String handleError(DatabaseError databaseError) {
        String errorMsg;
        switch (databaseError.getCode()) {
            case (DatabaseError.DISCONNECTED) :
            case (DatabaseError.NETWORK_ERROR) :
                errorMsg = "Network error";
                break;
            case (DatabaseError.INVALID_TOKEN) :
            case (DatabaseError.EXPIRED_TOKEN) :
                errorMsg = "Token error";
                break;
            case (DatabaseError.MAX_RETRIES) :
                errorMsg = "Too many tries";
                break;
            case (DatabaseError.PERMISSION_DENIED) :
                errorMsg = "Permission denied";
                break;
            case (DatabaseError.UNAVAILABLE) :
                errorMsg = "Currently unavailable";
                break;
            case (DatabaseError.OPERATION_FAILED) :
                errorMsg = "Operation failed";
                break;
            case (DatabaseError.OVERRIDDEN_BY_SET) :
                errorMsg = "Request overriden";
                break;
            case (DatabaseError.WRITE_CANCELED) :
                errorMsg = "Request cancelled";
                break;
            case (DatabaseError.USER_CODE_EXCEPTION) :
                errorMsg = "User code error";
                break;
            default:
                errorMsg = "Unknown error";
        }
        Log.e("DB ERROR", errorMsg);
        return errorMsg;
    }
}
