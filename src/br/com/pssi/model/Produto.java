package br.com.pssi.model;

public class Produto {
    private int id;
    private String codigoBarras;
    private String nome;
    private int quantidade;
    private double precoCusto;
    private double precoVenda;
    private String categoria;

    public Produto() {}

    public Produto(int id, String codigoBarras, String nome, int quantidade, double precoCusto, double precoVenda, String categoria) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}