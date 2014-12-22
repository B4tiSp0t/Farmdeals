package Farm.Deals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;
import android.util.Log;
import java.sql.Date;
import java.sql.PreparedStatement;

public class Dao {

    private static final String LOG_TAG = Dao.class.getName();
    private static Connection conn;
    private static final Dao singletonObj = new Dao();
    private DBConnectionListener dbConnectionListener;
    private String serverAddress;
    private String password;
    private String username;
    private String dbName;
    private String result;

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
                
                DriverManager.setLoginTimeout(10);
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

            String resultsQuery = "SELECT Products.[Product_ID] \n"
                    + "      ,Products.[Product_Name]\n"
                    + "      ,Products.[Product_Price]   \n"
                    + "	  ,Products.Quantity\n"
                    + "	  ,Counties.County_Name\n"
                    + "FROM [Products]\n"
                    + "JOIN [Regions]\n"
                    + "ON Regions.Region_ID=Products.Region_ID\n"
                    + "JOIN [Counties]\n"
                    + "ON Counties.County_ID=Regions.County_ID\n"
                    + "JOIN [Measurements]\n"
                    + "ON Measurements.Measurement_ID=Products.Measurement_ID";

            ResultSet rs = statement.executeQuery(resultsQuery + queryAdditions);

            String[][] resultArray = new String[numberOfProducts][5];
            rs.next();
            for (int i = 0; i < numberOfProducts; i++) {
                resultArray[i][0] = Integer.toString(rs.getInt("Product_ID"));
                resultArray[i][1] = rs.getString("Product_Name");
                resultArray[i][2] = rs.getString("Product_Price");
                resultArray[i][3] = Integer.toString(rs.getInt("Quantity"));
                resultArray[i][4] = rs.getString("County_Name");
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
            resultArray[0] = "--Επιλέξτε κατηγορία--";
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
            resultArray[0] = "--Επιλέξτε Νομό--";
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
    
  public String[][] getAccounts()
        {
            if (conn == null) {
                throw new InstantiationError("call Dao.connect(...) before calling Dao operations");
            }
            try{
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT [Email]\n"
                                            + "      ,[Password]\n"
                                            + "  FROM [dbo].[Accounts]\n"
                                            + "GO");
                         
               String[][] resultArray = new String[15][2];
               rs.next();
               for (int i = 0; i < 15; i++) {
                   resultArray[i][0] = rs.getString("Email");
                   resultArray[i][1] = rs.getString("Password");
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
        
        public int login(String email, String password)
        {
            if (conn == null) {

			throw new InstantiationError(
					"call Dao.connect(...) before calling Dao operations");

		}

		try {
                        String query = "SELECT COUNT ([Account_ID]) AS 'rowcount'\n"
                            + "FROM [Accounts]\n"
                                 + "WHERE [Email] = '" + email + "' AND "
                                + "[Password] = '" + password + "'";
      
                
			Statement statement = conn.createStatement();
                        ResultSet resultSetForCountingAccounts = statement.executeQuery(query);
 
                        resultSetForCountingAccounts.next();
                        int numberOfAccounts = resultSetForCountingAccounts.getInt("rowcount");
                        resultSetForCountingAccounts.close();
                        if(numberOfAccounts <= 0)
                        {
                            return numberOfAccounts;
                        }
                               
                         String resultsQuery = "SELECT [Account_ID]\n"
                            +"       ,[Email]\n"
                            + "      ,[Password]\n"
                            + "FROM [Accounts]\n"
                            + "WHERE [Email] = '" + email + "' AND "
                            + "[Password] = '" + password + "'";
                         
                         ResultSet rs = statement.executeQuery(resultsQuery);
                         rs.next();
                         int accountId = rs.getInt("Account_ID");
                        
                         rs.close();
                        statement.close();

                         return accountId;
		} catch (SQLException e) {

			e.printStackTrace();

		}
        return 0;    
        }

    public int signup(String name, String surname, String RC, String phone, String DOB, String address, String email, String password, String passwordConfirm) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            Date dob = Date.valueOf(DOB);
            String accountquery = "INSERT INTO [dbo].[Accounts]([Email],[Password]) VALUES(?,?);";
            PreparedStatement statement = conn.prepareStatement(accountquery);
            
            statement.setString(1, email);
            statement.setString(2, password);
            
            int accountSuccess = statement.executeUpdate();
            
            
            String query = "SELECT  [Account_ID]\n" +
                            "  FROM [projectdb].[dbo].[Accounts]" +
                    " WHERE Email = '" + email + "' AND Password = '" + password + "';";
      
            Statement getAccountIDStatement = conn.createStatement();
            ResultSet resultSet = getAccountIDStatement.executeQuery(query);
            
            resultSet.next();
            int accountId = resultSet.getInt("Account_ID");
            resultSet.close();
            
            
            
            String insertQuery = "INSERT INTO [dbo].[Farmer]\n"
                    + "           ([Farmer_Name]\n"
                    + "           ,[Farmer_Surname]\n"
                    + "           ,[Farmer_DOB]\n"
                    + "           ,[Farmer_Address]\n"
                    + "           ,[Farmer_Phone]\n"
                    + "           ,[Farmer_RC]\n"
                    + "           ,[Account_ID])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,?,?,?,?);";

            PreparedStatement insertFarmerDetails = conn.prepareStatement(insertQuery);
            insertFarmerDetails.setString(1, name);
            insertFarmerDetails.setString(2, surname);
            insertFarmerDetails.setDate(3, dob);
            insertFarmerDetails.setString(4, address);
            insertFarmerDetails.setString(5, phone);
            insertFarmerDetails.setString(6, RC);
            insertFarmerDetails.setInt(7, accountId);
            
            int farmerSuccess = insertFarmerDetails.executeUpdate();
            
            if (farmerSuccess == 1 && accountSuccess == 1) {
                return 1;
            }else 
            {
                return 0;
            }
            
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }
    
    public String[] getProductInfo(int productId)
    {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            String query = "SELECT [Product_Name]\n"
                    + "      ,[Product_Description]\n"
                    + "      ,[Product_Price]\n"
                    + "      ,[Address]\n"
                    + "      ,[Number]\n"
                    + "      ,[Zip_Code]\n"
                    + "      ,[Quantity]\n"
                    + "	  ,[Region_Name]\n"
                    + "	  ,[Measurement_Type]\n"
                    + "	  ,[Counties].County_Name\n"
                    + "	  ,[Farmer].Farmer_Name\n"
                    + "	  ,[Farmer].Farmer_Surname\n"
                    + "	  ,[Farmer].Farmer_Phone\n"
                    + "  FROM [dbo].[Products]\n"
                    + "  JOIN [Regions]\n"
                    + "                    ON Regions.Region_ID=Products.Region_ID\n"
                    + "                    JOIN [Counties]\n"
                    + "                    ON Counties.County_ID=Regions.County_ID\n"
                    + "                    JOIN [Measurements]\n"
                    + "                    ON Measurements.Measurement_ID=Products.Measurement_ID\n"
                    + "					JOIN [Farmer_Products]\n"
                    + "                    ON Farmer_Products.Product_ID = Products.Product_ID\n"
                    + "					JOIN [Farmer]\n"
                    + "                    ON Farmer.Farmer_ID = Farmer_Products.Farmer_ID "
                    + "                    WHERE Products.Product_ID = " + productId + ";";
            

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            

            String[] resultArray = new String[13];
            rs.next();
           
                resultArray[0] = rs.getString("Product_Name");
                resultArray[1] = rs.getString("Product_Description");
                resultArray[2] = Integer.toString(rs.getInt("Quantity"));
                resultArray[3] = rs.getString("Measurement_Type");
                resultArray[4] = rs.getString("Product_Price");
                resultArray[5] = rs.getString("Address");
                resultArray[6] = rs.getString("Number");
                resultArray[7] = rs.getString("Zip_Code");
                resultArray[8] = rs.getString("Region_Name");
                resultArray[9] = rs.getString("County_Name");
                resultArray[10] = rs.getString("Farmer_Name");
                resultArray[11] = rs.getString("Farmer_Surname");
                resultArray[12] = rs.getString("Farmer_Phone");
            

            rs.close();
            statement.close();

            return resultArray;
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return null;
    }

    public String[][] getFarmerProducts(int farmerID) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {
            String query = "SELECT COUNT (Products.Product_ID) AS 'ROWCOUNT'\n"
                    + "FROM [Products]\n"
                    + "  JOIN Farmer_Products\n"
                    + "  ON Farmer_Products.Product_ID=Products.Product_ID\n"
                    + "  JOIN Farmer\n"
                    + "  ON Farmer.Farmer_ID=Farmer_Products.Farmer_ID\n"
                    + "  WHERE Farmer.Farmer_ID= " + farmerID + ";";

            

            Statement statement = conn.createStatement();
            ResultSet resultSetForCountingProducts = statement.executeQuery(query);

            resultSetForCountingProducts.next();
            int numberOfProducts = resultSetForCountingProducts.getInt("rowcount");

            resultSetForCountingProducts.close();

            String resultsQuery = "SELECT Products.[Product_ID] \n" 
                    + "      ,[Product_Name]\n"
                    + "      ,[Product_Price]\n"
                    + "      ,[Quantity]\n"
                    + "      ,Counties.County_Name \n"
                    + "  FROM [Products]\n"
                    + "  JOIN Farmer_Products\n"
                    + "  ON Farmer_Products.Product_ID=Products.Product_ID\n"
                    + "  JOIN [Regions]\n"
                    + "  ON Regions.Region_ID=Products.Region_ID\n"
                    + "  JOIN [Counties]\n"
                    + "  ON Counties.County_ID=Regions.County_ID\n"
                    + "  JOIN Farmer\n"
                    + "  ON Farmer.Farmer_ID = Farmer_Products.Farmer_ID\n"
                    + "  WHERE Farmer.Farmer_ID=" + farmerID;

            ResultSet rs = statement.executeQuery(resultsQuery);

            String[][] resultArray = new String[numberOfProducts][5];
            rs.next();
            for (int i = 0; i < numberOfProducts; i++) {
                resultArray[i][0] = Integer.toString(rs.getInt("Product_ID"));
                resultArray[i][1] = rs.getString("Product_Name");
                resultArray[i][2] = rs.getString("Product_Price");
                resultArray[i][3] = Integer.toString(rs.getInt("Quantity"));
                resultArray[i][4] = rs.getString("County_Name");
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
    
    void editFarmerProduct(int productID, String price, String productDescription, int quantity) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {

            String updateTableSQL = "UPDATE [dbo].[Products]\n"
                    + "   SET [Product_Description] = ?\n"
                    + "      ,[Product_Price] = ?\n"
                    + "      ,[Quantity] = ?\n"
                    + " WHERE Product_ID = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(updateTableSQL);
            preparedStatement.setString(1, productDescription);
            preparedStatement.setString(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, productID);
            preparedStatement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

        }
        
        
        
        
        
    }
    
    int insertProduct(String name, String description, String quantity, String price, String region, String address, String number, String zipcode, int categoryID, int locationID, int accountId) {
        if (conn == null) {

            throw new InstantiationError(
                    "call Dao.connect(...) before calling Dao operations");

        }

        try {

            String accountquery = "INSERT INTO [dbo].[Regions]\n"
                    + "           ([Region_Name]\n"
                    + "           ,[County_ID])\n"
                    + "     VALUES\n"
                    + "           (?,?);";
            PreparedStatement statement = conn.prepareStatement(accountquery);

            statement.setString(1, region);
            statement.setInt(2, locationID);
            
            int regionSuccess = statement.executeUpdate();
            
            Statement regionIdStatement = conn.createStatement();
            
            ResultSet rs =  regionIdStatement.executeQuery("SELECT [Region_ID] FROM [dbo].[Regions] WHERE Region_Name = '" +region +"';");
            rs.next();
            int regionId = rs.getInt("Region_ID");
            rs.close();
            
            
            
            ResultSet farmerRs = regionIdStatement.executeQuery("SELECT [Farmer_ID]\n"
                    + "  FROM [dbo].[Farmer]\n"
                    + "  WHERE Account_ID = " + accountId + ";");
            farmerRs.next();
                    
            int farmerId = farmerRs.getInt("Farmer_ID");
            farmerRs.close();
            
                     
            
            String insertQuery = "INSERT INTO [dbo].[Products]\n"
                    + "           ([Product_Name]\n"
                    + "           ,[Product_Description]\n"
                    + "           ,[Measurement_ID]\n"
                    + "           ,[Product_Price]\n"
                    + "           ,[Category_ID]\n"
                    + "           ,[Region_ID]\n"
                    + "           ,[Address]\n"
                    + "           ,[Number]\n"
                    + "           ,[Zip_Code]\n"
                    + "           ,[Quantity])\n"
                    + "     VALUES\n"
                    + "           (?,?,? ,? ,? ,? ,?,? ,? ,?);";

            PreparedStatement insertProductStatement = conn.prepareStatement(insertQuery);
            insertProductStatement.setString(1, name);
            insertProductStatement.setString(2, description);
            insertProductStatement.setInt(3, 1);
            insertProductStatement.setString(4, price);
            insertProductStatement.setInt(5, categoryID);
            insertProductStatement.setInt(6, regionId);
            insertProductStatement.setString(7, address);
            insertProductStatement.setString(8, number);
            insertProductStatement.setString(9, zipcode);
            insertProductStatement.setInt(10, Integer.parseInt(quantity));
            
            int productSuccess = insertProductStatement.executeUpdate();
            
            ResultSet productRs = regionIdStatement.executeQuery("SELECT [Product_ID]\n"
                    + "  FROM [dbo].[Products]\n"
                    + "  WHERE Product_Name = '" + name + "' AND Product_Price = '" + price + "';");
            productRs.next();
            int productId = productRs.getInt("Product_ID");
            productRs.close();
            
            
            
            
            
            String farmerProductquery = "INSERT INTO [dbo].[Farmer_Products]\n"
                    + "           ([Farmer_ID]\n"
                    + "           ,[Product_ID])\n"
                    + "     VALUES\n"
                    + "           (?,?);";
            PreparedStatement farmerProductStatement = conn.prepareStatement(farmerProductquery);

            farmerProductStatement.setInt(1, farmerId);
            farmerProductStatement.setInt(2, productId);
            
            int farmerProductSuccess = farmerProductStatement.executeUpdate();
            
            
            if (regionSuccess == 1 && productSuccess == 1 && farmerProductSuccess == 1) {
                return 1;
            }else 
            {
                return 0;
            }
            
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }
}

