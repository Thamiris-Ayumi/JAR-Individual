/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import conexao.JDBC.Conexao;
import conexao.JDBC.Empresa;
import conexao.JDBC.EnviaDados;
import conexao.JDBC.InfoMaquina;
import conexao.JDBC.Maquina;
import conexao.JDBC.RegistroAtividade;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author thamiris
 */
public class EnviaDados {

    Conexao conexao = new Conexao();
    JdbcTemplate conMysql = conexao.getConnection();
    JdbcTemplate conAzure = conexao.getConnectionAzu();
//    MetricaMouse testeenvio;
    ColetaHDInfo testeenvio2;
    ColetaMemoria testeenvio3;
    ColetaProcessador testeenvio4;

    public void iniciarEnvio(Integer fkMaquina,Integer fkEmpresa) {
//        testeenvio = new MetricaMouse(fkMaquina,fkEmpresa);
        testeenvio2= new ColetaHDInfo();
        testeenvio3= new ColetaMemoria();
        testeenvio4= new ColetaProcessador();
        while (true) {
            try {
                testeenvio4.enviaDadosProcessador(fkMaquina, fkEmpresa);
//                testeenvio4.enviaDadosProcessadorazu(fkMaquina, fkEmpresa);
                testeenvio3.enviaDadosMem(fkMaquina, fkEmpresa);
                testeenvio2.enviaDadosTotalhd(fkMaquina, fkEmpresa);
//                testeenvio.enviaDadosMouse(fkMaquina, fkEmpresa);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
