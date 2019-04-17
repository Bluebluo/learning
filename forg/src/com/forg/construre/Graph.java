package data.construre;

public class Graph {

	private int V;
	private int E;
	private Bag<Integer>[] adj;
	
	public Graph(int v) {
		this.V = v;
		this.E = 0;
		adj = new Bag[v];
		for (int i=0;i<V;i++) {
			adj[i] = new Bag<Integer>();
		}
	}

	public void addEdge(int v,int w) {
		adj[v].add(w);
		this.E ++;
	}
	
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
	
	public int N() {
		return V;
	}
}
