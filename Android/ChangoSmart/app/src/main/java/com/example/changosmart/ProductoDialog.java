package com.example.changosmart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductoDialog extends AppCompatDialogFragment {
    private EditText etCantidad;
    private ProductDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View miAlertDialog = LayoutInflater.from(getActivity()).inflate(R.layout.alertdialog_producto,null);
        etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);




        Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

        buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0){
                    Toast.makeText(getActivity(), "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                }else{
                    int canditdadAComprar = Integer.valueOf(etCantidad.getText().toString());
                    listener.aplicarCantidad(canditdadAComprar);
                }
                dismiss();
            }
        });

        Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoCancelar);

        buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.aplicarCantidad(0);
                dismiss();
            }
        });

        builder.setView(miAlertDialog);
        return builder.create();
    }

    public interface ProductDialogListener{
        void aplicarCantidad(int cantidad);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ProductDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + " tiene que implementar ProductDialogListener");
        }
    }
}
