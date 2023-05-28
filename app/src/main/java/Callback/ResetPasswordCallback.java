package Callback;

public interface ResetPasswordCallback {
    void onResetPasswordSuccess(String password);
    void onResetPasswordFailure();
}