package pachet;
import javax.script.ScriptContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp
{

    public static void afisare (List<Persoana> persoanaList)
    {
        for(Persoana st : persoanaList)
        {
            System.out.println(st);
        }
    }

    public static void main(String[] args) throws SQLException
    {
        Connection connection = connect_to_data_base();
        Statement statement = connection.createStatement();


        List<Persoana> persoanaList = copy_to_list(connection, statement);



        int opt;
        Scanner scanner = new Scanner(System.in);
        do
        {
            System.out.println("\n\nM E N I U\n");
            System.out.println("1. Afisare lista\n");
            System.out.println("");
            System.out.println("");
            System.out.println("");

            System.out.println("Optiunea: ");
            opt = scanner.nextInt();

            switch(opt)
            {
                case 1:
                    afisare(persoanaList);
                    break;

                case 2:
                    break;

                default:
                    System.out.println("Optiune gresita");
                    break;
            }

        }while(opt != 0);



    }

    public static Connection connect_to_data_base() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lab8";
        Scanner sc = new Scanner(System.in);
        String password = sc.next();
        Connection connection = DriverManager.getConnection (url, "root", password);
        return connection;
    }




    public static List<Persoana> copy_to_list (Connection connection, Statement statement) throws SQLException
    {
        List<Persoana> persoanaList = new ArrayList<>();

        ResultSet rs = statement.executeQuery("SELECT * FROM Persoana");

        try
        {
            String tableName = "persoane"; // Replace with your actual table name

            // Prepare the SQL query
            String sqlQuery = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Iterate through the result set and add data to the list
            while (resultSet.next()) {
                int id_persoana = resultSet.getInt("id_persoana");
                String nume = resultSet.getString("nume");
                int varsta = resultSet.getInt("varsta");

                // Create a new Persoana object and add it to the list
                Persoana persoana = new Persoana(id_persoana, nume, varsta);
                persoanaList.add(persoana);
            }

            resultSet.close(); // Close the ResultSet
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return persoanaList;
    }

}













