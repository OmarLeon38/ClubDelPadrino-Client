package com.upao.cliente.clubdelpadrino_client.communication;

import android.content.Intent;

public interface Communication {
    void showDetails(Intent i);

    void exportInvoice(int idClient, int idOrden, String fileName);
}
