package com.example.capstone1.UserInfo;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePWInfo implements Parcelable{
    private String userID;
    private String userName;
    private String birth;
    public ChangePWInfo(String userID, String userName, String birth){
        this.userID = userID;
        this.userName = userName;
        this.birth = birth;
    }

    protected ChangePWInfo(Parcel in){
        userID = in.readString();
        userName = in.readString();
        birth = in.readString();
    }

    public static final Parcelable.Creator<ChangePWInfo> CREATOR = new Parcelable.Creator<ChangePWInfo>() {
        @Override
        public ChangePWInfo createFromParcel(Parcel in) {
            return new ChangePWInfo(in);
        }

        @Override
        public ChangePWInfo[] newArray(int size) { return new ChangePWInfo[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(birth);
    }
}
