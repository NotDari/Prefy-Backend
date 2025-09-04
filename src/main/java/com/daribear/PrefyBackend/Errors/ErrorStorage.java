package com.daribear.PrefyBackend.Errors;

import javax.servlet.http.HttpServletResponse;

/**
 * The class which contains an enum of all the available custom errors.
 * ALso provides a helper method to convert an ErrorType enum into a CustomError object.
 */
public class ErrorStorage {

    public enum ErrorType{

        UserDetailsIncorrect(HttpServletResponse.SC_UNAUTHORIZED,1 , "Invalid Username/Password"),
        UserAccountLocked(HttpServletResponse.SC_UNAUTHORIZED,2,  "Account is locked"),
        UserAccountDisabled(HttpServletResponse.SC_UNAUTHORIZED,3,  "Account is disabled"),
        InternalError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,4,  "Internal Server Error"),
        UnknownError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,5,  "Unknown Error"),
        UserLoggedOut(HttpServletResponse.SC_UNAUTHORIZED,6,  "User logged out"),
        NOAUTHATTEMPTED(HttpServletResponse.SC_UNAUTHORIZED,7,  "No Auth attempted"),

        REGEUSERTAKE(HttpServletResponse.SC_UNAUTHORIZED,8,  "Registration Username Taken"),

        REGEMAILTAKE(HttpServletResponse.SC_UNAUTHORIZED,9,  "Registration Email Taken"),

        CurrentUserDeleted(HttpServletResponse.SC_FORBIDDEN, 10, "Current user deleted"),

        USERNOTFOUND(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 11, "User not found"),

        REGISTEREMAILNOTVALID(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 12, "Email not valid"),
        REGISTEREMAILTAKEN(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 13, "Email Already taken"),
        NOTVALIDDEVICE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 14, "NOT Valid Device");
        public final Integer responseCode;
        public final Integer errorCode;
        public final String message;



        ErrorType(Integer responseCode,Integer errorCode, String message) {
            this.responseCode = responseCode;
            this.errorCode = errorCode;
            this.message = message;
        }
    }

    /**
     *
     * @param errorType Type of error
     * @return an errorType converted into the CustomError.
     */
    public static CustomError getCustomErrorFromType(ErrorType errorType){
        return new CustomError(errorType.responseCode, errorType.message, errorType.errorCode);
    }

}
