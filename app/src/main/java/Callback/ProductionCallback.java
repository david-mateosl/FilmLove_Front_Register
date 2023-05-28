package Callback;

import java.util.List;

import Modelo.Production;

public interface ProductionCallback {
    void onProductionSuccess(Production production);

    void onProductionListLoaded(List<Production> productions);

    void onProductionFailure();

}