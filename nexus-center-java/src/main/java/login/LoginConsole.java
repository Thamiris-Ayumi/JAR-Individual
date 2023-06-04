/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.dispositivos.DispositivoUsb;
import com.github.britooo.looca.api.group.dispositivos.DispositivosUsbGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import conexao.JDBC.Componente;
import conexao.JDBC.Conexao;
import conexao.JDBC.ConfiguracaoComponente;
import conexao.JDBC.Empresa;
import conexao.JDBC.EnviaDados;
import conexao.JDBC.InfoMaquina;
import conexao.JDBC.Maquina;
import conexao.JDBC.Metrica;
import conexao.JDBC.MetricaMouse;
import conexao.JDBC.RegistroAtividade;
import conexao.JDBC.Usb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author thamris
 */
public class LoginConsole {

    public static void main(String[] args) {
        Scanner leitor01 = new Scanner(System.in);
        System.out.println(""
                + "       ___  ___\n"
                + "    * |   \\ | | ____  __  __  _   _    ____ *\n"
                + "  *   |    \\| |/  _ \\ \\ \\/ / | | | |  /  __|  *\n"
                + "   *  |  |\\   |   __/ /    \\ | |_| | _\\  \\   *\n"
                + " *    |__| \\__|\\____|/__/\\__\\ \\___/ /____/    *\n"
                + "\n"
                + "       ____               _\n"
                + "   *  /  __\\ ____ __  __ | |__  ____  ____  *\n"
                + " *   |  /   /  _ \\| \\ | ||  __|/  _ \\|  _ \\   *\n"
                + "   * |  \\___|  __/|  \\| || |__ |  __/|    / *\n"
                + " *    \\____/\\____||_|\\__|\\____|\\____||_|\\_\\   *\n");

        System.out.println("Digite o patrimônio da sua máquina:");
        String patrimonio_digitado = leitor01.nextLine();

        System.out.println("Digite a senha:");
        String senha_digitada = leitor01.nextLine();

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Validação Log
        Maquina validMaquina = new Maquina(patrimonio_digitado, senha_digitada);// Adicionado Construtor na classe máquina]
        UsuarioDAO validacaoLog = new UsuarioDAO();// Executa-se a consulta ao banco referente ao método para instanciar objeto Maquina que servirá ara autenticação;

        ResultSet rsusariodaos = validacaoLog.autenticsacaoUsuario(validMaquina);// Nesta linha é instanciado objeto com parâmetros provenientes da consulta com a Azure
        LogTeste log = new LogTeste();

        try {
            log.login(rsusariodaos, rsusariodaos.next());
        } catch (SQLException ex) {
            System.out.println("Erro");
        }

        //--------------------------------------------------------------------------------------------------------
        try {
            // Objetos JDBC
            Conexao conexao = new Conexao();
            JdbcTemplate conMysql = conexao.getConnection();
            JdbcTemplate conAzure = conexao.getConnectionAzu();

            // Objetos Entidades
            InfoMaquina infoMaquina = new InfoMaquina();
            RegistroAtividade registroAtividade = new RegistroAtividade();
            Usb usb = new Usb();
            Componente componente = new Componente();
            ConfiguracaoComponente configComponente = new ConfiguracaoComponente();
            Metrica metrica = new Metrica();
            MetricaMouse metricaMouse = new MetricaMouse();

            // Objetos  Looca
            Looca looca = new Looca();
            Sistema sistema = looca.getSistema();
            Memoria memoria = looca.getMemoria();
            DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
            Processador processador = looca.getProcessador();

            // Retorno dos dados de patrimônio e senha
            String patrimonio_maquina = patrimonio_digitado;

            String senha_maquina = senha_digitada;

            Maquina maquina = new Maquina(patrimonio_maquina, senha_maquina);// Adicionado Construtor na classe máquina

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();// Executa-se a consulta ao banco referente ao método para instanciar objeto Maquina que servirá ara autenticação;
            ResultSet rsusariodao = objUsuarioDAO.autenticsacaoUsuario(maquina);// Nesta linha é instanciado objeto com parâmetros provenientes da consulta com a Azure

            if (rsusariodao.next()) {
                // LOGIN REALIZADO
                System.out.println("Login realizado!\nEnviando dados da máquina:");

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                //Setando Atributos
                maquina.setIdEmpresa(rsusariodao.getInt("idEmpresa"));
                maquina.setRazaoSocial(rsusariodao.getString("razaoSocial"));
                maquina.setCNPJ(rsusariodao.getString("CNPJ"));
                maquina.setEmail(rsusariodao.getString("email"));
                maquina.setTel(rsusariodao.getString("tel"));

                maquina.setNomeUsuario(rsusariodao.getString("nomeDoUsuario"));
                maquina.setFkEmpresa(rsusariodao.getInt("idEmpresa"));
                maquina.setIdMaquina(rsusariodao.getInt("idMaquina"));

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Tratamento de dados da tabela  InfoMaquina
                // Tamanho Total Disco
                Long tamanhoTotalDisco = grupoDeDiscos.getTamanhoTotal();
                double tamanhoTotalGB = tamanhoTotalDisco != null ? tamanhoTotalDisco / (1024 * 1024 * 1024.0) : 0.0;
                int tamanhoTotalFormatadoDisco = (int) Math.round(tamanhoTotalGB);

                // Tamanho Memoria Total
                Long memoriaTotal01 = looca.getMemoria().getTotal();
                long totalMemory1 = memoriaTotal01 != null ? memoriaTotal01 : 0L; // Verifica se a memória total é nula e atribui 0 em caso afirmativo
                int tamanhoTotalFormatadoMemoria = (int) Math.round((double) totalMemory1 / (1024 * 1024 * 1024));
                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert tabela Empresa
                List<Empresa> listadeEmpresa = conMysql.query("select * from Empresa where idEmpresa = ?",
                        new BeanPropertyRowMapper(Empresa.class), rsusariodao.getInt("idEmpresa"));// informações extraídas da consulta sqlserver realizadas em usuario DAO, preenchendo o bean property  com um construtor vazio disponível para receber o objeto específico do servidor.
                if (listadeEmpresa.isEmpty()) {
                    // Inset tabela Empresa
                    conMysql.update("INSERT INTO Empresa (idEmpresa, razaoSocial, CNPJ, email, tel) VALUES (?,?,?,?,?)",
                            maquina.getIdEmpresa(),
                            maquina.getRazaoSocial(),
                            maquina.getCNPJ(),
                            maquina.getEmail(),
                            maquina.getTel());
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert tabela Maquina
                List<Maquina> listaDeMaquina1 = conMysql.query("select * from Maquina where idMaquina = ?",
                        new BeanPropertyRowMapper(Maquina.class), rsusariodao.getInt("idMaquina"));
                if (listaDeMaquina1.isEmpty()) {

                    // Inset tabela Maquina
                    conMysql.update("INSERT INTO Maquina (idMaquina, nomeDoUsuario, patrimonio, senha, fkEmpresa) VALUES (?,?,?,?,?)",
                            maquina.getIdMaquina(),
                            maquina.getNomeUsuario(),
                            maquina.getPatrimonio(),
                            maquina.getSenha(),
                            maquina.getFkEmpresa());
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert tabela InfoMaquina Local
                List<Maquina> listaDeMaquina = conMysql.query("select * from InfoMaquina where fkMaquina = ?",
                        new BeanPropertyRowMapper(Maquina.class), rsusariodao.getInt("idMaquina"));
                if (listaDeMaquina.isEmpty()) {
                    conMysql.update("insert into InfoMaquina (sistemaoperacional, fabricante, arquitetura, nomeProcessador, capacidadeRam, capacidadeDisco, fkmaquina, fkempresa) values (?,?,?,?,?,?,?,?)",
                            sistema.getSistemaOperacional(),
                            sistema.getFabricante(),
                            sistema.getArquitetura(),
                            processador.getNome(),
                            tamanhoTotalFormatadoMemoria,
                            tamanhoTotalFormatadoDisco,
                            maquina.getIdMaquina(),
                            maquina.getFkEmpresa());
                } else {
                    conMysql.update("update InfoMaquina set sistemaoperacional=?, fabricante=?, arquitetura=?, nomeProcessador=?, capacidadeRam=?, capacidadeDisco=? where fkmaquina=?",
                            sistema.getSistemaOperacional(),
                            sistema.getFabricante(),
                            sistema.getArquitetura(),
                            processador.getNome(),
                            tamanhoTotalFormatadoMemoria,
                            tamanhoTotalFormatadoDisco,
                            maquina.getIdMaquina());

                }

                // Insert tabela InfoMaquina Azure
                List<Maquina> listaDeMaquinaAzure = conAzure.query("select * from InfoMaquina where fkMaquina = ?",
                        new BeanPropertyRowMapper(Maquina.class), rsusariodao.getInt("idMaquina"));
                if (listaDeMaquinaAzure.isEmpty()) {
                    conAzure.update("insert into InfoMaquina (sistemaoperacional, fabricante, arquitetura, nomeProcessador, capacidadeRam, capacidadeDisco, fkmaquina, fkempresa) values (?,?,?,?,?,?,?,?)",
                            sistema.getSistemaOperacional(),
                            sistema.getFabricante(),
                            sistema.getArquitetura(),
                            processador.getNome(),
                            tamanhoTotalFormatadoMemoria,
                            tamanhoTotalFormatadoDisco,
                            maquina.getIdMaquina(),
                            maquina.getFkEmpresa());

                } else {
                    conAzure.update("update InfoMaquina set sistemaoperacional=?, fabricante=?, arquitetura=?, nomeProcessador=?, capacidadeRam=?, capacidadeDisco=? where fkmaquina=?",
                            sistema.getSistemaOperacional(),
                            sistema.getFabricante(),
                            sistema.getArquitetura(),
                            processador.getNome(),
                            tamanhoTotalFormatadoMemoria,
                            tamanhoTotalFormatadoDisco,
                            maquina.getIdMaquina());
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert Tabela Usb
                LocalDateTime dataHoraAcesso = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                DispositivosUsbGrupo dispositivos = looca.getDispositivosUsbGrupo();

                List<DispositivoUsb> dispositivosConectados = dispositivos.getDispositivosUsbConectados();

                for (DispositivoUsb dispositivo : dispositivosConectados) {
                    usb.setNome(dispositivo.getNome());
                    usb.setFornecedor(dispositivo.getForncecedor());
                    conMysql.update("INSERT INTO Usb (nome,fornecedor,dataHora,fkmaquina,fkEmpresa) VALUES (?,?,?,?,?)",
                            usb.getNome(),
                            usb.getFornecedor(),
                            dataHoraAcesso,
                            maquina.getIdMaquina(),
                            maquina.getFkEmpresa()
                    );
                    conAzure.update("INSERT INTO Usb (nome,fornecedor,dataHora,fkmaquina,fkEmpresa) VALUES (?,?,?,?,?)",
                            usb.getNome(),
                            usb.getFornecedor(),
                            dataHoraAcesso,
                            maquina.getIdMaquina(),
                            maquina.getFkEmpresa()
                    );
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert Tabela RegistroAtividade
                conMysql.update("insert into RegistroAtividade(fkEmpresa, fkMaquina, inicializado) values (?,?,?)",
                        maquina.getFkEmpresa(),
                        maquina.getIdMaquina(),
                        dataHoraAcesso
                );

                conAzure.update("insert into RegistroAtividade(fkEmpresa, fkMaquina, inicializado) values (?,?,?)",
                        maquina.getFkEmpresa(),
                        maquina.getIdMaquina(),
                        dataHoraAcesso
                );
                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert Tabela Componente
                List<Componente> ListaComponente = conMysql.query("select*From Componente  join ConfiguracaoComponente on fkComponente = idComponente where fkMaquina = ?",
                        new BeanPropertyRowMapper(Maquina.class), rsusariodao.getInt("idMaquina"));
                if (ListaComponente.isEmpty()) {
                    //set 1
                    componente.setTipoCompenente("Disco");
                    componente.setIdComponente(1);

                    conMysql.update("insert into Componente(idComponente, tipoComponente) values (?,?)",
                            componente.getIdComponente(),
                            componente.getTipoCompenente()
                    );

                    //set 2
                    componente.setIdComponente(2);
                    componente.setTipoCompenente("Processador");

                    conMysql.update("insert into Componente(idComponente, tipoComponente) values (?,?)",
                            componente.getIdComponente(),
                            componente.getTipoCompenente()
                    );

                    //set 3
                    componente.setIdComponente(3);
                    componente.setTipoCompenente("Memoria");

                    conMysql.update("insert into Componente(idComponente, tipoComponente) values (?,?)",
                            componente.getIdComponente(),
                            componente.getTipoCompenente()
                    );
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Insert Tabela ConfiguracaoComponente 
                //set CPU
                componente.setIdComponente(1);
                configComponente.setUnidadeMedida("GB");
                conMysql.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        tamanhoTotalFormatadoDisco,
                        configComponente.getUnidadeMedida()
                );
                conAzure.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        tamanhoTotalFormatadoDisco,
                        configComponente.getUnidadeMedida()
                );
                //Tratando Dados Processador
                componente.setIdComponente(2);

                Long frequenciaLong = processador.getFrequencia();
                double frequenciaGHz = frequenciaLong != null ? frequenciaLong / 1e9 : 0.0;
                int frequenciaInt = (int) Math.round(frequenciaGHz);

                // Set Processador
                configComponente.setCapacidade(frequenciaInt);
                configComponente.setUnidadeMedida("GHz");
                conMysql.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        configComponente.getCapacidade(),
                        configComponente.getUnidadeMedida()
                );
                conAzure.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        configComponente.getCapacidade(),
                        configComponente.getUnidadeMedida()
                );
                //Tratando Dados Memoria
                Long memoriaTotal = looca.getMemoria().getTotal();
                long totalMemory = memoriaTotal != null ? memoriaTotal : 0L; // Verifica se a memória total é nula e atribui 0 em caso afirmativo
                int memoriaTotalInt = (int) Math.round((double) totalMemory / (1024 * 1024 * 1024));

                // Set Memoria
                componente.setIdComponente(3);
                configComponente.setCapacidade(memoriaTotalInt);
                configComponente.setUnidadeMedida("GB");
                conMysql.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        configComponente.getCapacidade(),
                        configComponente.getUnidadeMedida()
                );
                conAzure.update("insert into ConfiguracaoComponente(fkMaquina, fkEmpresa, FkComponente,capacidade,unidadeMedida) values (?,?,?,?,?)",
                        maquina.getIdMaquina(),
                        maquina.getFkEmpresa(),
                        componente.getIdComponente(),
                        configComponente.getCapacidade(),
                        configComponente.getUnidadeMedida()
                );

                // Selects com Resultados
                // Tabela Maquina
                List< RegistroAtividade> registroAtividades = conMysql.query("select * from RegistroAtividade order by idRegistroUsuario asc",
                        new BeanPropertyRowMapper(RegistroAtividade.class));
                System.out.println(registroAtividades);

                // Tabela InfoMaquina
                List< InfoMaquina> infoMaquinas = conMysql.query("select * from InfoMaquina order by idInfoMaquina asc",
                        new BeanPropertyRowMapper(InfoMaquina.class));
                System.out.println(infoMaquinas);

                //Tabela Maquina e Empresa
                List<Maquina> maquinas = conMysql.query("select * from Maquina join Empresa on fkempresa=idempresa order by idMaquina asc",
                        new BeanPropertyRowMapper(Maquina.class));
                System.out.println(maquinas);

                EnviaDados inicio = new EnviaDados();
                inicio.iniciarEnvio(maquina.getIdMaquina(), maquina.getFkEmpresa());

            } else {
                // ERRO NO LOGIN
                System.out.println("Erro ao realizar login!");
            }

        } catch (Exception erro) {
            System.out.println("Erro ao enviar dados para o banco");
        }
    }
}
