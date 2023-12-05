package com.upao.cliente.clubdelpadrino_client.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.upao.cliente.clubdelpadrino_client.R;

public class VerBoletaActivity extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_boleta);
        byte[] pdf = getIntent().getByteArrayExtra("pdf");
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromBytes(pdf).load();
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });
    }
}