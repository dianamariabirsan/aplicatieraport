package com.example.healthapp.service.ml;

public record DecisionFeatures(
    int varsta,
    int sexF,
    double greutate,
    double inaltime,
    int hasDiabet,
    int hasHTA,
    int adminCount,
    int hasMetformin,
    int hasInsulina,
    int isWegovy,
    int isMounjaro
) {
    public double[] toArray() {
        return new double[] {
            varsta, sexF, greutate, inaltime, hasDiabet, hasHTA,
            adminCount, hasMetformin, hasInsulina, isWegovy, isMounjaro
        };
    }
}
