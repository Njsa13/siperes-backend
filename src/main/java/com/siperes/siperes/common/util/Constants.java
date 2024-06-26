package com.siperes.siperes.common.util;

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
        public static final String USER_NOT_FOUND = "User tidak ditemukan";
        public static final String USERNAME_NOT_FOUND = "Username tidak ditamukan";
        public static final String EMAIL_NOT_FOUND = "Email tidak ditamukan";
        public static final String WRONG_PASSWORD = "Password salah";
        public static final String EMAIL_ALREADY_VERIFIED = "Email sudah diverifikasi";
        public static final String EMAIL_NOT_VERIFIED = "Email belum diverifikasi";
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
        public static final String LOGOUT_FAILED = "Gagal logout";
        public static final String CHECK_FIELD_EXISTS_FAILED = "Gagal mengecek keberadaan value dari field terkait";
        public static final String FAILED_ADD_ADMIN = "Gagal menambahkan data admin";
        public static final String FAILED_UPLOAD_IMG = "Gagal mengunggah gambar";
        public static final String FAILED_UPDATE_IMG = "Gagal mengupdate gambar";
        public static final String FAILED_DELETE_IMG = "Gagal Menghapus gambar";
        public static final String FAILED_ADD_RECIPE = "Gagal membuat resep baru";
        public static final String FAILED_UPDATE_RECIPE = "Gagal memperbarui resep";
        public static final String FAILED_DELETE_RECIPE = "Gagal menghapus resep";
        public static final String FAILED_GET_UPDATE_RECIPE_DETAIL = "Gagal memuat detail update resep";
        public static final String FAILED_GET_SETTING_RECIPE_DETAIL = "Gagal memuat detail pengaturan resep";
        public static final String FAILED_SET_RECIPE = "Gagal mengubah pengaturan resep";
        public static final String FAILED_GET_ALL_MY_RECIPE = "Gagal memuat semua resep saya";
        public static final String FAILED_GET_MY_RECIPE_DETAIL = "Gagal memuat detail resep saya";
        public static final String FAILED_GET_ALL_BOOKMARKED_RECIPE = "Gagal memuat semua resep yang di bookmark";
        public static final String FAILED_CREATE_BOOKMARK = "Gagal melakukan bookmark pada resep";
        public static final String FAILED_DELETE_BOOKMARK = "Gagal menghapus bookmark pada resep";
        public static final String FAILED_GET_RECIPE_HISTORIES = "Gagal memuat riwayat resep";
        public static final String FAILED_GET_RECIPE_HISTORY_DETAIL = "Gagal memuat detail riwayat resep";
        public static final String FAILED_GET_RECIPE_LIST = "Gagal memuat daftar resep";
        public static final String FAILED_GET_RECIPE_DETAIL = "Gagal memuat detail resep";
        public static final String FAILED_CREATE_INGREDIENT = "Gagal menambahkan bahan";
        public static final String FAILED_UPDATE_INGREDIENT = "Gagal memperbarui bahan";
        public static final String FAILED_DELETE_INGREDIENT = "Gagal menghapus bahan";
        public static final String FAILED_GET_INGREDIENT_LIST = "Gagal memuat daftar bahan";
        public static final String FAILED_CHANGE_RECIPE_STATUS = "Gagal mengubah status resep";
        public static final String FAILED_GET_RECIPE_INFORMATION = "Gagal mengubah status resep";
        public static final String RECIPE_NOT_FOUND = "Resep tidak ditemukan";
        public static final String RECIPE_HISTORY_NOT_FOUND = "Riwayat resep tidak ditemukan";
        public static final String RECIPE_HISTORY_DETAIL_NOT_FOUND = "Detail riwayat resep tidak ditemukan";
        public static final String INGREDIENT_DETAIL_NOT_FOUND = "Tidak ditemukan bahan dengan slug: ";
        public static final String STEP_NOT_FOUND = "Tidak ditemukan langkah dengan slug: ";
        public static final String INGREDIENT_NOT_FOUND = "Bahan tidak ditemukan";
        public static final String IMAGE_PROFILE_NOT_FOUND = "Foto profile tidak ditemukan";
        public static final String ORIGINAL_RECIPE_NOT_FOUND = "Resep salinan tidak ditemukan atau resep tidak memiliki resep asal (Merupakah resep original atau resep asal telah dihapus)";
        public static final String MODIFICATION_REQUEST_NOT_FOUND = "penmintaan modifikasi tidak ditemukan";
        public static final String RECIPE_REVIEW_NOT_FOUND = "Review tidak ditemukan";
        public static final String INPUT_CANNOT_NULL = "Input tidak boleh null atau empty";
        public static final String FAILED_CHECK_FIELD_EXISTS= "Gagal memeriksa keberadaan field";
        public static final String FILED_GET_TWO_WORDS = "Gagal mengambil 2 huruf pertama";
        public static final String FAILED_GET_USER_DETAIL = "Gagal memuat data user";
        public static final String FAILED_UPDATE_USER_DETAIL = "Gagal memperbarui data user";
        public static final String FAILED_UPDATE_PROFILE_IMAGE = "Gagal memperbarui foto profile";
        public static final String FAILED_DELETE_PROFILE_IMAGE = "Gagal menghapus foto profile";
        public static final String FAILED_CREATE_MODIFICATION_REQUEST = "Gagal membuat permintaan modifikasi";
        public static final String FAILED_GET_MODIFICATION_REQUEST = "Gagal memuat permintaan modifikasi";
        public static final String FAILED_GET_RECIPE_COPY = "Gagal memuat resep copy untuk permintaan modifikasi";
        public static final String FAILED_TO_APPROVE_REQUEST = "Gagal menyetujui permintaan modifikasi";
        public static final String FAILED_TO_REJECT_REQUEST = "Gagal menolak permintaan modifikasi";
        public static final String INVALID_CURRENT_PASSWORD = "Password sekarang salah";
        public static final String INVALID_CONFIRM_PASSWORD = "Password baru dan konfirmasi password tidak cocok";
        public static final String FAILED_CHANGE_PASSWORD = "Gagal mengganti password";
        public static final String FAILED_RESET_PASSWORD = "Gagal mereset password";
        public static final String FAILED_DOWNLOAD_IMG = "Gagal Mengunduh gambar";
        public static final String FAILED_COPY_RECIPE = "Gagal menyalin resep";
        public static final String FAILED_GET_OTHER_USER_PROFILE = "Gagal memuat datas user";
        public static final String FAILED_GET_DATA_USER = "Gagal memuat data user";
        public static final String FAILED_CHANGE_USER_STATUS = "Gagal mengganti status user";
        public static final String FAILED_GET_USER_INFO = "Gagal memuat informasi tentang user";
        public static final String FAILED_WRITE_REVIEW = "Gagal menulis ulasan resep";
        public static final String FAILED_GET_REVIEW = "Gagal memuat ulasan resep";
        public static final String FAILED_DELETE_REVIEW = "Gagal menghapus ulasan resep";
        public static final String INVALID_SLUG = "Slug tidak valid";
        public static final String CONFLICT_REVIEW = "Ulasan belum diubah";
        public static final String MAX_COPY_RECIPE = "Tidak bisa menyalin resep melebihi 3";
        public static final String FORBIDDEN_BOOKMARK = "Tidak dapat melakukan bookmark pada resep milik sendiri";
        public static final String FORBIDDEN_EXIST_BOOKMARK = "Resep sudah di bookmark";
        public static final String FORBIDDEN_NOT_EXIST_BOOKMARK = "Bookmark sudah dihapus";
        public static final String FORBIDDEN_COPY = "Tidak dapat menyalin resep milik sendiri";
        public static final String FORBIDDEN_ACCESS_PRIVATE = "Tidak bisa mengakses resep private";
        public static final String FORBIDDEN_APPROVE_REQUEST = "Tidak bisa menyetujui atau menolak permintaan modifikasi pada resep original";
        public static final String FORBIDDEN_PRIVATE_REQUEST = "Resep tujuan sedang di private";
        public static final String FORBIDDEN_EMPTY_HISTORIES = "Belum ada pembaruan pada resep salinan yang ingin anda ajukan";
        public static final String FORBIDDEN_REVIEW = "Tidak dapat mengulas resep milik sendiri";
        public static final String FORBIDDEN_NOT_EXIST_REVIEW = "Ulasan sudah dihapus";

        public static final String EXISTS_REQUEST = "Sudah melakukan permintaan modifikasi sebelumnya dan masih berstatus menunggu";
    }

    public static final class EmailMessage {
        public static final String RESET_PASSWORD_SUBJECT = "Reset Password Siperes";
        public static final String RESET_PASSWORD_TEXT = "Tekan link berikut untuk reset password: ";
        public static final String EMAIL_VERIFICATION_SUBJECT = "Verifikasi Email Siperes";
        public static final String EMAIL_VERIFICATION_TEXT = "Tekan link berikut untuk verifikasi email: ";
        public static final String RESET_PASSWORD_URL = "https://spiceswap.vercel.app/email-verify-forgot-password?token=";
        public static final String EMAIL_VERIFICATION_URL = "https://spiceswap.vercel.app/email-verify-register?token=";

    }

    public static final class ValidationMessage {
        public static final String USER_EXISTS = "Username sudah digunakan";
        public static final String EMAIL_EXISTS = "Email sudah digunakan";
        public static final String PASSWORD_MIN_CHAR = "Password minimal 6 karakter";
        public static final String NOT_BLANK = "Tidak boleh kosong";
        public static final String INVALID_EMAIL = "Format email tidak valid";
        public static final String CANT_CONTAIN_SPACES = "Username tidak boleh mengandung spasi";
        public static final String INVALID_BASE64_IMG = "Format gambar tidak valid";
        public static final String INVALID_VALUE = "Nilai terlalu besar atau nilai tidak boleh desimal";
        public static final String MAX_INGREDIENTS = "Jumlah bahan tidak boleh melebihi 50";
        public static final String MAX_STEPS = "Jumlah langkah tidak boleh melebihi 40";
        public static final String UNKNOWN_ENUM = "Nilai enum tidak diketahuai: ";
        public static final String INGREDIENT_EXISTS = "Bahan sudah ada";
        public static final String MAX_RATING = "Angka rating maksimal 5";
        public static final String MIN_RATING = "Angka rating minimal 1";
    }
}