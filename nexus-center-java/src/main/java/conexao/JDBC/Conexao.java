/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {

    private JdbcTemplate connectionmysql;
    private JdbcTemplate connectionazure;

    public Conexao() {
        BasicDataSource dataSourcemysql = new BasicDataSource();
        dataSourcemysql.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // CONEXÃO BANCO LOCAL
        dataSourcemysql.setUrl("jdbc:mysql://localhost:3306/NexusCenter");
        dataSourcemysql.setUsername("root");
        dataSourcemysql.setPassword("sptech");
        this.connectionmysql = new JdbcTemplate(dataSourcemysql);
        
        // CONEXÃO BANCO AZURE
        BasicDataSource dataSourceMSSQL = new BasicDataSource();   
        dataSourceMSSQL.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
        dataSourceMSSQL.setUrl("jdbc:sqlserver://nexus-center.database.windows.net:1433;database=nexus-center;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");    
        dataSourceMSSQL.setUsername("admin-nexus-center");    
        dataSourceMSSQL.setPassword("#Gfgrupo6");
        this.connectionazure = new JdbcTemplate(dataSourceMSSQL);
        
    }

    public JdbcTemplate getConnection() {
        return connectionmysql;
    }
    
    public JdbcTemplate getConnectionAzu(){
        return connectionazure;
    }
    
    // Conexão com o banco de dados da Azure
    public Connection conectaBDAzure() {
        Connection con = null;

        try {
            String url = "jdbc:sqlserver://nexus-center.database.windows.net:1433;database=nexus-center;user=admin-nexus-center;password={#Gfgrupo6};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            con = DriverManager.getConnection(url);
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "ConexaoDAO" + erro.getMessage());
        }
        return con;
    }
;

}