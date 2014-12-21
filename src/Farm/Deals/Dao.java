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
        public String[][] getProducts()
        {
            if (conn == null) {

			throw new InstantiationError(
					"call Dao.connect(...) before calling Dao operations");

		}

		try {

			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT [Product_Name]\n"
                                    + "      ,[Product_Description]\n"
                                    + "      ,[Measurement_ID]\n"
                                    + "      ,[Product_Price]\n"
                                    + "      ,[Category_ID]\n"
                                    + "      ,[Location_ID]\n"
                                    + "  FROM [dbo].[Products]\n"
                                    + "GO");
                         
                        String[][] resultArray = new String[15][6];
                        rs.next();
                        for (int i = 0; i < 15; i++) {
                            resultArray[i][0] = rs.getString("Product_Name");
                            resultArray[i][1] = rs.getString("Product_Description");
                            resultArray[i][2] = rs.getString("Measurement_ID");
                            resultArray[i][3] = rs.getString("Product_Price");
                            resultArray[i][4] = rs.getString("Category_ID");
                            resultArray[i][5] = rs.getString("Location_ID");
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
         public String[] getCategories()
        {
            if (conn == null) {

			throw new InstantiationError(
					"call Dao.connect(...) before calling Dao operations");

		}

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT [Category_Name]\n"
                                    + "  FROM [dbo].[Categories]\n"
                                    + "GO");
                         
                        String[] resultArray = new String[5];
                        rs.next();
                        for (int i = 0; i < 5; i++) {
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
        public String[] getLocation()
        {
            if (conn == null) {

			throw new InstantiationError(
					"call Dao.connect(...) before calling Dao operations");

		}

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT [County_Name]\n"
                                    + "  FROM [dbo].[Counties]\n"
                                    + "GO");
                         
                        String[] resultArray = new String[15];
                        rs.next();
                        for (int i = 0; i < 15; i++) {
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
}
