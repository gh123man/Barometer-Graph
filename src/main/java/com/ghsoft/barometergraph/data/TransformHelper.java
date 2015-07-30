package com.ghsoft.barometergraph.data;

import com.ghsoft.barometergraph.views.BarometerDataGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    public static final BarometerDataGraph.TransformFunction TO_MPA = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.0001);
        }
        @Override
        public String getUnit() {
            return "MPa";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_KPA = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.1);
        }
        @Override
        public String getUnit() {
            return "KPa";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_BAR = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.001);
        }
        @Override
        public String getUnit() {
            return "bar";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_ATM = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.000986923266716);
        }
        @Override
        public String getUnit() {
            return "atm";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_CMHG = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.07500637554192);
        }
        @Override
        public String getUnit() {
            return "cmHg";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_MMHG = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.7500637554192);
        }
        @Override
        public String getUnit() {
            return "mmHg";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_INHG = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.02961339710085);
        }
        @Override
        public String getUnit() {
            return "inHg";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_CMAQ = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 1.019744288922);
        }
        @Override
        public String getUnit() {
            return "cmAq";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_MMAQ = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 10.19744288922);
        }
        @Override
        public String getUnit() {
            return "mmAq";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_AT = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.001019716212978);
        }
        @Override
        public String getUnit() {
            return "at";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_MSW = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.009931170631574);
        }
        @Override
        public String getUnit() {
            return "msw";
        }
    };
    public static final BarometerDataGraph.TransformFunction TO_FSW = new BarometerDataGraph.TransformFunction() {
        @Override
        public float transform(float val) {
            return (float) (val * 0.03263381732743);
        }
        @Override
        public String getUnit() {
            return "fsw";
        }
    };

    static {
        Map<String, BarometerDataGraph.TransformFunction> map = new HashMap<>();
        map.put(TO_HPA.getUnit(), TO_HPA);
        map.put(TO_PSI.getUnit(), TO_PSI);
        map.put(TO_MPA.getUnit(), TO_MPA);
        map.put(TO_KPA.getUnit(), TO_KPA);
        map.put(TO_BAR.getUnit(), TO_BAR);
        map.put(TO_ATM.getUnit(), TO_ATM);
        map.put(TO_CMHG.getUnit(), TO_CMHG);
        map.put(TO_MMHG.getUnit(), TO_MMHG);
        map.put(TO_INHG.getUnit(), TO_INHG);
        map.put(TO_CMAQ.getUnit(), TO_CMAQ);
        map.put(TO_MMAQ.getUnit(), TO_MMAQ);
        map.put(TO_AT.getUnit(), TO_AT);
        map.put(TO_MSW.getUnit(), TO_MSW);
        map.put(TO_FSW.getUnit(), TO_FSW);
        FUNCTIONS = Collections.unmodifiableMap(map);

        List<String> units = new ArrayList<>();
        for (String key : map.keySet()) {
            units.add(key);
        }
        UNITS = units.toArray(new String[units.size()]);

    }

    public static BarometerDataGraph.TransformFunction fromUnit(String unit) {
        return FUNCTIONS.get(unit);
    }
}
