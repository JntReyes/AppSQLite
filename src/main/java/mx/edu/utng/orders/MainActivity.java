package mx.edu.utng.orders;

import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.util.Calendar;
import mx.edu.utng.orders.model.Customer;
import mx.edu.utng.orders.model.MethodPayment;
import mx.edu.utng.orders.model.OrderDetail;
import mx.edu.utng.orders.model.OrderHeader;
import mx.edu.utng.orders.model.Product;
import mx.edu.utng.orders.sqlite.DBOperations;
import mx.edu.utng.orders.sqlite.OrderDB;

public class MainActivity extends AppCompatActivity {
    DBOperations data;
    public class DataTaskTest extends AsyncTask <Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            //Inserciones
            String currentDate= Calendar.getInstance().getTime().toString();
            try{
                data.getDb().beginTransaction();
                //InsertCustomer
                String customer1=data.insertCustomer(new Customer(null,"Veronica","Del Topo","402558"));
                String customer2=data.insertCustomer(new Customer(null,"Carlos","Villagran","458852"));
                //Method
                String methodPayment1=data.insertMethodPayment(new MethodPayment(null,"Efectivo"));
                String methodPayment2=data.insertMethodPayment(new MethodPayment(null,"Credito"));
                //Products
                String product1=data.insertProduct(new Product(null,"Leche",4,120));
                String product2=data.insertProduct(new Product(null,"Galletas",5,45));
                String product3=data.insertProduct(new Product(null,"Queso",149.5f,45));
                String product4=data.insertProduct(new Product(null,"Papitas",189.5f,12));
                //Orders Header
                String order1=data.insertOrderHeader(new OrderHeader(null,customer1,methodPayment1,currentDate));
                String order2=data.insertOrderHeader(new OrderHeader(null,customer2,methodPayment2,currentDate));

                //Orders DETAIL
                data.insertOrderDetail(new OrderDetail(order1,1,product1,5,2));
                data.insertOrderDetail(new OrderDetail(order1,2,product1,15,5));
                data.insertOrderDetail(new OrderDetail(order2,1,product1,35,26));
                data.insertOrderDetail(new OrderDetail(order2,2,product1,45,12));
                //Eliminar el pedido
                data.deleteOrderHeader(order1);
                //Actualización
                data.updateCustomer(new Customer(customer2,"Jeanette ","Reyes","4181113220"));
                data.getDb().setTransactionSuccessful();

            }finally {
                data.getDb().endTransaction();
            }
            //Querys
            Log.d("Customers","Customers");
            DatabaseUtils.dumpCursor(data.getCustomers());
            Log.d("Method of Payment","Method of Payments");
            DatabaseUtils.dumpCursor(data.getMethodsPayment());
            Log.d("Products","Products");
            DatabaseUtils.dumpCursor(data.getProducts());
            Log.d("Order Headers","Order Headers");
            DatabaseUtils.dumpCursor(data.getOrderDetails());
            Log.d("Order Details","Order Details");
            DatabaseUtils.dumpCursor(data.getOrderDetails());

            return null;
        }
    }
            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getApplicationContext().deleteDatabase(OrderDB.DATABASE_NAME);
                data=DBOperations.getDBOperations(getApplicationContext());
                new DataTaskTest().execute();

    }
}
