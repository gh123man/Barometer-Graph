package com.ghsoft.barometergraph.data;

import com.ghsoft.barometergraph.views.BarometerDataGraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brian on 7/26/15.
 */
public class TransformHelper {

    public static final Map<String, BarometerDataGraph.TransformFunction> FUNCTIONS;
    public static final String[] UNITS;

    public static final BarometerDataGraph.TransformFunction TO_HPA = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return val;
        }
        @Override
        public String getUnit() {
            return "hPa";
        }
    };

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

    static {
        Map<String, BarometerDataGraph.TransformFunction> map = new HashMap<>();
        map.put(TO_HPA.getUnit(), TO_HPA);
        map. put(TO_PSI.getUnit(), TO_PSI);
        FUNCTIONS = Collections.unmodifiableMap(map);

        UNITS = new String[] {
            TO_PSI.getUnit(),
            TO_HPA.getUnit()
        };

    }

    public static BarometerDataGraph.TransformFunction fromUnit(String unit) {
        return FUNCTIONS.get(unit);
    }
}
