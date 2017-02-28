package com.gabrielcoman.logd.rxdatasource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RxRow {

    /**
     * The holder view will contain the row to be loaded and further customised
     */
    private View holderView = null;

    /**
     * The associated row Id
     */
    private int rowId = 0;

    /**
     * Custom constructor
     * @param context the current context you want to load the row in
     * @param rowId the resource Id of the row you want to load (e.g. R.layout.my_custom_row_
     * @param parent the view's parent (usually the ListView you want to load the row in)
     */
    RxRow (Context context, int rowId, ViewGroup parent) {

        // assign the row Id so we don't forget it
        this.rowId = rowId;

        // and create the new holder view, if null
        if (holderView == null) {
            holderView = LayoutInflater.from(context).inflate(rowId, parent, false);
        }
    }

    /**
     * Getter for the holder view; This is the main way a user will be able to access the holder
     * @return the current holder view
     */
    public View getHolderView () {
        return holderView;
    }

    /**
     * Setter for the holder view
     * @param v the new view you want to set
     */
    void setHolderView(View v) {
        holderView = v;
    }

    /**
     * Getter for the Row Id
     * @return int of the row Id
     */
    public int getRowId() {
        return rowId;
    }
}
