package cadastrobd.control;

import cadastro.model.PessoaFisicaDAO;
import cadastro.model.PessoaJuridicaDAO;
import cadastrobd.model.Pessoa;
import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaJuridica;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CadastroBDTeste {
    
    private static final Scanner entrada = new Scanner(System.in);
    
    public static void main(String[] args) {
        PessoaFisicaDAO repoFisica = null;
        PessoaJuridicaDAO repoJuridica = null;
        
        boolean conectado = false;
        
        do {
            try {
                repoFisica = new PessoaFisicaDAO();
                repoJuridica = new PessoaJuridicaDAO();
                conectado = true;
            } catch (SQLException ex) {
                System.out.println("Erro de conexao: " + ex.getMessage());
                System.out.println("Pressione enter para tentar conectar novamente.");
                entrada.nextLine();
            }
        } while (!conectado);
        
        boolean continuar = true;
        String opcao;
        do {
            System.out.println("\n============================");
            System.out.println(" --- MENU --- ");
            System.out.println("1 - para incluir");
            System.out.println("2 - para alterar");
            System.out.println("3 - para excluir");
            System.out.println("4 - buscar pessoa");
            System.out.println("5 - exibir todos");
            System.out.println("0 - para finalizar");
            opcao = entrada.nextLine().trim();
            
            OUTER:
            switch (opcao) {
                case "1" -> {
                    System.out.println("\nIncluindo...");
                    System.out.println("F - para pessoa fisica. J - para pessoa juridica.");
                    String tipo = entrada.nextLine().toUpperCase().trim();
                    switch (tipo) {
                        case "F" -> {
                            PessoaFisica pf = new PessoaFisica();
                            setarPessoa(pf);
                            
                            System.out.println("Digite o CPF: ");
                            String valor = entrada.nextLine();
                            pf.setCpf(valor.substring(0, Math.min(valor.length(), 11)));//Truncando a stringpro tamanho do banco. O metodo math.min retorna
                                                                                        //o valor atual se o tamanho já for menor que o especificado
                            
                            try {
                                if (repoFisica.incluir(pf)) {
                                    System.out.println("Cadastrado com sucesso!");
                                } else {
                                    System.out.println("Nao foi possível realizar o cadastro!");
                                }
                                
                            } catch (SQLException ex) {
                                System.out.println("Falha de conexao.\n" + ex.getMessage());
                                ///testar o fluxo
                                break OUTER;
                            }
                        }
                        case "J" -> {
                            PessoaJuridica pj = new PessoaJuridica();
                            setarPessoa(pj);
                            
                            System.out.println("Digite o CNPJ: ");
                            String valor = entrada.nextLine();
                            pj.setCnpj(valor.substring(0, Math.min(valor.length(), 14)));
                            
                            try {
                                if (repoJuridica.incluir(pj)) {
                                    System.out.println("Cadastrado com sucesso!");
                                } else {
                                    System.out.println("Nao foi possível realizar o cadastro!");
                                }
                                
                            } catch (SQLException ex) {
                                System.out.println("Falha de conexao.\n" + ex.getMessage());
                                ///testar o fluxo
                                break OUTER;
                            }
                        }
                        default -> {
                            System.out.println("Tipo invalido! Tente novamente.");
                            break OUTER;
                        }
                    }
                }
                case "2" -> {
                    System.out.println("\nAlterando...");
                    System.out.println("F - para pessoa fisica. J - para pessoa juridica.");
                    String tipo = entrada.nextLine().toUpperCase().trim();
                    
                    switch (tipo) {
                        case "F" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                PessoaFisica pf = repoFisica.getPessoa(id);
                                if (pf == null) {
                                    System.out.println("ID nao encontrado!");
                                    break OUTER;
                                }
                                System.out.println(pf.exibir()+"\n");
                                
                                setarPessoa(pf);
                                
                                System.out.println("Digite o CPF: ");
                                String valor = entrada.nextLine();
                                pf.setCpf(valor.substring(0, Math.min(valor.length(), 11)));
                                
                                if (repoFisica.alterar(pf)) {
                                    System.out.println("Alterado com sucesso!");
                                } else {
                                    System.out.println("Registro nao encontrado! Nao foi possivel alterar.");
                                }
                                
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas numeros.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexão..." + e.getMessage());
                            }
                            
                        }
                        case "J" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                PessoaJuridica pj = repoJuridica.getPessoa(id);
                                if (pj == null) {
                                    System.out.println("ID nao encontrado!");
                                    break OUTER;
                                }
                                System.out.println(pj.exibir()+"\n");
                                
                                setarPessoa(pj);
                                System.out.println("Digite o CNPJ: ");
                                String valor = entrada.nextLine();
                                pj.setCnpj(valor.substring(0, Math.min(valor.length(), 14)));
                                
                                if (repoJuridica.alterar(pj)) {
                                    System.out.println("Alterado com sucesso!");
                                } else {
                                    System.out.println("Registro nao encontrado! Não foi possivel alterar.");
                                }
                                
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas numeros.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        default -> {
                            System.out.println("Tipo invalido! Tente novamente.");
                            break OUTER;
                        }
                    }
                }
                case "3" -> {
                    System.out.println("\nExcluindo...");
                    System.out.println("F - para pessoa fisica. J - para pessoa juridica.");
                    String tipo = entrada.nextLine().toUpperCase().trim();
                    
                    switch (tipo) {
                        case "F" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                
                                if (repoFisica.excluir(id)) {
                                    System.out.println("Excluido com sucesso!");
                                } else {
                                    System.out.println("Registro nao encontrado! Nao foi possivel excluir.");
                                }
                                
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas numeros.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        case "J" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                
                                if (repoJuridica.excluir(id)) {
                                    System.out.println("Excluido com sucesso!");
                                } else {
                                    System.out.println("Registro nao encontrado! Nao foi possivel excluir.");
                                }
                                
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas numeros.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        default -> {
                            System.out.println("Tipo invalido! Tente novamente.");
                            break OUTER;
                        }
                    }
                }
                case "4" -> {
                    System.out.println("\nBuscando...");
                    System.out.println("F - para pessoa fisica. J - para pessoa juridica.");
                    String tipo = entrada.nextLine().toUpperCase().trim();
                    
                    switch (tipo) {
                        case "F" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                PessoaFisica pf = repoFisica.getPessoa(id);
                                if (pf == null) {
                                    System.out.println("ID nao encontrado!");
                                    break OUTER;
                                }
                                System.out.println(pf.exibir());
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas números.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        case "J" -> {
                            System.out.println("Digite o ID: ");
                            try {
                                int id = entrada.nextInt();
                                entrada.nextLine();
                                PessoaJuridica pj = repoJuridica.getPessoa(id);
                                if (pj == null) {
                                    System.out.println("ID nao encontrado!");
                                    break OUTER;
                                }
                                System.out.println(pj.exibir());
                            } catch (InputMismatchException e) {
                                entrada.nextLine();
                                System.out.println("ID invalido, digite apenas numeros.");
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        default -> {
                            System.out.println("Tipo invalido! Tente novamente.");
                            break OUTER;
                        }
                    }
                }
                case "5" -> {
                    System.out.println("\nListando...");
                    System.out.println("F - para pessoa fisica. J - para pessoa juridica.");
                    String tipo = entrada.nextLine().toUpperCase().trim();
                    
                    switch (tipo) {
                        case "F" -> {
                            try {
                                ArrayList<PessoaFisica> pessoas = repoFisica.getPessoas();
                                System.out.println("============================");
                                System.out.println("Lista de pessoas fisicas\n");
                                for (PessoaFisica pf : pessoas) {
                                    System.out.println(pf.exibir() + "\n");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        case "J" -> {
                            try {
                                ArrayList<PessoaJuridica> pessoas = repoJuridica.getPessoas();
                                System.out.println("============================");
                                System.out.println("Lista de pessoas juridicas\n");
                                for (PessoaJuridica pj : pessoas) {
                                    System.out.println(pj.exibir() + "\n");
                                }
                            } catch (SQLException e) {
                                System.out.println("Erro de conexao..." + e.getMessage());
                            }
                        }
                        default -> {
                            System.out.println("Tipo invalido! Tente novamente.");
                            break OUTER;
                        }
                    }
                }
                case "0" -> {
                    System.out.println("\nFinalizando....");
                    try {
                        repoFisica.fecharConexao();
                        repoJuridica.fecharConexao();
                    } catch (SQLException e) {
                        System.out.println("Erro de conexao..." + e.getMessage());
                    } finally {
                        continuar = false;
                    }
                }
                default -> {
                    System.out.println("Opcao invalida! Tente novamente");
                    break;
                }
            }
        } while (continuar);
    }
    
    public static void setarPessoa(Pessoa p) {
        String valor;
        
        System.out.println("Digite o nome: ");
        valor = entrada.nextLine();
        p.setNome(valor.substring(0, Math.min(valor.length(), 150)));//Limita os caracteres ao tamanho no banco
        System.out.println("Digite o logradouro: ");
        valor = entrada.nextLine();
        p.setLogradouro(valor.substring(0, Math.min(valor.length(), 100)));
        System.out.println("Digite a cidade: ");
        valor = entrada.nextLine();
        p.setCidade(valor.substring(0, Math.min(valor.length(), 100)));
        System.out.println("Digite o estado: ");
        valor = entrada.nextLine();
        p.setEstado(valor.substring(0, Math.min(valor.length(), 2)));
        System.out.println("Digite o telefone: ");
        valor = entrada.nextLine();
        p.setTelefone(valor.substring(0, Math.min(valor.length(), 11)));
        System.out.println("Digite o e-mail: ");
        valor = entrada.nextLine();
        p.setEmail(valor.substring(0, Math.min(valor.length(), 150)));
    }

//método main do 1º Procedimento
//    public static void main(String[] args) {
//        try {
//            //instância os repositórios para persistência de dados
//            PessoaFisicaDAO repoFisica =  new PessoaFisicaDAO();
//            PessoaJuridicaDAO repoJuridica = new PessoaJuridicaDAO();
//            
//            //Instânciar um objeto de Pessoa Fisíca e incluir no banco
//            PessoaFisica pf = new PessoaFisica(0, "Kervini", "07 de Setembro", "Rincão",
//                    "SP", "996045313", "202301206073@alunos.estacio.br", "12345678910");
//
//            if (repoFisica.incluir(pf)) {
//                System.out.println("Cadastrado com sucesso!");
//            } else {
//                System.out.println("Nao foi possivel realizar o cadastro!");
//            }
//
//            //alterando um registro existente do banco
//            PessoaFisica pf2 = new PessoaFisica(20, "teste", "Rua do meio", "Aquela",
//                    "SP", "987654312", "teste@email.com", "12345678910");
//
//            if (repoFisica.alterar(pf2)) {
//                System.out.println("Alterado com sucesso!");
//            } else {
//                System.out.println("Pessoa nao encontrada!");
//            }
//
//            //Listar todas as pessoas fisicas do banco
//            ArrayList<PessoaFisica> pessoasfisicas = repoFisica.getPessoas();
//
//            for (PessoaFisica pessoaf : pessoasfisicas) {
//                System.out.println(pessoaf.exibir() + "\n");
//            }
//
//            //excluir uma pessoa fisica
//            if (repoFisica.excluir(11)) {
//                System.out.println("Excluido com sucesso!");
//            } else {
//                System.out.println("Pessoa não encontrada!");
//            }
//
//            //Instânciar uma pessoa juridica e persistir no banco
//            PessoaJuridica pj = new PessoaJuridica(0, "Codificando trabalhos", 
//                    "Rua da minha empresa", "String", "BD", "0101010101", "cript@email.com", "010101010101");
//
//            if (repoJuridica.incluir(pj)) {
//                System.out.println("Cadastrado com sucesso!");
//            } else {
//                System.out.println("Não foi possível cadastrar a empresa!");
//            }
//
//            //alterando os dados de uma pessoa juridica
//            PessoaJuridica pj2 = new PessoaJuridica(12, "Novo nome fantasia", 
//                    "Rua bela", "Varchar", "MG", "0202020202", "criptomoedas@email.com", "0202020203");
//
//            if (repoJuridica.alterar(pj2)) {
//                System.out.println("Dados da empresa alterados com sucesso!");
//            } else {
//                System.out.println("Nao foi possivel alterar os dados!");
//            }
//
//            //Listar todas as pessoas juridicas do banco
//            ArrayList<PessoaJuridica> pessoasjuridicas = repoJuridica.getPessoas();
//
//            for (PessoaJuridica pessoaj : pessoasjuridicas) {
//                System.out.println(pessoaj.exibir() + "\n");
//            }
//
//            //excluindo uma pessoa juridica do banco
//            if (repoJuridica.excluir(12)) {
//                System.out.println("Excluido com sucesso!");
//            } else {
//                System.out.println("Pessoa não encontrada!");
//            }
//
//        } catch (SQLException ex) {
//            //Captura todas as exceções do tipo SQLExcpetion, que normalmente são por falha de conexão
//            System.out.println("Falha de conexão com o banco de dados.\n" + ex.getMessage());
//        }
//    }
}
