package mx.edu.utng.orders;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.edu.utng.orders.adapters.MessagesAdapter;

import mx.edu.utng.orders.sqlite.DBOperations;
import mx.edu.utng.orders.model.Messages;

/**
 * Created by jntreyes on 23/02/17.
 */

public class MessageActivity extends AppCompatActivity {
    private EditText etMessage;
    private EditText etIp;
    private Button btSaveMessage;
    private DBOperations operations;
    private Messages message = new Messages();
    private RecyclerView rvMessages;
    private List<Messages> messages=new ArrayList<Messages>();
    private MessagesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        //inicia instancia
        operations=DBOperations.getDBOperations(getApplicationContext());
        message.setId("");


        initComponents();
    }
    private void initComponents(){
        rvMessages=(RecyclerView)findViewById(R.id.rv_message_list);
        rvMessages.setHasFixedSize(true);
        LinearLayoutManager layoutManeger=new LinearLayoutManager(this);
        rvMessages.setLayoutManager(layoutManeger);
        //
        getMessages();
        adapter=new MessagesAdapter(messages);

        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.bt_delete_message:
                        operations.deleteMessages(messages.get(rvMessages.getChildPosition((View)v.getParent().getParent())).getId());
                        getMessages();
				clearData();
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.bt_edit_message:

                        Toast.makeText(getApplicationContext(),"Editar",Toast.LENGTH_SHORT).show();
                        Cursor c = operations.getMessagesById(messages.get(
                                rvMessages.getChildPosition(
                                        (View)v.getParent().getParent())).getId());
                        if (c!=null){
                            if (c.moveToFirst()){
                                message = new Messages(c.getString(1),c.getString(2),c.getString(3));
                            }
                            etMessage.setText(message.getMessage());
                            etIp.setText(String.valueOf(message.getIp()));
                        }else{
                            Toast.makeText(getApplicationContext(),"Registro no encontrado",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
        rvMessages.setAdapter(adapter);

        etMessage=(EditText)findViewById(R.id.et_message);
        etIp=(EditText)findViewById(R.id.et_ip);

        btSaveMessage=(Button)findViewById(R.id.bt_save_message);

        btSaveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!message.getId().equals("")){
                    message.setMessage(etMessage.getText().toString());
                    message.setIp(etIp.getText().toString());
                    operations.updateMessages(message);

                }else {
                    message = new Messages("", etMessage.getText().toString(),etIp.getText().toString());
                    operations.insertMessages(message);
                }
                getMessages();
                clearData();
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void getMessages(){
        Cursor c =operations.getMessages();
        messages.clear();
        if(c!=null){
            while (c.moveToNext()){
                messages.add(new Messages(c.getString(1),c.getString(2),c.getString(3)));
            }
        }

    }

    private void clearData(){
        etMessage.setText("");
        etIp.setText("");
        message=null;
        message= new Messages();
        message.setId("");
    }
}

