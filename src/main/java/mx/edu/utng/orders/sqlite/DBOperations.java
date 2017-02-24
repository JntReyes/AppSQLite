package mx.edu.utng.orders.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Message;

import mx.edu.utng.orders.model.Customer;
import mx.edu.utng.orders.model.MethodPayment;
import mx.edu.utng.orders.model.OrderDetail;
import mx.edu.utng.orders.model.OrderHeader;
import mx.edu.utng.orders.model.Product;
import mx.edu.utng.orders.model.Messages;

/**
 * Created by Jeanette on 7/02/17.
 */

public final class DBOperations {
    private static OrderDB db;
    private static DBOperations operations;

    private static final String JOIN_ORDER_CUSTOMER_METHOD =
            "order_header " +
            "INNER JOIN customer " +
            "ON order_header.id_customer = customer.id " +
            "INNER JOIN method_payment " +
            "ON order_header.id_method_payment = method_payment.id";
    private final String[] orderProj = new String[]{
            OrderDB.Tables.ORDER_HEADER + "."
                    + OrderContract.OrderHeaders.ID,
            OrderContract.OrderHeaders.DATE,
            OrderContract.Customers.FIRSTNAME,
            OrderContract.Customers.LASTNAME,
            OrderContract.MethodsPayment.NAME};

    private DBOperations(){

    }

    public static DBOperations getDBOperations(
            Context context){
        if(operations==null) {
            operations = new DBOperations();
        }
        if(db==null){
            db = new OrderDB(context);
        }
        return operations;
    }
    //Operations of  Orders
    public Cursor getOrders(){
        SQLiteDatabase database = db.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);

        return  builder.query(database, orderProj,
                null, null, null, null, null);
    }

    public Cursor getOrderById(String id){
        SQLiteDatabase database = db.getWritableDatabase();
        String selection = String.format("%s=?",
                OrderContract.OrderHeaders.ID);
        String[] selectionArgs = {id};
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);
        String[] projection = {
                OrderDB.Tables.ORDER_HEADER+"."
                        + OrderContract.OrderHeaders.ID,
                OrderContract.OrderHeaders.DATE,
                OrderContract.Customers.FIRSTNAME,
                OrderContract.Customers.LASTNAME,
                OrderContract.MethodsPayment.NAME
        };
        return builder.query(database, projection, selection,
                selectionArgs, null, null, null);
    }

    public String insertOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        String idOrderHeader =
                OrderContract.OrderHeaders.generateIdOrderHeader();
        values.put(OrderContract.OrderHeaders.ID, idOrderHeader);
        values.put(OrderContract.OrderHeaders.DATE,
                orderHeader.getDate());
        values.put(OrderContract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(OrderContract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());

        database.insertOrThrow(OrderDB.Tables.ORDER_HEADER,
                null, values);
        return idOrderHeader;
    }

    public boolean updateOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderHeaders.DATE,
                orderHeader.getDate());
        values.put(OrderContract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(OrderContract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());

        String whereClause = String.format("%s=?", OrderContract.OrderHeaders.ID);
        String[] whereArgs = {orderHeader.getIdOrderHeader()};

        int result = database.update(OrderDB.Tables.ORDER_HEADER, values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderHeader(String idOrderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                OrderContract.OrderHeaders.ID + "=?";
        String[] whereArgs = {idOrderHeader};

        int result =  database.delete(
                OrderDB.Tables.ORDER_HEADER, whereClause, whereArgs);
        return result > 0;
    }
    //Operations of  OrderDetails
    public Cursor getOrderDetails(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                OrderDB.Tables.ORDER_DETAIL);
        return  database.rawQuery(sql, null);
    }

    public Cursor getOrderDetailById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                " WHERE %s=?", OrderDB.Tables.ORDER_DETAIL,
                OrderContract.OrderHeaders.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderDetails.ID,
                orderDetail.getIdOrderHeader());
        values.put(OrderContract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(OrderContract.OrderDetails.ID_PRODUCT,
                orderDetail.getIdProduct());
        values.put(OrderContract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(OrderContract.OrderDetails.PRICE,
                orderDetail.getPrice());

        database.insertOrThrow(OrderDB.Tables.ORDER_DETAIL,
                null, values);
        return String.format("%s#%d",
                orderDetail.getIdOrderHeader(),
                orderDetail.getSequence());
    }

    public boolean updateOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(OrderContract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(OrderContract.OrderDetails.PRICE,
                orderDetail.getPrice());

        String whereClause = String.format("%s=? AND %s=?",
                OrderContract.OrderDetails.ID,
                OrderContract.OrderDetails.SEQUENCE);
        String[] whereArgs = {orderDetail.getIdOrderHeader(),
        String.valueOf(orderDetail.getSequence())};

        int result = database.update(OrderDB.Tables.ORDER_DETAIL,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderDetail(String idOrderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                OrderContract.OrderHeaders.ID + "=?";
        String[] whereArgs = {idOrderDetail};

        int result =  database.delete(
                OrderDB.Tables.ORDER_DETAIL, whereClause, whereArgs);
        return result > 0;
    }
    //Operations of  Products
    public Cursor getProducts(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                OrderDB.Tables.PRODUCT);
        return  database.rawQuery(sql, null);
    }

    public Cursor getProductById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                        " WHERE %s=?",
                OrderDB.Tables.PRODUCT,
                OrderContract.Products.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertProduct(Product product){
        SQLiteDatabase database = db.getWritableDatabase();
        String idProduct = OrderContract.Products.generateIdProduct();
        ContentValues values = new ContentValues();
        values.put(OrderContract.Products.ID, idProduct);
        values.put(OrderContract.Products.NAME, product.getName());
        values.put(OrderContract.Products.PRICE, product.getPrice());
        values.put(OrderContract.Products.STOCK, product.getStock());
        database.insertOrThrow(OrderDB.Tables.PRODUCT, null, values);
        return idProduct;
    }

    public boolean updateProduct(Product product){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderContract.Products.NAME, product.getName());
        values.put(OrderContract.Products.PRICE, product.getPrice());
        values.put(OrderContract.Products.STOCK, product.getStock());
        String whereClause = String.format("%s=?", OrderContract.Products.ID);
        String[] whereArgs = {product.getIdProduct()};
        int result = database.update(OrderDB.Tables.PRODUCT,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteProduct(String idProduct){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = OrderContract.Products.ID + "=?";
        String[] whereArgs = {idProduct};
        int result =  database.delete(OrderDB.Tables.PRODUCT,
                whereClause, whereArgs);
        return result > 0;
    }
    // Operations Customers
    public Cursor getCustomers() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                OrderDB.Tables.CUSTOMER);
        return database.rawQuery(sql, null);
    }
    public Cursor getCustomersById(String idCustomer) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                OrderDB.Tables.CUSTOMER,
                OrderContract.Customers.ID);
        String[] whereArgs ={idCustomer};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String idCustomer = OrderContract.Customers.generateIdCustomer();
        ContentValues values = new ContentValues();
        values.put(OrderContract.Customers.ID, idCustomer);
        values.put(OrderContract.Customers.FIRSTNAME, customer.getFirstname());
        values.put(OrderContract.Customers.LASTNAME, customer.getLastname());
        values.put(OrderContract.Customers.PHONE, customer.getPhone());

        return database.insertOrThrow(OrderDB.Tables.CUSTOMER,
                null, values) > 0 ? idCustomer : null;
    }

    public boolean updateCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderContract.Customers.FIRSTNAME,
                customer.getFirstname());
        values.put(OrderContract.Customers.LASTNAME,
                customer.getLastname());
        values.put(OrderContract.Customers.PHONE,
                customer.getPhone());
        String whereClause = String.format("%s=?",
                OrderContract.Customers.ID);
        final String[] whereArgs = {customer.getIdCustomer()};
        int result = database.update(OrderDB.Tables.CUSTOMER,
                values, whereClause, whereArgs);
        return result > 0;
    }
    public boolean deleteCustomer(String idCustomer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?",
                OrderContract.Customers.ID);
        final String[] whereArgs = {idCustomer};
        int result = database.delete(OrderDB.Tables.CUSTOMER, whereClause, whereArgs);
        return result > 0;
    }

    // Operation Method of payment
    public Cursor getMethodsPayment() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                OrderDB.Tables.METHOD_PAYMENT);
        return database.rawQuery(sql, null);
    }

    public Cursor getMethodsPaymentById(String idMethodPayment) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                OrderDB.Tables.METHOD_PAYMENT,
                OrderContract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        return database.rawQuery(sql, null);
    }
    public String insertMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();

        String idMethodPayment = OrderContract.MethodsPayment.generateIdMethodPayment();

        ContentValues values = new ContentValues();
        values.put(OrderContract.MethodsPayment.ID, idMethodPayment);
        values.put(OrderContract.MethodsPayment.NAME, methodPayment.getName());

        return database.insertOrThrow(
                OrderDB.Tables.METHOD_PAYMENT, null,
                values) > 0 ? idMethodPayment : null;
    }

    public boolean updateMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderContract.MethodsPayment.NAME,
                methodPayment.getName());
        String whereClause = String.format("%s=?",
                OrderContract.MethodsPayment.ID);
        String[] whereArgs = {methodPayment.getIdMethodPayment()};
        int result = database.update(
                OrderDB.Tables.METHOD_PAYMENT, values,
                whereClause, whereArgs);
        return result > 0;
    }

    public boolean deleteMethodPayment(String idMethodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?", OrderContract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        int result = database.delete(OrderDB.Tables.METHOD_PAYMENT, whereClause, whereArgs);
        return result > 0;
    }


    //Operations of  Messages
    public Cursor getMessages(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                OrderDB.Tables.MESSAGES);
        return  database.rawQuery(sql, null);
    }

    public Cursor getMessagesById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                        " WHERE %s=?",
                OrderDB.Tables.MESSAGES,
                OrderContract.Messages.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertMessages(Messages messages){
        SQLiteDatabase database = db.getWritableDatabase();
        String idMessage = OrderContract.Messages.generateIdMessages();
        ContentValues values = new ContentValues();
        values.put(OrderContract.Messages.ID, idMessage);
        values.put(OrderContract.Messages.MESSAGE, messages.getMessage());
        values.put(OrderContract.Messages.IP, messages.getIp());
        database.insertOrThrow(OrderDB.Tables.MESSAGES, null, values);
        return idMessage;
    }

    public boolean updateMessages(Messages messages){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderContract.Messages.MESSAGE, messages.getMessage());
        values.put(OrderContract.Messages.IP, messages.getIp());
        String whereClause = String.format("%s=?", OrderContract.Messages.ID);
        String[] whereArgs = {messages.getId()};
        int result = database.update(OrderDB.Tables.MESSAGES,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteMessages(String idMessages){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = OrderContract.Messages.ID + "=?";
        String[] whereArgs = {idMessages};
        int result =  database.delete(OrderDB.Tables.MESSAGES,
                whereClause, whereArgs);
        return result > 0;
    }



    public SQLiteDatabase getDb() {
        return db.getWritableDatabase();
    }


}
