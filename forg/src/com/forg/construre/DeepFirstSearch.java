package data.construre;

public class DeepFirstSearch extends Graph{
	
	private boolean[] marked;
	private int[] edgeTo;
	private int count = 0;
	
	public DeepFirstSearch(int v) {
		super(v);
		this.marked = new boolean[v];
		this.edgeTo = new int[v];
		// TODO Auto-generated constructor stub
	}

	
	//深度优先搜索树
	public void dfs(Graph g,int k) {
		//System.out.println(k+"节点访问状态"+marked[k]);
		marked[k] = true;
		count ++;
		//System.out.println("现在访问了"+count+"个节点."+"全局访问结果"+ (count < g.N()));
		if(count < g.N()) {
			for (int w:g.adj(k)) {
		//		System.out.println("k="+k+"   w="+w+"    "+w+"节点访问状态"+marked[w]+"   ");
				if(!marked[w]) {
					edgeTo[w] = k;
					System.out.println("edgeTo["+w+"]"+"="+k);
					dfs(g, w);
				}
			}
		}
	}
	
	
	public boolean hasPathTo(int v) {
		return marked[v];
	}
	
	public Iterable<Integer> pathTo(int v){
		if(!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for(int x = v ;x != 0;x = edgeTo[x]) {
			path.push(x);
		}
		return path;
	}
	
	public static void main(String[] args) {
		Graph graph = new Graph(6);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(0, 3);
		graph.addEdge(1, 2);
		graph.addEdge(1, 4);
		graph.addEdge(1, 5);
		graph.addEdge(1, 0);
		graph.addEdge(2, 1);
		graph.addEdge(2, 0);
		graph.addEdge(3, 4);
		graph.addEdge(3, 0);
		graph.addEdge(4, 5);
		graph.addEdge(4, 3);
		graph.addEdge(4, 1);
		graph.addEdge(5, 4);
		graph.addEdge(5, 1);
		DeepFirstSearch dFirstSearch = new DeepFirstSearch(6);
		dFirstSearch.dfs(graph, 0);
		for(int v = 0;v<graph.N();v++) {
			System.out.print('0' +  " to "+v+" : " +'0');
			if(dFirstSearch.hasPathTo(v)) {
				for( int s: dFirstSearch.pathTo(v)) {
					if(s == v) {
						System.out.print("-" + s);
						break;
					}else {
						System.out.print("-" + s);
					}
				}
				System.out.println();
			}
		}
	}
	
	
}
