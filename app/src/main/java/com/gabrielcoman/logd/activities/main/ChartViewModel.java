package com.gabrielcoman.logd.activities.main;

import java.util.List;

class ChartViewModel {

    private String[] labels;
    private float[] values;

    ChartViewModel(List<ResponseGroupViewModel> models) {

        if (models.size() > 0) {
            int length = models.size() >= 7 ? 7 : models.size();
            labels = new String[length];
            values = new float[length];
            for (int i = length - 1; i >= 0; i--) {
                int t = (length - 1) - i;
                labels[t] = models.get(i).getDayAndMonth();
                values[t] = (float) models.get(i).getAverage();
            }
        }
    }

    String[] getLabels() {
        return labels;
    }

    float[] getValues() {
        return values;
    }

    boolean isValid () {
        return labels != null && values != null && labels.length > 0 && values.length > 0;
    }
}
