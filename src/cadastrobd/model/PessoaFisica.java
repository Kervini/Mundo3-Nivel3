package cadastrobd.model;

public class PessoaFisica extends Pessoa{
    private String cpf;
    
    public PessoaFisica(){
        super();
    }
    
    public PessoaFisica(int id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cpf){
        super(id, nome, logradouro, cidade, estado, telefone, email);
        this.cpf = cpf;
    }

    @Override
    public String exibir() {
        return super.exibir() + "\nCPF: " + cpf;
    }

    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf){
        this.cpf = cpf;
    }
}
