/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import conexao.JDBC.Conexao;
import conexao.JDBC.Maquina;

/**
 *
 * @author thamiris
 */

//(DAO) Data Access Object - Acesso ao banco de dados
public class UsuarioDAO {

    Connection con;

    // Autenticação do Usuário
    public ResultSet autenticsacaoUsuario(Maquina maquina) {

        con = new Conexao().conectaBDAzure();

        try {

            String sql = "select * from Maquina join Empresa on fkempresa=idempresa WHERE patrimonio = ? AND senha = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, maquina.getPatrimonio());
            pstm.setString(2, maquina.getSenha());

            ResultSet rs = pstm.executeQuery();

            return rs;

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO:" + erro);
            return null;
        }
       }

    }
   