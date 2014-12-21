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
}
