package Callback;

import java.io.IOException;

import okhttp3.Response;
import retrofit2.Call;

public interface ProductionVoteCallback {
    void onVoteProductionFailure();
    void onVoteProductionSuccess(String mensaje);

}
