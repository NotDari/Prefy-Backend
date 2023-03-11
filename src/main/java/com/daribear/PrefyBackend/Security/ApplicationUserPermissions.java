package com.daribear.PrefyBackend.Security;

public enum ApplicationUserPermissions {
    Large_Data_Read("Large_Data:read"),
    Suggestion_READ("Suggestion:read"),
    Report_Read("report:read");

    private final String permission;

    ApplicationUserPermissions(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
