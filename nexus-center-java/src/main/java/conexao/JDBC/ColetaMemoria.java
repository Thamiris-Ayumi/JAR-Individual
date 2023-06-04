/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexao.JDBC;

import com.github.britooo.looca.api.group.memoria.Memoria;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author thamiris
 */
public class ColetaMemoria {

    private DateTimeFormatter formatter;
    private Integer idMetrica;
    private Double capacidade;
    private Double valorUtilizado;
    private String unidadeMedida;
    private String tipoComponente;
    private String dataHora;
    private String modeloComponente;
    private Memoria memoria;

    public ColetaMemoria() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        memoria = new Memoria();
        this.idMetrica = null;
        this.capacidade = memoria.getTotal().doubleValue() / 1000000000;
        this.valorUtilizado = memoria.getEmUso().doubleValue() / 1000000000;
        this.unidadeMedida = "GB";
        this.tipoComponente = "Memória";
        this.dataHora = LocalDateTime.now().format(formatter);
        this.modeloComponente = "Unknow";
    }

    public JdbcTemplate conectmem() {
        JdbcTemplate conection = new Conexao().getConnection();
        return conection;
    }

    public JdbcTemplate conectmemazu() {
        JdbcTemplate conection2 = new Conexao().getConnectionAzu();
        return conection2;
    }

    public void enviaDadosMem(Integer fkMaquina, Integer fkEmpresa) {
        ColetaMemoria coleta = new ColetaMemoria();

        this.conectmem().update("insert into Metrica values(?,?,?,?,?,?,?)",
                coleta.idMetrica = null,
                coleta.valorUtilizado,
                coleta.unidadeMedida,
                coleta.dataHora,
                fkMaquina,
                fkEmpresa,
                3);
        this.conectmemazu().update("insert into Metrica(valorUtilizado,unidadeMedida,dataHora,fkMaquina,fkEmpresa,fkComponente) values(?,?,?,?,?,?)",
                
                coleta.valorUtilizado,
                coleta.unidadeMedida,
                coleta.dataHora,
                fkMaquina,
                fkEmpresa.toString(),
                3);

    }

    public void enviaDadosMemazu(Integer fkMaquina, Integer fkEmpresa) {
        ColetaMemoria coleta = new ColetaMemoria();


    }

//  Para enviar à entidade Configuração Componente:
    public Double getCapacidade() {
        return capacidade;
    }
    
//  Para enviar à entidade Metrica:
    public Double getValorUtilizado() {
        return valorUtilizado;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    
//  Para enviar à entidade Componente:
    public String getTipoComponente() {
        return tipoComponente;
    }

    public String getModeloComponente() {
        return modeloComponente;
    }

    @Override
    public String toString() {
        return "ColetarMemoria{" + "capacidade=" + capacidade + ", valorUtilizado=" + valorUtilizado + ", unidadeMedida=" + unidadeMedida + ", tipoComponente=" + tipoComponente + ", modeloComponente=" + modeloComponente + '}';
    }

}
