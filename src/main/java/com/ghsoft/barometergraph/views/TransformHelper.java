package com.ghsoft.barometergraph.views;

/**
 * Created by brian on 7/26/15.
 */
public class TransformHelper {

    public static final BarometerDataGraph.TransformFunction TO_PSI = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.014503773773);
        }
        @Override
        public String getUnit() {
            return "psi";
        }
    };
}
