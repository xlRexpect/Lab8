package ex1;

        import java.sql.*;
        import java.time.LocalDate;
        import java.time.Period;
        import java.util.InputMismatchException;
        import java.util.Scanner;

public class MainApp {

    private static class ExceptieVarsta extends Exception {
        public ExceptieVarsta(String message) {
            super(message);
        }
    }

    private static class ExceptieAnExcursie extends Exception {
        public ExceptieAnExcursie(String message) {
            super(message);
        }
    }
    public static void main(String[] args) throws SQLException, ExceptieVarsta {
        Connection connection=connectToDatabase();
        Statement statement=connection.createStatement();

        Scanner sc=new Scanner(System.in);
        int opt;
        do{
            System.out.println("====MENU====================================");
            System.out.println("0=exit");
            System.out.println("1=afisare tabele");
            System.out.println("2=adaugare persoana");
            System.out.println("3=adaugare excursii");
            System.out.println("4=afisare excursii pentru fiecare persoana");
            System.out.println("5=cautare excursii pentru o persoana");
            System.out.println("6=cautare persoana care au vizitat o destinatie");
            System.out.println("7=persoane care au facut excursii intr-un anumit an");
            System.out.println("8=stergere excursie");
            System.out.println("9=stergere persoana");
            opt=sc.nextInt();
            switch (opt){
                case 0->{
                    System.out.println("iesire din program!");
                }
                case 1->{
                    ResultSet rs = statement.executeQuery("Select * from persoane");
                    afisareTabele(rs,0);
                    rs.close();
                    rs = statement.executeQuery("Select * from excursii");
                    afisareTabele(rs,1);
                    rs.close();
                }
                case 2->{
                    System.out.println("====Adaugarepersoana======================");

                    System.out.println("nume=");
                    String nume = sc.next();
                    System.out.println("varsta");
                    try {
                        int varsta = readAgeFromUser();
                        AdaugarePersoana(connection, nume, varsta);
                    } catch (ExceptieVarsta e) {
                        System.out.println(e.getMessage());
                        // handle the exception or exit the program
                    }
                }
                case 3->{
                    System.out.println("====Adaugare excursie======================");
                    System.out.println("id persoana=");
                    int id_pers=sc.nextInt();
                    System.out.println("destinatia=");
                    String destinatia=sc.next();
                    System.out.println("anul=");
                    int an = readAnExcursie(connection,id_pers);
                    AdaugareExcursie(connection, id_pers, destinatia, an);
                }
                case 4->{
                    afisareExcursiiPentruPersoane(connection,statement);
                }
                case 5->{
                    System.out.println("====Cautare excursii======================");
                    System.out.println("nume=");
                    String nume=sc.next();
                    cautareExcursiiPersoana(connection,statement,nume);
                }
                case 6->{
                    System.out.println("====Cautare persoane======================");
                    System.out.println("destinatie=");
                    String destinatie=sc.next();
                    cautarePersoaneExcursie(connection,statement,destinatie);
                }
                case 7->{
                    System.out.println("====persoane care au facut excursii intr-un anumit an======");
                    System.out.println("anul=");
                    int anul=sc.nextInt();
                    cautarePersoanaAn(connection,statement,anul);
                }
                case 8->{
                    System.out.println("====Stergere excursie======");
                    System.out.println("id excursie=");
                    int idExcurs=sc.nextInt();
                    stergereExcurs(connection,statement,idExcurs);
                }
                case 9->{
                    System.out.println("====Stergere persoana======");
                    System.out.println("id persoana=");
                    int idPers=sc.nextInt();
                    stergerePersoana(connection,statement,idPers);
                }
            }
        }while(opt!=0);
        connection.close();
        statement.close();
    }

    private static int readAnExcursie(Connection connection,int id_pers){
        Scanner scanner = new Scanner(System.in);
        int varsta=0;
        while (true) {
            try{
                String sql="select varsta from persoane where id_persoana=?";
                try(PreparedStatement ps=connection.prepareStatement(sql)) {
                    ps.setInt(1, id_pers);
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            varsta = resultSet.getInt("varsta");
                            // Use 'intValue' as needed
                        } else {
                            System.out.println("No rows found.");
                        }
                    }
                }
                catch (SQLException e) {
                    System.out.println(sql);
                    e.printStackTrace();
                }
                System.out.print("Introduceți anul excursiei: ");
                int an= scanner.nextInt();
                if(an<(LocalDate.now().getYear()-varsta)){
                    throw new ExceptieAnExcursie("Persoana nu a putut sa particepe la aceasta excursie");
                }
                return an;
            }catch (InputMismatchException e) {
                System.out.println("Introduceți un număr întreg valid pentru an.");
                scanner.nextLine(); // Consume the invalid input
            }catch (ExceptieAnExcursie e){
                System.out.println(e.getMessage());
            }

        }
    }


    private static int readAgeFromUser() throws ExceptieVarsta {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Introduceți vârsta: ");
                int varsta = scanner.nextInt();
                if (varsta < 0 || varsta > 120) {
                    throw new ExceptieVarsta("Vârsta introdusă nu este validă.");
                }
                return varsta;
            } catch (InputMismatchException e) {
                System.out.println("Introduceți un număr întreg valid pentru vârstă.");
                scanner.nextLine(); // Consume the invalid input
            } catch (ExceptieVarsta e) {
                System.out.println(e.getMessage());
            }
        }
    }




    private static void stergerePersoana(Connection connection, Statement statement, int idPers) {
        String sql="delete from persoane where id_persoana=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setInt(1, idPers);
            int nr_randuri=ps.executeUpdate();
            System.out.println("\nNumar randuri afectate de modificare="+nr_randuri);
        }
        catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
    }

    private static void stergereExcurs(Connection connection, Statement statement, int idExcurs) {
        String sql="delete from excursii where id_excursie=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setInt(1, idExcurs);
            int nr_randuri=ps.executeUpdate();
            System.out.println("\nNumar randuri afectate de modificare="+nr_randuri);
        }
        catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
    }

    private static void cautarePersoanaAn(Connection connection, Statement statement, int anul) {
        String sql = "SELECT p.nume FROM persoane p "
                + "JOIN excursii e ON p.id_persoana = e.id_persoana "
                + "WHERE e.anul = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, anul);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Persoane care au facut excursii in anul " + anul + ":");
                while (rs.next()) {
                    String nume = rs.getString("nume");
                    System.out.println(nume);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cautarePersoaneExcursie(Connection connection, Statement statement, String destinatie) {
        String sql = "SELECT p.nume, p.varsta FROM persoane p "
                + "JOIN excursii e ON p.id_persoana = e.id_persoana "
                + "WHERE e.destinatia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, destinatie);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Persoane care mau vizitat " + destinatie + ":");
                while (rs.next()) {
                    String nume = rs.getString("nume");
                    System.out.println(nume);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cautareExcursiiPersoana(Connection connection, Statement statement, String nume) throws SQLException {

        String sql = "SELECT e.destinatia, e.anul FROM excursii e "
                + "JOIN persoane p ON e.id_persoana = p.id_persoana "
                + "WHERE p.nume = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nume);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Excursiile pentru " + nume + ":");
                while (rs.next()) {
                    String destinatia = rs.getString("destinatia");
                    int anul = rs.getInt("anul");
                    System.out.println("Destinația: " + destinatia + ", Anul: " + anul);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afisareExcursiiPentruPersoane(Connection connection, Statement statement) throws SQLException {
        ResultSet rsPers = statement.executeQuery("SELECT * FROM persoane");
        PreparedStatement psExcursii = connection.prepareStatement("SELECT * FROM excursii WHERE id_persoana = ?");
        while (rsPers.next()) {
            afisareRandTab1(rsPers);
            try {
                psExcursii.setInt(1, rsPers.getInt("id_persoana"));
                ResultSet rsExcurs = psExcursii.executeQuery();
                while (rsExcurs.next()) {
                    afisareRandTab2(rsExcurs);
                }
                rsExcurs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        rsPers.close();
        psExcursii.close();
    }


    private static void AdaugareExcursie(Connection connection, int idPers, String destinatia, int an) {
        String sql="INSERT INTO `lab8`.`excursii` (`id_persoana`, `destinatia`, `anul`) VALUES (?,?,?);";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setInt(1, idPers);
            ps.setString(2, destinatia);
            //if(checkExcursionYearFromUser(connection, ps,an,idPers)) {
                ps.setInt(3, an);
            //
            int nr_randuri=ps.executeUpdate();
            System.out.println("\nNumar randuri afectate de adaugare="+nr_randuri);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
    }

    private static void AdaugarePersoana(Connection connection,String nume, int varsta) {
        String sql="INSERT INTO `lab8`.`persoane` (`nume`, `varsta`) VALUES (?,?);";
        try(PreparedStatement ps=connection.prepareStatement(sql)) {
            ps.setString(1, nume);
            ps.setInt(2, varsta);
            int nr_randuri=ps.executeUpdate();
            System.out.println("\nNumar randuri afectate de adaugare="+nr_randuri);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
    }


    private static void afisareTabele(ResultSet rs, int t) throws SQLException {
        while(rs.next()){
            switch(t){
                case 0->{
                    afisareRandTab1(rs);
                }
                case 1->{
                    afisareRandTab2(rs);
                }
            }
        }
    }

    private static void afisareRandTab2(ResultSet rs) throws SQLException {
        System.out.println("id="+rs.getInt("id_persoana")+", id excursie= " + rs.getInt("id_excursie")+ ", destinatia="+rs.getString("destinatia")+", anul="+rs.getInt("anul"));
    }

    private static void afisareRandTab1(ResultSet rs) throws SQLException {
        System.out.println("id="+rs.getInt("id_persoana")+", nume= " + rs.getString("nume")+ ", varsta="+rs.getInt("varsta"));
    }

    private static Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lab8";
        System.out.println("parola: (pentru root)");
        Scanner sc=new Scanner(System.in);
        String password=sc.next();
        Connection connection = DriverManager.getConnection (url, "root", password);
        return connection;
    }

}
