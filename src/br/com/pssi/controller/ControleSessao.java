package br.com.pssi.controller;

import br.com.pssi.model.Usuario;

public class ControleSessao {
    private static Usuario usuarioLogado;

    public static void iniciarSessao(Usuario usuario) { usuarioLogado = usuario; }
    public static void encerrarSessao() { usuarioLogado = null; }
    public static Usuario getUsuarioLogado() { return usuarioLogado; }
    public static boolean isAutenticado() { return usuarioLogado != null; }
}