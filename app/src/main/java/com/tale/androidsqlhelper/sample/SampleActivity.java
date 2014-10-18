package com.tale.androidsqlhelper.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.androidsqlhelper.AndroidSqlHelper;
import com.tale.androidsqlhelper.ITable;
import com.tale.androidsqlhelper.sample.model.PrimaryType;

import java.util.List;

/**
 * Created by giang on 18/10/2014.
 */
public class SampleActivity extends ActionBarActivity {


    EditText mEtInteger;
    TextView mTvIntegerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mEtInteger = (EditText) findViewById(R.id.etInteger);
        mTvIntegerValue = (TextView) findViewById(R.id.tvIntegerValue);

        findViewById(R.id.btInsertInteger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertClick();
            }
        });


        findViewById(R.id.btLoadInteger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntClick();
            }
        });

    }

    public void insertClick() {
        final String intValue = mEtInteger.getText().toString();
        if (TextUtils.isEmpty(intValue) || !TextUtils.isDigitsOnly(intValue)) {
            return;
        }

        PrimaryType primaryType = new PrimaryType();
        primaryType.intValue = Integer.parseInt(intValue);

        long id = AndroidSqlHelper.insert(primaryType);

        if (id > 0) {
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        }
    }

    public void getIntClick() {
        List<? extends ITable> tables = AndroidSqlHelper.quickQueryObjects(PrimaryType.class, null, null);
        if (tables != null && tables.size() > 0) {
            PrimaryType primaryType = (PrimaryType) tables.get(0);
            mTvIntegerValue.setText(String.valueOf(primaryType.intValue));
        }
    }
}
