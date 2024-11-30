package com.spiceswap.spiceswap.common.util;

public class Constants {
    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }

    public static final class BrowseRecipe {
        public static final String BROWSE_RECIPE_PATS = BASE_URL + "/browse-recipe";
    }

    public static final class OtherUserProfile {
        public static final String OTHER_USER_PROFILE_PATS = BASE_URL + "/user";
    }

    public static final class ManageUserProfile {
        public static final String MANAGE_USER_PROFILE_PATS = BASE_URL + "/me";
        public static final String MANAGE_USER_PROFILE_PATS_ALL = MANAGE_USER_PROFILE_PATS + "/**";
    }

    public static final class ManageRecipe {
        public static final String MANAGE_RECIPE_PATS = BASE_URL + "/recipe";
        public static final String MANAGE_RECIPE_PATS_ALL = MANAGE_RECIPE_PATS + "/**";
    }

    public static final class ModificationRequest {
        public static final String MODIFICATION_REQUEST_PATS = BASE_URL + "/modification-request";
        public static final String MODIFICATION_REQUEST_PATS_ALL = MODIFICATION_REQUEST_PATS + "/**";
    }

    public static final class ManageIngredient {
        public static final String MANAGE_INGREDIENT_PATS = BASE_URL + "/ingredient";
        public static final String MANAGE_INGREDIENT_PATS_ALL = MANAGE_INGREDIENT_PATS + "/**";
    }

    public static final class AdminManageRecipe {
        public static final String ADMIN_MANAGE_RECIPE_PATS = BASE_URL + "/manage-recipe";
        public static final String ADMIN_MANAGE_RECIPE_PATS_ALL = ADMIN_MANAGE_RECIPE_PATS + "/**";
    }

    public static final class AdminManageUser {
        public static final String ADMIN_MANAGE_USER_PATS = BASE_URL + "/manage-user";
        public static final String ADMIN_MANAGE_USER_PATS_ALL = ADMIN_MANAGE_USER_PATS + "/**";
    }

    public static final class RecipeReview {
        public static final String RECIPE_REVIEW_PATS = BASE_URL + "/review";
        public static final String RECIPE_REVIEW_PATS_ALL = RECIPE_REVIEW_PATS + "/**";
    }

    public static final class CommonPats {
            public static final String[] SECURE_LIST_PATS = {
                    ManageRecipe.MANAGE_RECIPE_PATS_ALL,
                    ManageIngredient.MANAGE_INGREDIENT_PATS_ALL,
                    ManageUserProfile.MANAGE_USER_PROFILE_PATS_ALL,
                    ModificationRequest.MODIFICATION_REQUEST_PATS_ALL,
                    RecipeReview.RECIPE_REVIEW_PATS_ALL,
                    AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS_ALL,
                    AdminManageUser.ADMIN_MANAGE_USER_PATS_ALL
            };
    }

    public static final class ErrorMessage {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USERNAME_NOT_FOUND = "Username not found";
        public static final String EMAIL_NOT_FOUND = "Email not found";
        public static final String WRONG_PASSWORD = "Wrong password";
        public static final String EMAIL_ALREADY_VERIFIED = "Email has been verified";
        public static final String EMAIL_NOT_VERIFIED = "Email has not been verified";
        public static final String TOKEN_EXPIRED = "Token expired";
        public static final String TOKEN_NOT_FOUND = "Token not found";
        public static final String INVALID_TOKEN_TYPE = "Invalid token type";
        public static final String INVALID_TOKEN = "Invalid token";
        public static final String REGISTER_TOKEN_NOT_FOUND = "Register token not found";
        public static final String SEND_EMAIL_FAILED = "Failed to send email";
        public static final String TWO_MIN_COOL_DOWN = "Wait 2 minutes to resend";
        public static final String INACTIVE_USER = "This account has been disabled";
        public static final String REGISTER_FAILED = "Failed to register account";
        public static final String LOGIN_FAILED = "Login failed";
        public static final String REFRESH_TOKEN_FAILED = "Failed to refresh token";
        public static final String VERIFY_EMAIL_FAILED = "Failed to verify email";
        public static final String EXTRACT_JWT_FAILED = "Failed to extract jwt";
        public static final String LOGOUT_FAILED = "Failed to log out";
        public static final String CHECK_FIELD_EXISTS_FAILED = "Failed to check the existence of the value of the related field";
        public static final String FAILED_ADD_ADMIN = "Failed to add admin data";
        public static final String FAILED_UPLOAD_IMG = "Failed to upload image";
        public static final String FAILED_UPDATE_IMG = "Failed to update image";
        public static final String FAILED_DELETE_IMG = "Failed to delete image";
        public static final String FAILED_ADD_RECIPE = "Failed to add new recipe";
        public static final String FAILED_UPDATE_RECIPE = "Failed to update recipe";
        public static final String FAILED_DELETE_RECIPE = "Failed to delete recipe";
        public static final String FAILED_GET_UPDATE_RECIPE_DETAIL = "Failed to load recipe update detail";
        public static final String FAILED_GET_SETTING_RECIPE_DETAIL = "Failed to load recipe setting detail";
        public static final String FAILED_SET_RECIPE = "Failed to change recipe setting";
        public static final String FAILED_GET_ALL_MY_RECIPE = "Failed to load all my recipes";
        public static final String FAILED_GET_MY_RECIPE_DETAIL = "Failed to load my recipe detail";
        public static final String FAILED_GET_ALL_BOOKMARKED_RECIPE = "Failed to load all bookmarked recipes";
        public static final String FAILED_CREATE_BOOKMARK = "Failed to bookmark the recipe";
        public static final String FAILED_DELETE_BOOKMARK = "Failed to delete bookmark on recipe";
        public static final String FAILED_GET_RECIPE_HISTORIES = "Failed to load recipe histories";
        public static final String FAILED_GET_RECIPE_HISTORY_DETAIL = "Failed to load recipe history detail";
        public static final String FAILED_GET_RECIPE_LIST = "Failed to load recipe list";
        public static final String FAILED_GET_RECIPE_DETAIL = "Failed to load recipe detail";
        public static final String FAILED_CREATE_INGREDIENT = "Failed to add ingredient";
        public static final String FAILED_UPDATE_INGREDIENT = "Failed to update ingredient";
        public static final String FAILED_DELETE_INGREDIENT = "Failed to delete ingredient";
        public static final String FAILED_GET_INGREDIENT_LIST = "Failed to load ingredient list";
        public static final String FAILED_CHANGE_RECIPE_STATUS = "Failed to change recipe status";
        public static final String FAILED_GET_RECIPE_INFORMATION = "Failed to load information about all recipe";
        public static final String RECIPE_NOT_FOUND = "Recipe not found";
        public static final String RECIPE_HISTORY_NOT_FOUND = "No recipe history found";
        public static final String RECIPE_HISTORY_DETAIL_NOT_FOUND = "Recipe history detail not found";
        public static final String INGREDIENT_DETAIL_NOT_FOUND = "No ingredient found with slug: ";
        public static final String STEP_NOT_FOUND = "No step found with slug: ";
        public static final String INGREDIENT_NOT_FOUND = "Ingredient not found";
        public static final String IMAGE_PROFILE_NOT_FOUND = "Profile picture not found";
        public static final String ORIGINAL_RECIPE_NOT_FOUND = "Copy recipe not found or recipe does not have original recipe (Is it an original recipe or the original recipe has been deleted)";
        public static final String MODIFICATION_REQUEST_NOT_FOUND = "Modification request not found";
        public static final String RECIPE_REVIEW_NOT_FOUND = "No reviews found";
        public static final String INPUT_CANNOT_NULL = "Input cannot be null or empty";
        public static final String FAILED_CHECK_FIELD_EXISTS= "Failed to check for field existence";
        public static final String FILED_GET_TWO_WORDS = "Failed to pick up the first 2 letters";
        public static final String FAILED_GET_USER_DETAIL = "Failed to load user data";
        public static final String FAILED_UPDATE_USER_DETAIL = "Failed to update user data";
        public static final String FAILED_UPDATE_PROFILE_IMAGE = "Failed to update profile picture";
        public static final String FAILED_DELETE_PROFILE_IMAGE = "Failed to delete profile picture";
        public static final String FAILED_CREATE_MODIFICATION_REQUEST = "Failed to create modification request";
        public static final String FAILED_GET_MODIFICATION_REQUEST = "Failed to load modification request list";
        public static final String FAILED_GET_RECIPE_COPY = "Failed to load copy recipes for modification request";
        public static final String FAILED_TO_APPROVE_REQUEST = "Failed to approve modification request";
        public static final String FAILED_TO_REJECT_REQUEST = "Failed to reject the modification request";
        public static final String INVALID_CURRENT_PASSWORD = " Invalid current password";
        public static final String INVALID_CONFIRM_PASSWORD = "New password and confirmation password do not match";
        public static final String FAILED_CHANGE_PASSWORD = "Failed to change password";
        public static final String FAILED_RESET_PASSWORD = "Failed to reset password";
        public static final String FAILED_DOWNLOAD_IMG = "Failed to download image";
        public static final String FAILED_COPY_RECIPE = "Failed to copy recipe";
        public static final String FAILED_GET_OTHER_USER_PROFILE = "Failed to load other user profile data";
        public static final String FAILED_GET_ALL_USER_DATA = "Failed to load user data list";
        public static final String FAILED_CHANGE_USER_STATUS = "Failed to change user status";
        public static final String FAILED_GET_USER_INFORMATION = "Failed to load information about all user";
        public static final String FAILED_WRITE_REVIEW = "Failed to add recipe review";
        public static final String FAILED_GET_REVIEW = "Failed to load recipe review";
        public static final String FAILED_DELETE_REVIEW = "Failed to delete recipe review";
        public static final String FAILED_GET_REVIEW_LIST = "Failed to load recipe review list";
        public static final String INVALID_SLUG = "Invalid slug";
        public static final String CONFLICT_REVIEW = "The review has not been modified";
        public static final String MAX_COPY_RECIPE = "Cannot copy more than 3 recipes";
        public static final String FORBIDDEN_BOOKMARK = "Cannot bookmark your own recipes";
        public static final String FORBIDDEN_EXIST_BOOKMARK = "Recipe has been bookmarked";
        public static final String FORBIDDEN_NOT_EXIST_BOOKMARK = "The bookmark has been deleted";
        public static final String FORBIDDEN_COPY = "Cannot copy own recipe";
        public static final String FORBIDDEN_ACCESS_PRIVATE = "Can't access private recipe";
        public static final String FORBIDDEN_APPROVE_REQUEST = "Cannot approve or reject request modification to the original recipe";
        public static final String FORBIDDEN_PRIVATE_REQUEST = "The destination recipe is currently private";
        public static final String FORBIDDEN_EMPTY_HISTORIES = "There have been no updates on the copy recipe you wish to apply for";
        public static final String FORBIDDEN_REVIEW = "Cannot review own recipe";
        public static final String FORBIDDEN_NOT_EXIST_REVIEW = "Review has been deleted";
        public static final String EXISTS_REQUEST = "Have made a previous modification request and it is still in waiting status";
    }

    public static final class EmailMessage {
        public static final String RESET_PASSWORD_SUBJECT = "Reset SpiceSwap Password";
        public static final String RESET_PASSWORD_TEXT = "Press the following link to reset your password: ";
        public static final String EMAIL_VERIFICATION_SUBJECT = "SpiceSwap Email Verification";
        public static final String EMAIL_VERIFICATION_TEXT = "Click the following link to verify your email: ";
        public static final String RESET_PASSWORD_URL = "https://spiceswap.vercel.app/email-verify-forgot-password?token=";
        public static final String EMAIL_VERIFICATION_URL = "https://spiceswap.vercel.app/email-verify-register?token=";

    }

    public static final class ValidationMessage {
        public static final String USER_EXISTS = "Username is already in use";
        public static final String EMAIL_EXISTS = "Email is already in use";
        public static final String PASSWORD_MIN_CHAR = "Password must be at least 6 characters";
        public static final String NOT_BLANK = "Cannot be empty";
        public static final String INVALID_EMAIL = "Invalid email format";
        public static final String CANT_CONTAIN_SPACES = "Usernames cannot contain spaces";
        public static final String INVALID_BASE64_IMG = "Invalid image format";
        public static final String INVALID_VALUE = "The value is too large or the value cannot be decimal";
        public static final String MAX_INGREDIENTS = "The number of ingredients should not exceed 50";
        public static final String MAX_STEPS = "The number of steps should not exceed 40";
        public static final String UNKNOWN_ENUM = "Unknown enum value: ";
        public static final String INGREDIENT_EXISTS = "The ingredient already exists";
        public static final String MAX_RATING = "Invalid rating value";
        public static final String MIN_RATING = "Invalid rating value";
    }
}