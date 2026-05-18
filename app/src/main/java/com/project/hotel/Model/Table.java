package com.project.hotel.Model;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Table {
    protected TableLayout tableLayout;
    protected AppCompatActivity context;

    public Table(TableLayout tableLayout, AppCompatActivity context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public abstract void showAllFromDB();

    public void TableRoomsGenerated(TableLayout table, String[] options) {
        TableRow row = new TableRow(context);
        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.BLACK);
        for (String str : options) {
            TextView newText = new TextView(context);
            newText.setText(str);
            newText.setGravity(Gravity.CENTER);
            row.addView(newText, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        row.setPadding(5, 5, 5, 5);
        row.setBackground(border);
        table.addView(row);
    }
}
