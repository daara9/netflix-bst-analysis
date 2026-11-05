// Integrantes: Danilo Oliveira Santos - RA 10410905
//              Miguel Batista da Silva - RA 10736250

public class NoBST {
    private ProgramaNetFlix dado;
    private NoBST esquerdo;
    private NoBST direito;

    public NoBST(ProgramaNetFlix dado) {
        this.dado = dado;
    }
    public ProgramaNetFlix getDado() { return dado; }
    public void setDado(ProgramaNetFlix dado) { this.dado = dado; }
    public NoBST getEsquerdo() { return esquerdo; }
    public void setEsquerdo(NoBST esquerdo) { this.esquerdo = esquerdo; }
    public NoBST getDireito() { return direito; }
    public void setDireito(NoBST direito) { this.direito = direito; }
}
