package Callback;

import java.util.List;

import Modelo.Production;

public interface VotedProductionsCallback {
    void onVotedProductionsSuccess(List<Production> votedProductions);
    void onVotedProductionsFailure();
}
