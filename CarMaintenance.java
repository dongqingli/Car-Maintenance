import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javax.sql.*;
import javax.naming.*;
import java.sql.*;
import java.util.*;
import com.mysql.jdbc.jdbc2.optional.*;
import java.math.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
public class CarMaintenance extends Application {
	Connection conn = null;
	//Pane
	BorderPane borderPane = new BorderPane();
    GridPane topPane = new GridPane();
    GridPane center1Pane = new GridPane();
    GridPane center2Pane = new GridPane();
    GridPane bottomPane = new GridPane();
    VBox vbox = new VBox(2);

	@Override
	public void start(Stage applicationStage) {
	    //mysql connection
		
		try {
			MysqlDataSource dataSource = new MysqlDataSource();			
			dataSource.setUser("dl369");
			dataSource.setPassword("qCqEQbUb");
			dataSource.setServerName("sql.njit.edu");
			dataSource.setDatabaseName("dl369");
			conn = dataSource.getConnection();
		}
		catch (Exception e) {
		      System.out.println("Connect to database err: " + e.getMessage());
		      e.printStackTrace();
		}
		Scene scene = null;
	    Insets gridPadding = null;
	    //Button
	    Button search = new Button("Search");
	    Button addNew = new Button("New");
	    Button update = new Button("Update");
	    Button delete = new Button("Delete");
	    //Label and text
	    Label [] topLabel = new Label[3];
	    String [] topName = new String[]{"Name", "From date", "To date"};
	    TextField [] topField = new TextField[3];
	    Label [] bottomLabel = new Label[10];
	    String [] bottomName = new String[]{"Description", "Name", "Street", "City", "State", "ZipCode", "Date", "Cost","Comments", "  "};
	    TextField [] bottomField = new TextField[9];
	    Label editLabel = new Label("Edit the information below:");
	    for (int i = 0; i < 3; i++) {
	    	int j = 0;
	    	if (i == 0) j = 1;
	    	if (i == 1) j = 4;
	    	if (i == 2) j = 5;
	    	topLabel[i] = new Label(topName[i]);
	    	topPane.add(topLabel[i], j, 0);
	    	GridPane.setHalignment(topLabel[i], HPos.CENTER);
	    	topField[i] = new TextField();
	    	topPane.add(topField[i], j, 1);
    	}
	    topPane.add(search, 6, 1);
/*	    
	    for (int i = 0; i < 9; i++) {
	    	bottomLabel[i] = new Label(bottomName[i]);
	    	center1Pane.add(bottomLabel[i], i, 1);
	    	GridPane.setHalignment(bottomLabel[i], HPos.CENTER);
    	}
*/
	    Double size = 80.0;
	    Double size1 = 90.0;
	    for (int i = 0; i < 10; i++) {
	    	bottomLabel[i] = new Label(bottomName[i]);
	    	bottomLabel[i].setMinWidth(size);
	    	center2Pane.add(bottomLabel[i], i, 2);
	    	GridPane.setHalignment(bottomLabel[i], HPos.CENTER);
    	}
//	    bottomLabel[0].setMinWidth(size1);
//	    bottomLabel[3].setMinWidth(size1);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MaintenanceAndRepairs");
//			System.out.println(rs.getString("ID"));
			int row = 2;
			while (rs.next()) {
				int id = rs.getInt("ID");
				String store = rs.getString("NAME");
				String street = rs.getString("Street");
				String city = rs.getString("City");
				String state = rs.getString("State");
				String zipcode = rs.getString("ZipCode");
				java.sql.Date date = rs.getDate("Date");
				java.math.BigDecimal cost = rs.getBigDecimal("Cost");
				int serviceid = rs.getInt("ServiceID");
				String serviceind = rs.getString("ServiceInd");
				String comments = rs.getString("Comments");
				String table = null;
				String subid = null;
				if (serviceind.equals("M")) {
					table = "MaintenanceTypes";
					subid = "MaintenanceID";
				}
				else {
					table = "RepairTypes";
					subid = "RepairID";
				}
				String sql = "SELECT * FROM " + table + " WHERE " + subid + " = " + serviceid;
//				System.out.println(sql);
				Statement stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(sql);
				String description = null;
				while (rs1.next()) {
					description = rs1.getString("Description");
				}
				rs1.close();
				center1Pane.add(new Label(description), 0, row);
				center1Pane.add(new Label(store), 1, row);
				center1Pane.add(new Label(street), 2, row);
				center1Pane.add(new Label(city), 3, row);
				center1Pane.add(new Label(state), 4, row);
				center1Pane.add(new Label(zipcode), 5, row);
				center1Pane.add(new Label(date.toString()), 6, row);
				center1Pane.add(new Label(cost.toString()), 7, row);
				center1Pane.add(new Label(comments), 8, row);
				center1Pane.add(new Button("edit"), 9, row);
				row++;

			}
			rs.close();
		}
		catch (Exception e) {
		      System.out.println("Query database err: " + e.getMessage());
		      e.printStackTrace();
		}

/*	    
	    for (int i = 0; i < 3; i++) {
	    	topLabel[i] = new Label(topName[i]);
	    	topPane.add(topLabel[i], i * 3, 0);
	    	GridPane.setHalignment(topLabel[i], HPos.CENTER);
	    	topField[i] = new TextField();
	    	topPane.add(topField[i], i * 3, 1);
    	}
*/    	
	    bottomPane.add(editLabel, 0, 0);
	    bottomPane.setColumnSpan(editLabel, 5);
	    for (int i = 0; i < 9; i++) {
	    	bottomLabel[i] = new Label(bottomName[i]);
	    	bottomPane.add(bottomLabel[i], i, 1);
	    	GridPane.setHalignment(bottomLabel[i], HPos.CENTER);
	    	bottomField[i] = new TextField();
	    	bottomPane.add(bottomField[i], i, 2);
    	}
	    bottomPane.add(addNew, 3, 4);
	    bottomPane.add(update, 4, 4);
	    bottomPane.add(delete, 5, 4);
	    
	    search.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	            String storeName = topField[0].getText();
	            String fromdate = topField[1].getText();
	            String todate = topField[2].getText();
	            String defaultfromdate = "2000-01-01";
	            String defaulttodate = "2020-01-01";
	            String sql = null;
	            if (storeName.equals("") && fromdate.equals("") && todate.equals("")) {
	            	return;
	            }
	            
            	if (fromdate.equals("")) {
            		fromdate = defaultfromdate;
            	}
            	if (todate.equals("")) {
            		todate = defaulttodate;
            	}
            	if (storeName.equals("")) {
            		sql = "SELECT * FROM MaintenanceAndRepairs WHERE Date Between '" + fromdate + "' and '" + todate + "'";
            	}
            	else {
            		sql = "SELECT * FROM MaintenanceAndRepairs WHERE Name = '" + storeName + "' AND " + "Date Between '" + fromdate + "' and '" + todate + "'";
            	}
            	center1Pane.getChildren().clear();
//            	center1Pane.getChildren().retainAll(center1Pane.getChildren().get(0));
/*            	for (int i = 0; i < 9; i++) {
        	    	bottomLabel[i] = new Label(bottomName[i]);
        	    	center1Pane.add(bottomLabel[i], i, 1);
        	    	GridPane.setHalignment(bottomLabel[i], HPos.CENTER);
            	}
*/            	
        		try {
        			Statement stmt = conn.createStatement();
        			System.out.println(sql);
        			ResultSet rs = stmt.executeQuery(sql);
//        			System.out.println(rs.getString("ID"));
        			int row = 2;
        			while (rs.next()) {
        				int id = rs.getInt("ID");
        				String store = rs.getString("NAME");
        				String street = rs.getString("Street");
        				String city = rs.getString("City");
        				String state = rs.getString("State");
        				String zipcode = rs.getString("ZipCode");
        				java.sql.Date date = rs.getDate("Date");
        				java.math.BigDecimal cost = rs.getBigDecimal("Cost");
        				int serviceid = rs.getInt("ServiceID");
        				String serviceind = rs.getString("ServiceInd");
        				String comments = rs.getString("Comments");
        				String table = null;
        				String subid = null;
        				if (serviceind.equals("M")) {
        					table = "MaintenanceTypes";
        					subid = "MaintenanceID";
        				}
        				else {
        					table = "RepairTypes";
        					subid = "RepairID";
        				}
        				String sql1 = "SELECT * FROM " + table + " WHERE " + subid + " = " + serviceid;
//        				System.out.println(sql);
        				Statement stmt1 = conn.createStatement();
        				ResultSet rs1 = stmt1.executeQuery(sql1);
        				String description = null;
        				while (rs1.next()) {
        					description = rs1.getString("Description");
        				}
        				rs1.close();
        				center1Pane.add(new Label(description), 0, row);
        				center1Pane.add(new Label(store), 1, row);
        				center1Pane.add(new Label(street), 2, row);
        				center1Pane.add(new Label(city), 3, row);
        				center1Pane.add(new Label(state), 4, row);
        				center1Pane.add(new Label(zipcode), 5, row);
        				center1Pane.add(new Label(date.toString()), 6, row);
        				center1Pane.add(new Label(cost.toString()), 7, row);
        				center1Pane.add(new Label(comments), 8, row);
        				center1Pane.add(new Button("edit"), 9, row);
        				row++;

        			}
        			rs.close();
        		}
        		catch (Exception e) {
        		      System.out.println("Query database err: " + e.getMessage());
        		      e.printStackTrace();
        		}
        	    center1Pane.setPadding(new Insets(10, 10, 10, 10)); 
        	    center1Pane.setHgap(30);
        	    center1Pane.setVgap(5);
        	    center1Pane.setAlignment(Pos.TOP_CENTER);
	            return;
	         } 
	      });
	    vbox.getChildren().addAll(center2Pane, center1Pane);
	    scene = new Scene(borderPane);	    
	    gridPadding = new Insets(10, 10, 10, 10);
	    topPane.setPadding(gridPadding); 
	    topPane.setHgap(20);
	    topPane.setVgap(5);
	    topPane.setAlignment(Pos.TOP_CENTER);
	    center1Pane.setPadding(gridPadding); 
	    center1Pane.setHgap(30);
	    center1Pane.setVgap(5);
	    center1Pane.setAlignment(Pos.TOP_CENTER);
	    center2Pane.setPadding(new Insets(10, 10, 10, 10)); 
	    center2Pane.setHgap(30);
	    center2Pane.setVgap(5);
	    center2Pane.setAlignment(Pos.TOP_CENTER);
	    bottomPane.setPadding(gridPadding); 
	    bottomPane.setHgap(10);
	    bottomPane.setVgap(10);
	    borderPane.setTop(topPane);
	    borderPane.setCenter(vbox);
//	    borderPane.setCenter(center1Pane);
//	    borderPane.setTop(center2Pane);
	    borderPane.setBottom(bottomPane);

	    applicationStage.setScene(scene);
	    applicationStage.setTitle("Car Maintenance and Repair");
	    applicationStage.show();
	}
	public static void main(String [] args) {
	      launch(args); // Launch application

	      return;
	   }
}
