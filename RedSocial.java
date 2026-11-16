import java.util.ArrayList;
import java.util.List;

/**
 * APE 4 - Fase 1: Reto de grafos y vertices.
 * Red social de 6 vertices y 5 aristas (grafo no dirigido) con:
 *  - al menos un ciclo, - un vertice aislado, - un vertice de grado >= 3.
 */
public class RedSocial {

    private final String[] nombres;
    private final List<List<Integer>> adyacencia = new ArrayList<>();
    private int totalAristas = 0;

    public RedSocial(String... nombres) {
        this.nombres = nombres;
        for (int i = 0; i < nombres.length; i++) {
            adyacencia.add(new ArrayList<>());
        }
    }

    public void agregarArista(int u, int v) {
        adyacencia.get(u).add(v);
        adyacencia.get(v).add(u);
        totalAristas++;
    }

    public int grado(int v) {
        return adyacencia.get(v).size();
    }

    public int[][] matrizAdyacencia() {
        int n = nombres.length;
        int[][] m = new int[n][n];
        for (int u = 0; u < n; u++) {
            for (int v : adyacencia.get(u)) {
                m[u][v] = 1;
            }
        }
        return m;
    }

    // Deteccion de ciclo en grafo no dirigido (DFS recordando al padre)
    public boolean tieneCiclo() {
        boolean[] visitado = new boolean[nombres.length];
        for (int i = 0; i < nombres.length; i++) {
            if (!visitado[i] && dfsCiclo(i, -1, visitado)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCiclo(int u, int padre, boolean[] visitado) {
        visitado[u] = true;
        for (int v : adyacencia.get(u)) {
            if (!visitado[v]) {
                if (dfsCiclo(v, u, visitado)) return true;
            } else if (v != padre) {
                return true; // arista hacia un vertice ya visitado que no es el padre
            }
        }
        return false;
    }

    public int componentesConexas() {
        boolean[] visitado = new boolean[nombres.length];
        int componentes = 0;
        for (int i = 0; i < nombres.length; i++) {
            if (!visitado[i]) {
                componentes++;
                dfs(i, visitado);
            }
        }
        return componentes;
    }

    private void dfs(int u, boolean[] visitado) {
        visitado[u] = true;
        for (int v : adyacencia.get(u)) {
            if (!visitado[v]) dfs(v, visitado);
        }
    }

    public void imprimirResumen() {
        int n = nombres.length;
        System.out.println("Vertices: " + n + " | Aristas: " + totalAristas);

        System.out.println("\nLista de adyacencia y grado:");
        int sumaGrados = 0;
        for (int u = 0; u < n; u++) {
            StringBuilder sb = new StringBuilder(nombres[u] + " -> ");
            for (int v : adyacencia.get(u)) sb.append(nombres[v]).append(" ");
            System.out.println(sb + "(grado " + grado(u) + ")");
            sumaGrados += grado(u);
        }
        System.out.println("Suma de grados = " + sumaGrados + " = 2 x " + totalAristas);

        System.out.println("\nMatriz de adyacencia:");
        int[][] m = matrizAdyacencia();
        for (int u = 0; u < n; u++) {
            StringBuilder fila = new StringBuilder();
            for (int v = 0; v < n; v++) fila.append(m[u][v]).append(" ");
            System.out.println(fila + " " + nombres[u]);
        }

        System.out.println("\nVerificacion de requisitos:");
        System.out.println("Tiene ciclo: " + tieneCiclo());
        for (int u = 0; u < n; u++) {
            if (grado(u) == 0) System.out.println("Vertice aislado: " + nombres[u]);
            if (grado(u) >= 3) System.out.println("Vertice de grado >= 3: " + nombres[u] + " (grado " + grado(u) + ")");
        }
        System.out.println("Componentes conexas: " + componentesConexas());
    }

    public static void main(String[] args) {
        // 0=Ana 1=Bruno 2=Carla 3=Diego 4=Elena 5=Fabian
        RedSocial red = new RedSocial("Ana", "Bruno", "Carla", "Diego", "Elena", "Fabian");
        red.agregarArista(0, 1); // Ana - Bruno
        red.agregarArista(1, 2); // Bruno - Carla
        red.agregarArista(2, 0); // Carla - Ana   (cierra el ciclo Ana-Bruno-Carla)
        red.agregarArista(0, 3); // Ana - Diego
        red.agregarArista(3, 4); // Diego - Elena
        // Fabian no tiene aristas: vertice aislado
        red.imprimirResumen();
    }
}