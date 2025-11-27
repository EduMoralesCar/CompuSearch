package com.universidad.compusearch.util;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class ValidationText {

    public static boolean esNombreParecido(String nombre1, String nombre2) {
        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();

        double similitud = similarity.apply(nombre1.toLowerCase(), nombre2.toLowerCase());

        return similitud >= 0.90;
    }
}
