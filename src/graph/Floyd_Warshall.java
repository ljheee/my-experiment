package graph;

//弗洛伊德算法，插点法 没有负回权路就行
//以无向图G为入口，得出任意两点之间的路径长度dist[i][j]，路径path[i][j][k]，
//途中无连接得点距离用0表示，点自身也用0表示
//O(V^3) V vetex E edge
//可以用来求传递闭包 把所有的权值赋为1
//也可以用布尔求值 来算闭包
//仔细回想一下矩阵算法重复平方 O(V^3*lnV)的区别 
//一个是增加边（点V在里面）然后循环(V-1)次增长边为(V-1)，一个是遍历点 V在外面
//矩阵算法可能在并行计算机中能被很好地优化，因为矩阵乘法在并行计算机中有很多优化办法

//http://imlazy.ycool.com/post.1104393.html
//如果要你求一个网络中只有一条路径的最大流，应该用什么算法。（原题：http://online-judge.uva.es/p/v5/544.html）
//首先，我们通常用的最大流算法Ford-Fulkerson算法是不能用了。
//然而，我们可以用最短路径的算法，Dijkstra或Floyd-Warshall加以修改，
//来解决这个问题。因为这个问题实际就是要找出一条容量最大的路径。
//但是我们同时也知道另一件事，很多人初学的时候都会通过把Dijkstra和Floyd-Warshall里的小于号改成大于号来求最长路径。
//结果显然是错误的，因为这样的算法算出的最长路径中会有重复的边。为什么，很简单，
//Dijkstra和Floyd-Warshall里都无法限制重复边的出现，但是在最短路径中，如果一条路径有重复的边，
//那它肯定不会是最短路径，所以重复边被问题本身的性质剃除了。但是如果用来求最长路径，
//那么越有重复的边，路径就越长，就越满足要求，则最终的结果就会错误了。
//所以如果要看一个问题能不能用Dijkstra和Floyd-Warshall来解决，
//只要看有重复边的路径是否会比没有重复边有路径更符合问题的要求。
//比如求容量最大的路径时，重复的边不可能使容量变大，因为容量取决于路径中容量最小的一条边。
//还有一个同类的问题：用2种颜色可不可能给一张地图着色，使每两个相邻的国家颜色都不同。
//解法是看一个无向图中存不存在边数为奇数的圈。同样，在这个问题里，
//有重复边有路径不可能改变原有的解，所以可以用Dijkstra或Floyd-Warshall来解。

//http://hi.baidu.com/qlyzpqz/item/d006b6092eb18b1eeafe38ab
//FLOD不能用于求最长路径,但能把W取反来取最短路径求最长,但如果有负权环路,会出错误的结果,用BELLMAN_FORD来检测
/**
 * 
 * Will Floyd-Warshall2( ) correctly find the length of the longest path between
 * each pair of vertices? Why or why not?
 * 
 * 
 * Solution: NO. It will not. It does not correctly treat cycles, even positive
 * weight cycles for graphs whose edge weights are all positive. In fact, the
 * All-Pairs-Longest-Paths problem is NP-complete, even for simple paths on
 * graphs with only positive edge weights.
 */
// 自我总结:Floyd-Warshall可以通过取反取最短求最长,直接改变大小于符号是也是行的
// 之所以网上说不行,是因为存在正权环路,实行情况中很常出现,而同样求最短时,也不能存在负环权路(这个很少出现,因为大多权重全为正)
// 利用了动态规划里的最优子结构,最短路径,最长路径皆是,但最短简单路径和最长简单路径皆不是,只要存在正权环和负权环
// 所以FLOYD可以求最短路径,最长路径,因为是保证没负(正)权环路的前提下,其实都求出了最短简单路径和最长简单路径
public class Floyd_Warshall
{
	int[][]		dist	= null; // 任意两点之间路径长度
	int[][][]	path	= null; // 任意两点之间的路径

	/**
	 * http://59.73.198.250/bbs/viewthread.php?tid=359
	 */
	public Floyd_Warshall(int[][] G)
	{
		int MAX = Integer.MAX_VALUE;
		MAX = Integer.MIN_VALUE;
		int row = G.length;// 图G的行数
		int[][] spot = new int[row][row];// 定义任意两点之间经过的点
		int[] onePath = new int[row];// 记录一条路径
		dist = new int[row][row];
		path = new int[row][row][];
		for (int i = 0; i < row; i++)
			// 处理图两点之间的路径
			for (int j = 0; j < row; j++) {
				if (G[i][j] == 0) {
					G[i][j] = MAX;// 没有路径的两个点之间的路径为默认最大
				}
				if (i == j) {
					G[i][j] = 0;// 本身的路径长度为0
				}
			}
		for (int i = 0; i < row; i++) {
			// 初始化为任意两点之间没有路径
			for (int j = 0; j < row; j++) {
				spot[i][j] = -1;
			}
		}
		for (int i = 0; i < row; i++) {
			// 假设任意两点之间的没有路径
			onePath[i] = -1;
		}
		for (int v = 0; v < row; ++v) {
			for (int w = 0; w < row; ++w) {
				dist[v][w] = G[v][w];
			}
		}
		for (int u = 0; u < row; ++u) {
			for (int v = 0; v < row; ++v) {
				for (int w = 0; w < row; ++w) {
					// 防止溢出
					if (dist[v][u] != MAX && dist[u][w] != MAX && dist[v][w] < dist[v][u] + dist[u][w]) {
						dist[v][w] = dist[v][u] + dist[u][w];// 如果存在更短路径则取更短路径
						spot[v][w] = u;// 把经过的点加入
					}
				}
			}

			System.out.println(" *************** ********" + u + " *************** ********");
			for (int i = 0; i < row; i++) {
				// 求出所有的路径
				int[] point = new int[1];
				for (int j = 0; j < row; j++) {
					point[0] = 0;
					onePath[point[0]++] = i;
					outputPath(spot, i, j, onePath, point);
					path[i][j] = new int[point[0]];
					for (int s = 0; s < point[0]; s++) {
						path[i][j][s] = onePath[s];
					}
				}
			}

			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist[i].length; j++) {
					System.out.println();
					System.out.print("From " + i + " to " + j + " path is: ");
					for (int k = 0; k < path[i][j].length; k++) {
						System.out.print(path[i][j][k] + "->");
					}
					System.out.println();
					System.out.println("From " + i + " to " + j + " length :" + dist[i][j]);
				}
			}

		}

		// 上面是精简形式，把空间从O(V^3)节省到O(V^2)
		// int[][][] distance = new int[row][row][row];
		// int i, j, k;
		// for (i = 1; i <= row; i++) {
		// for (j = 1; j <= row; j++) {
		// distance[i][j][0] = G[i][j];
		// }
		// }
		// for (k = 1; k <= row; k++) {
		// for (i = 1; i <= row; i++) {
		// for (j = 1; j <= row; j++) {
		// distance[i][j][k] = distance[i][j][k - 1];
		// if (distance[i][k][k - 1] + distance[k][j][k - 1] <
		// distance[i][j][k])
		// distance[i][j][k] = distance[i][k][k - 1] + distance[k][j][k - 1];
		// }
		// }
		// }

		for (int i = 0; i < row; i++) {
			// 求出所有的路径
			int[] point = new int[1];
			for (int j = 0; j < row; j++) {
				point[0] = 0;
				onePath[point[0]++] = i;
				outputPath(spot, i, j, onePath, point);
				path[i][j] = new int[point[0]];
				for (int s = 0; s < point[0]; s++) {
					path[i][j][s] = onePath[s];
				}
			}
		}

	}

	// 求强通量分量
	// O(v^3)
	void scc(int[][] g)
	{
		int n = g.length;
		int i, j, k;
		int nr_scc = 0;
		int[] v_id = new int[n];
		for (i = 1; i <= n; ++i) {
			g[i][i] = 1;
		}
		for (i = 1; i <= n; ++i) {
			for (j = 1; j <= n; ++j) {
				g[i][j] = g[i][j] > 0 ? 1 : 0;
			}
		}
		for (k = 1; k <= n; ++k) {
			for (i = 1; i <= n; ++i) {
				for (j = 1; j <= n; ++j) {
					g[i][j] = Math.max(g[i][j], g[i][k] & g[k][j]); // floyd
				}
			}
		}
		for (i = 1; i <= n; ++i) {
			for (j = i; j <= n; ++j) {
				if ((g[i][j] & g[j][i]) == 1) {
					v_id[j] = v_id[i] = (v_id[i] == 0 ? ++nr_scc : v_id[i]);
				}
			}
		}
	}

	void outputPath(int[][] spot, int i, int j, int[] onePath, int[] point)
	{
		// 输出i 到j 的路径的实际代码，point[]记录一条路径的长度
		if (i == j) {
			return;
		}
		if (spot[i][j] == -1) {
			onePath[point[0]++] = j;
		}
		// System.out.print(" "+j+" ");
		else {
			outputPath(spot, i, spot[i][j], onePath, point);
			outputPath(spot, spot[i][j], j, onePath, point);
		}
	}

	public static void main(String[] args)
	{
		int data[][] = { { 0, 27, 44, 17, 11, 27, 42, 0, 0, 0, 20, 25, 21, 21, 18, 27, 0 },// x1
				{ 27, 0, 31, 27, 49, 0, 0, 0, 0, 0, 0, 0, 52, 21, 41, 0, 0 },// 1
				{ 44, 31, 0, 19, 0, 27, 32, 0, 0, 0, 47, 0, 0, 0, 32, 0, 0 },// 2
				{ 17, 27, 19, 0, 14, 0, 0, 0, 0, 0, 30, 0, 0, 0, 31, 0, 0 },// 3
				{ 11, 49, 0, 14, 0, 13, 20, 0, 0, 28, 15, 0, 0, 0, 15, 25, 30 },// 4
				{ 27, 0, 27, 0, 13, 0, 9, 21, 0, 26, 26, 0, 0, 0, 28, 29, 0 },// 5
				{ 42, 0, 32, 0, 20, 9, 0, 13, 0, 32, 0, 0, 0, 0, 0, 33, 0 },// 6
				{ 0, 0, 0, 0, 0, 21, 13, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0 },// 7
				{ 0, 0, 0, 0, 0, 0, 0, 19, 0, 11, 20, 0, 0, 0, 0, 33, 21 },// 8
				{ 0, 0, 0, 0, 28, 26, 32, 0, 11, 0, 10, 20, 0, 0, 29, 14, 13 },// 9
				{ 20, 0, 47, 30, 15, 26, 0, 0, 20, 10, 0, 18, 0, 0, 14, 9, 20 },// 10
				{ 25, 0, 0, 0, 0, 0, 0, 0, 0, 20, 18, 0, 23, 0, 0, 14, 0 },// 11
				{ 21, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 27, 22, 0, 0 },// 12
				{ 21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0 },// 13
				{ 18, 41, 32, 31, 15, 28, 0, 0, 0, 29, 14, 0, 22, 0, 0, 11, 0 },// 14
				{ 27, 0, 0, 0, 25, 29, 33, 0, 33, 14, 9, 14, 0, 0, 11, 0, 9 },// 15
				{ 0, 0, 0, 0, 30, 0, 0, 0, 21, 13, 20, 0, 0, 0, 0, 9, 0 } // 16
		};
		data = new int[5][5];
		data[0][2] = 1;
		data[0][3] = 6;
		data[1][0] = 2;
		data[2][3] = 3;
		data[4][0] = 5;
		data[4][1] = 2;
		// for (int i = 0; i < data.length; i++) {
		// for (int j = i; j < data.length; j++) {
		// if (data[i][j] != data[j][i]) {
		// return;
		// }
		// }
		// }
		Floyd_Warshall test = new Floyd_Warshall(data);
		for (int i = 0; i < data.length; i++) {
			for (int j = i; j < data[i].length; j++) {
				System.out.println();
				System.out.print("From " + i + " to " + j + " path is: ");
				for (int k = 0; k < test.path[i][j].length; k++) {
					System.out.print(test.path[i][j][k] + " ");
				}
				System.out.println();
				System.out.println("From " + i + " to " + j + " length :" + test.dist[i][j]);
			}
		}
	}
}