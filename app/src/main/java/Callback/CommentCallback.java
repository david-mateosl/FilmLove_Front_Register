package Callback;

public interface CommentCallback {
    void onCommentSuccess(String message);
    void onCommentFailure(String error);
}
