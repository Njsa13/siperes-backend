package com.siperes.siperes.common.util;

public class Constants {
    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }

    public static final class TestAuth {
        public static final String TEST_PATS = BASE_URL + "/test";
        public static final String TEST_USER_PATS = BASE_URL + "/test/user";
        public static final String TEST_ADMIN_PATS = BASE_URL + "/test/admin";
    }

    public static final class CommonPats {
            public static final String[] SECURE_LIST_PATS = {
            };
    }

    public static final class ErrorMessage {
        public static final String USERNAME_NOT_FOUND = "Username tidak ditamukan";
        public static final String EMAIL_NOT_FOUND = "Email tidak ditamukan";
        public static final String WRONG_PASSWORD = "Password salah";
        public static final String USER_ALREADY_VERIFIED = "User sudah diverifikasi";
        public static final String USER_NOT_VERIFIED = "User belum diverifikasi";
        public static final String TOKEN_EXPIRED = "Token kedaluwarsa";
        public static final String TOKEN_NOT_FOUND = "Token tidak ditemukan";
        public static final String TOKEN_TYPE_INVALID = "Tipe token tidak valid";
        public static final String INVALID_TOKEN = "Token tidak valid";
        public static final String REGISTER_TOKEN_NOT_FOUND = "Token register tidak ditemukan";
        public static final String SEND_EMAIL_FAILED = "Gagal mengirim email";
        public static final String TWO_MIN_COOL_DOWN = "Tunggu 2 menit untuk kirim ulang";
        public static final String INACTIVE_USER = "Akun user telah dinonaktifkan";
        public static final String REGISTER_FAILED = "Gagal daftar akun";
        public static final String LOGIN_FAILED = "Login gagal";
        public static final String REFRESH_TOKEN_FAILED = "Gagal refresh token";
        public static final String VERIFY_EMAIL_FAILED = "Gagal menverifikasi email";
        public static final String EXTRACT_JWT_FAILED = "Gagal mengekstrak jwt";
        public static final String CHECK_FIELD_EXISTS_FAILED = "Gagal mengecek keberadaan value dari field terkait";
    }

    public static final class EmailMessage {
        public static final String RESET_PASSWORD_SUBJECT = "Reset Password Siperes";
        public static final String RESET_PASSWORD_TEXT = "Tekan link berikut untuk reset password: ";
        public static final String EMAIL_VERIFICATION_SUBJECT = "Verifikasi Email Siperes";
        public static final String EMAIL_VERIFICATION_TEXT = "Tekan link berikut untuk verifikasi email: ";
        public static final String RESET_PASSWORD_URL = "http://frontend.domain/email-verify-forgot-password?token=";
        public static final String EMAIL_VERIFICATION_URL = "http://frontend.domain/email-verify-register?token=";

    }

    public static final class ValidationMessage {
        public static final String USER_EXISTS = "Username sudah digunakan";
        public static final String EMAIL_EXISTS = "Email sudah digunakan";
        public static final String PASSWORD_MIN_CHAR = "Password minimal 6 karakter";
        public static final String NOT_BLANK = "Tidak boleh kosong";
        public static final String INVALID_EMAIL = "Format email tidak valid";
    }
}