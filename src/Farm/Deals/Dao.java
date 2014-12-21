package Farm.Deals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;
import android.util.Log;

public class Dao {

    private static final String LOG_TAG = Dao.class.getName();
    private static Connection conn;
    private static final Dao singletonObj = new Dao();
    private DBConnectionListener dbConnectionListener;
    private String serverAddress;
    private String password;
    private String username;
    private String dbName;

    private Dao() {

        /* Singleton */
    }

    /**
     * All network related task should be kept away from UI thread. Ensure that
     * you have the permission setup in the AndroidManifest.xml <uses-permission
     * android:name="android.permission.INTERNET" />
     */
    private class DBConnectionTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = DBConnectionTask.class.getName();

        @Override
        protected Void doInBackground(Void... arg) {

            try {

                Log.i(LOG_TAG, "attempting to connect to " + serverAddress);
                dbConnectionListener
                        .onConnectionStatusInfo("attempting to connect to "
                                + serverAddress);
                Log.i(LOG_TAG, "with username " + username);
                dbConnectionListener.onConnectionStatusInfo("with username "
                        + username);
                Log.i(LOG_TAG, "with password " + password);
                dbConnectionListener.onConnectionStatusInfo("with password "
                        + password);

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"
                        + serverAddress + "/" + dbName, username, password);

                Log.i(LOG_TAG, "connected ");
                dbConnectionListener.onConnectionStatusInfo("[*] connected ");
                dbConnectionListener.onConnectionSuccessful();

            } catch (Exception e) {

                Log.i(LOG_TAG, "connecting failed");
                dbConnectionListener
                        .onConnectionStatusInfo("[x] connecting failed");
                dbConnectionListener.onConnectionFailed();
                e.printStackTrace();
                Log.e(LOG_TAG, e.getMessage());

            }

            return null;
        }
    }

    public static Dao instance(DBConnectionListener listener) {

        singletonObj.dbConnectionListener = listener;
        return singletonObj;

    }

    public void connect(String serverAddress, String password, String username,
            String dbName) {

        this.serverAddress = serverAddress;
        this.dbName = dbName;
        this.username = username;
        this.password = password;

        if (conn == null) {

            (new DBConnectionTask()).execute(null, null, null);

        } else {

            dbConnectionListener.onConnectionSuccessful();

        }

    }

    /**
     * Call when your activity is destroyed
     */
    public void disconnect() {

        if (conn != null) {

            try {

                if (!conn.isClosed()) {

                    conn.close();
                    Log.i(LOG_TAG, "db connection closed");
                    dbConnectionListener
                            .onConnectionStatusInfo("db connection closed");

                }

            } catch (SQLException e) {

                e.printStackTrace();
                Log.e(LOG_TAG, "db connection failed during close()");
                dbConnectionListener
                        .onConnectionStatusInfo("db connection failed to close() ");

            } finally {

                conn = null;

            }

        }

    }

    /**
     * This is a stub and not a functional one, of how to use your new conn
     * object, connected to your mssql server. More Info :
     * http://developer.android.com/reference/java/sql/package-summary.html
     */
    public void addUser(/* Some user info Model as parameter */) {

        Log.i(LOG_TAG, "in adduser()");

        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {

            Statement statement = conn.createStatement();
            ResultSet rs = statement
                    .executeQuery("INSERT INTO user_info_table "
                            + " VALUES ('1001', 'Bob', '333333', '33')");
            rs.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public String[][] searchProducts(int categoryID, String productName, int countyID) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            String query = "SELECT COUNT ([Product_ID]) AS 'ROWCOUNT'\n"
                    + "FROM [Products]\n"
                    + "JOIN [Regions]\n"
                    + "ON Regions.Region_ID=Products.Region_ID\n"
                    + "JOIN [Counties]\n"
                    + "ON Counties.County_ID=Regions.County_ID ";
            String queryAdditions = "";
            if (categoryID != 0 || !"".equals(productName) || countyID != 0) {
                queryAdditions += " WHERE";

                if (categoryID == 0) {
                    queryAdditions += " [Products].[Category_ID] > 0";
                } else {
                    queryAdditions += " [Products].[Category_ID] = " + categoryID;
                }

                if (productName.equals("")) {
                    queryAdditions += " AND [Products].[Product_Name] != ''";
                } else {
                    queryAdditions += " AND [Products].[Product_Name] LIKE '%" + productName;
                    queryAdditions += "%'";
                }
                if (countyID == 0) {
                    queryAdditions += " AND [Counties].[County_ID] > 0";
                } else {
                    queryAdditions += " AND [Counties].[County_ID] = " + countyID;
                }
            }

            Statement statement = conn.createStatement();
            ResultSet resultSetForCountingProducts = statement.executeQuery(query + queryAdditions);

            resultSetForCountingProducts.next();
            int numberOfProducts = resultSetForCountingProducts.getInt("rowcount");

            resultSetForCountingProducts.close();

            String resultsQuery = "SELECT Products.[Product_Name]\n"
                    + "      ,Products.[Product_Description]\n"
                    + "      ,Products.[Product_Price]   \n"
                    + "      ,Products.[Address]\n"
                    + "      ,Products.[Number]\n"
                    + "      ,Products.[Zip_Code]\n"
                    + "	  ,Products.Quantity\n"
                    + "	  ,Counties.County_Name\n"
                    + "	  ,Measurements.Measurement_Type\n"
                    + "	  ,Regions.Region_Name\n"
                    + "FROM [Products]\n"
                    + "JOIN [Regions]\n"
                    + "ON Regions.Region_ID=Products.Region_ID\n"
                    + "JOIN [Counties]\n"
                    + "ON Counties.County_ID=Regions.County_ID\n"
                    + "JOIN [Measurements]\n"
                    + "ON Measurements.Measurement_ID=Products.Measurement_ID";

            ResultSet rs = statement.executeQuery(resultsQuery + queryAdditions);

            String[][] resultArray = new String[numberOfProducts][10];
            rs.next();
            for (int i = 0; i < numberOfProducts; i++) {
                resultArray[i][0] = rs.getString("Product_Name");
                resultArray[i][1] = rs.getString("Product_Description");
                resultArray[i][2] = rs.getString("Address");
                resultArray[i][3] = Integer.toString(rs.getInt("Product_Price"));
                resultArray[i][4] = Integer.toString(rs.getInt("Number"));
                resultArray[i][5] = Integer.toString(rs.getInt("Zip_Code"));
                resultArray[i][6] = Integer.toString(rs.getInt("Quantity"));
                resultArray[i][7] = rs.getString("County_Name");
                resultArray[i][8] = rs.getString("Measurement_Type");
                resultArray[i][9] = rs.getString("Region_Name");
                rs.next();
            }

            rs.close();
            statement.close();

            return resultArray;
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return null;
    }

    public String[] getCategories() {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT [Category_Name]\n"
                    + "  FROM [dbo].[Categories]\n"
                    + "GO");

            String[] resultArray = new String[6];
            resultArray[0] = "----Επιλέξτε κατηγορία----";
            rs.next();
            for (int i = 1; i < 6; i++) {
                resultArray[i] = rs.getString("Category_Name");
                rs.next();
            }
            rs.close();
            statement.close();
            return resultArray;
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return null;
    }

    public String[] getLocation() {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT [County_Name]\n"
                    + "  FROM [dbo].[Counties]\n"
                    + "GO");

            String[] resultArray = new String[52];
            resultArray[0] = "----Επιλέξτε Νομό----";
            rs.next();
            for (int i = 1; i < 52; i++) {
                resultArray[i] = rs.getString("County_Name");
                rs.next();
            }
            rs.close();
            statement.close();
            return resultArray;
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return null;
    }

    void editFarmerProduct(int productID, int price, String productDescription, int quantity) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            String query = "UPDATE [dbo].[Products]\n"
                    + "   SET [Product_Description] = " + productDescription
                    + "      ,[Product_Price] = " + price
                    + "      ,[Quantity] = " + quantity
                    + " WHERE ";
            String queryAdditions = "";

            queryAdditions += " Product_ID = " + productID;

            Statement statement = conn.createStatement();
            ResultSet resultSetForCountingProducts = statement.executeQuery(query + queryAdditions);

            resultSetForCountingProducts.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
    void deleteProduct(int productId) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            
            String farmerProductsQuery = "DELETE FROM [dbo].[Farmer_Products]\n" +
                "      WHERE Product_ID = ? ;";
            PreparedStatement statement = conn.prepareStatement(farmerProductsQuery);
            statement.setInt(1, productId);
            statement.executeUpdate();
                        
            String productquery = "DELETE FROM [dbo].[Products]\n" +
                        "      WHERE Product_ID = ?;"; 
            
            PreparedStatement productStatement = conn.prepareStatement(productquery);
            productStatement.setInt(1, productId);
            productStatement.executeUpdate();
                        
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
