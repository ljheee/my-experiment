package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 最小生成树的Kruskal算法(以无向图为例) */
/** 最小生成树是最小权值生成树，并不是边最小化，因为生成树的边都固定为V-1 */
public class MaxFlow
{
	public static Map<Ve, Integer>	vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>			EdgeQueue	= new ArrayList<E>();
	public static List<E>			EdgeList	= new ArrayList<E>();
	public static int				MAX			= Integer.MAX_VALUE;
	public static int				edgeCnt;
	public static int				vertexCnt	= edgeCnt = 0;					// 点数，边数

	public static int				MAXN		= 10;
	public static int[][]			capacity;
	public static int[][]			flow;
	public static int[]				minCapacity;
	public static int[]				father;
	public static int				maxFlow;
	public static boolean[]			visit;
	public static int[]				level;
	public static int[]				gap;
	public static int[]				nextV;

	// Ford-Fullkerson方法 Method
	// 使用BFS来实现Ford-Fullkerson方法中的找增广路径的算法称为Edmonds-Karp算法。
	// Edmonds-Karp算法是最短增广路算法，因为实用BFS找到的增广路径是所有可能的增广路径中最短的路径。它的复杂度是O(VE^2)，其中V是结点数，E是有向边数。
	// 如果用使用DFS代替BFS，则Ford-Fullkerson方法退化成一般增广路算法。其复杂度是O(E| f* |)。其中f*是算法找出的最大流。
	// 最大流的Edmonds-Karp算法 Algorithm
	// S: Start T: Terminal
	public static int Edmonds_Karp(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		while (true) {
			minCapacity = new int[MAXN];// 每次寻找增广路径都将每个点的流入容量置为0
			visit = new boolean[MAXN];// 标记一个点是否已经压入队列
			minCapacity[S] = MAX;// 源点的容量置为正无穷
			queue.offer(S);// 将源点加入队列
			while (!queue.isEmpty()) { // 当队列不为空
				u = queue.peek();
				queue.poll();
				for (v = 1; v <= vertexCnt; v++) {
					if (!visit[v] && capacity[u][v] > 0) {
						visit[v] = true;
						father[v] = u;// 记录下他的父亲方便往后的正反向更新
						queue.offer(v);
						// 当前点的容量为父亲点容量与边流量的较小者
						// 一边找最短路径 一边更新这条路径上的最小残余流量(min residual capacity)
						// 通过数组传递MIN CAPACITY NICE
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					}
				}
				// 找到增广路径(augmenting path)[残留网络中S到T的简单路径]
				if (minCapacity[T] > 0) {// 如果找到了汇点并且汇点容量不为0则清空队列。
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[T] == 0) {
				// 经过BFS 不能达到终点T 即找不到增广路径(augmenting path)[残留网络中S到T的简单路径]
				break;
			}
			for (int i = T; i != 1; i = father[i]) {
				capacity[father[i]][i] -= minCapacity[T];// 正向更新
				capacity[i][father[i]] += minCapacity[T];// 反向更新
				flow[father[i]][i] += minCapacity[T]; // 更新FLOW表
			}
			maxFlow += minCapacity[T];// 更新最大流
			// 如果Maxflow 可以认为没有流
		}
		return maxFlow;
	}

	public static void main(String[] args)
	{
		buildCap();
		System.out.println(Edmonds_Karp(1, 4));
		buildCap();
		System.out.println(Dinic(1, 4));
		buildCap();
		System.out.println(Dinic2(1, 4));
		buildCap();
		System.out.println(ISAP2(1, 4));
	}

	public static boolean BFS4Dinic(int s, int t)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		level = new int[vertexCnt + 1];
		queue.offer(s);
		level[s] = 1;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.poll();
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] == 0) {
					level[v] = level[u] + 1;
					queue.offer(v);
					if (v == t) { // 找到T就行了 其他相同层次的可以不用管了
						return level[t] != 0; // 其实可以直接返回TRUE
					}
				}
			}
		}
		return level[t] != 0;
	}

	// Dinic是在Edmond_Karp上的优化，就是传说中的分层；分层听起来难理解，其实就是分级即给节点具有一定规则的标记，看一下实现就懂了！
	// 算法的实现步骤：
	// 1.分层：利用BFS搜索方式给每个节点给予标记level[i]；
	// 2.判断分层是否成功即汇点是否被分到级别level[sink]!=0；
	// 3.在分层的基础上即level[i]上寻找所有的增广路、累计流量，回到步骤1；
	// No-Recursive
	public static int Dinic(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		// 如果BFS为FALSE 说明已经没有到T的路径了
		while (BFS4Dinic(S, T)) {
			int[] nextV = new int[vertexCnt + 1];// 保存U的下一个对应端点V,不作重复遍历V
			for (int i = 0; i <= vertexCnt; i++) { // nextV里初始化是第一个节点哈 FOR循环
				nextV[i] = 1; // DFS中，如果需要回溯，就回溯到nextV中的节点。
			}
			father = new int[vertexCnt + 1];
			minCapacity = new int[vertexCnt + 1];// 每次寻找增广路径都将每个点的流入容量置为0
			minCapacity[S] = MAX;// 源点的容量置为正无穷
			queue.offer(S);// 将源点加入队列
			// 当队列为空时，说明已经回到S了 可以重新BFS了
			while (!queue.isEmpty()) {
				u = queue.peek();
				for (v = nextV[u]; v <= vertexCnt; v++) {
					if ((capacity[u][v] > 0) && (level[u] + 1 == level[v])) {
						father[v] = u;
						queue.push(v);
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
						nextV[u] = v + 1;
						break;
					}
				}
				// 如果这个新搜到的点是T
				if (v == T) {
					for (int i = T; i != S; i = father[i]) {
						capacity[father[i]][i] -= minCapacity[T];// 正向更新
						capacity[i][father[i]] += minCapacity[T];// 反向更新
						flow[father[i]][i] += minCapacity[T]; // 更新FLOW表
						if (capacity[father[i]][i] == 0) {
							// 把stack弹到前一个饱和压入的点 因为他没能力继续压入了 后面的PRUNE
							//设成u=S也行 但这样是 剪枝  阻塞优化＝＝多路增广 连续增广 
							//设成u=S时 按理说应该重置minCapacity数组,但不重置也实际没问题 想想为什么
							//不用minCapacity用一个aug变量更安全
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// 如果Maxflow==0 可以认为没有流
					maxFlow += minCapacity[T];// 更新最大流
				}
				// 说明U这点的子孙遍历完了 可以设置为BLACK
				// 其实不设也没有关系 但避免重复
				else if (v > vertexCnt) {
					level[u] = MAX;
					queue.poll();
				}
			}

		}
		return maxFlow;
	}

	// Recursive
	public static int Dinic_Recurisive(int S, int T)
	{
		maxFlow = 0;// 最大流初始化
		// 如果BFS为FALSE 说明已经没有到T的路径了
		while (BFS4Dinic(S, T)) {
			maxFlow += outFlow(S, MAX, T);
		}
		return maxFlow;
	}

	public static int outFlow(int u, int beforeMin, int T)
	{
		int out = 0, branchFlow;
		// u是汇点，或者不可达了 则返回
		if (u == T || beforeMin == 0) {
			return beforeMin;
		}

		for (int v = 1; v <= vertexCnt; v++) {
			if ((capacity[u][v] > 0) && (level[u] + 1 == level[v])) {
				branchFlow = outFlow(v, Math.min(beforeMin, capacity[u][v]), T);
				father[v] = u;
				capacity[father[v]][v] -= branchFlow;// 正向更新
				capacity[v][father[v]] += branchFlow;
				flow[father[v]][v] += branchFlow; // 更新FLOW表
				out += branchFlow;
				beforeMin -= branchFlow;
				// 前面压入的已经没了 所以不能再压出去了
				if (beforeMin == 0) {
					break;
				}
			}
		}
		// 说明U这点的子孙遍历完了 可以设置为BLACK
		level[u] = MAX;
		return out;
	}

	private static void buildCap()
	{
		vertexCnt = 4;
		capacity = new int[vertexCnt + 1][vertexCnt + 1];
		flow = new int[vertexCnt + 1][vertexCnt + 1];
		minCapacity = new int[vertexCnt + 1];
		father = new int[vertexCnt + 1];
		visit = new boolean[vertexCnt + 1];
		level = new int[vertexCnt + 1];
		gap = new int[vertexCnt + 2];
		nextV = new int[vertexCnt + 1];
		addCap(1, 2, 40);
		addCap(1, 4, 20);
		addCap(2, 3, 30);
		addCap(2, 4, 20);
		addCap(3, 4, 10);
	}

	private static void addCap(int u, int v, int cap)
	{
		capacity[u][v] = cap;
	}

	public static int Dinic2(int S, int T)
	{
		int[] cur = new int[vertexCnt + 1];
		int flow = 0;
		int i, u, flag, v, ag, k;
		while (BFS4Dinic(S, T)) {
			for (i = 0; i <= vertexCnt; i++) { // cur里初始化是第一个节点哈
				cur[i] = 0; // DFS中，如果需要回溯，就回溯到cur中的节点。
				minCapacity[i] = MAX; // a里面存的是剩余流量
			}
			u = S;
			while (true) {
				flag = 0;
				for (v = cur[u]; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && level[u] + 1 == level[v]) {
						flag = 1;
						break;
					}
				}
				if (flag == 1) {
					cur[u] = v + 1;
					father[v] = u;
					minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					u = v; // 从找到的节点后开始在层次图里找路
					if (u == T) // 找到t后，增广
					{
						ag = minCapacity[T];
						flow += ag;
						for (v = T; v != S; v = father[v]) {
							cur[father[v]] = v; // 退回上一步。。 感觉这个多了
							capacity[father[v]][v] -= ag;
							capacity[v][father[v]] += ag;
							minCapacity[v] -= ag;
							if (capacity[father[v]][v] == 0) {
								u = father[v];
							}
						}
					}
				}
				else if (u != S) // 如果u ！= s 那么这条路走不通的话，从u的上一个节点继续找。
				{
					level[u] = MAX; // 相当于从层次图里删除这个节点。
					u = father[u];
				}
				else {
					// 如果从源点找不到增广路，就结束咧。
					break;
				}
			}
		}
		return flow;
	}

	// Improved Shortest Augmenting Path
	// 1. Gap 优化 2. 主动标号技术
	// ISAP和DINIC 原理都是一样的 都借鉴了Preflow里的标号 ISAP是动态改变标号＝＝主动标号，DINIC是调用BFS来重建标号＝＝被动标号
	// ISAP里还加入了GAP 优化，和DINIC里都有阻塞优化 详细可见下面的描述
	public static int ISAP(int S, int T)
	{
		maxFlow = 0;
		for (int i = 0; i <= vertexCnt; i++) {
			level[i] = gap[i] = 0;
			nextV[i] = 1;
		}
		int u = S, v;
		gap[0] = vertexCnt;
		minCapacity[S] = MAX;
		while (level[S] <= vertexCnt) {
			for (v = nextV[u]; v <= vertexCnt; v++) {
				if ((capacity[u][v] > 0) && (level[v] + 1 == level[u])) {
					father[v] = u;
					minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					nextV[u] = v;
					// u=v 与DINIC的区别所在 因为是它自己建BFS生成的标号树，超过了VERTEXCNT后
					// 他自己循环重建而DINIC是出去重建BFS
					u = v;
					break;
				}
			}
			// 如果这个新搜到的点是T，找到了增广路径
			if (v == T) {
				for (int i = T; i != S; i = father[i]) {
					capacity[father[i]][i] -= minCapacity[T];// 正向更新
					capacity[i][father[i]] += minCapacity[T];// 反向更新
					flow[father[i]][i] += minCapacity[T]; // 更新FLOW表
				}
				maxFlow += minCapacity[T];// 更新最大流
				//设成u=S也行 但这样是 剪枝  阻塞优化＝＝多路增广 连续增广 
				//设成u=S时 按理说应该重置minCapacity数组,但不重置也实际没问题 想想为什么
				//不用minCapacity用一个aug变量更安全
				if (capacity[father[v]][v] == 0) {
					u = father[v];
				}
			}
			// U点没容许弧了，增大标号 dis 或者Level
			else if (v > vertexCnt) {
				int minLev = vertexCnt;
				for (v = 1; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && level[v] < minLev) {
						minLev = level[v];
					}
				}
				// Gap 优化，如果出现断层，直接中断
				if ((--gap[level[u]]) == 0) {
					break;
				}
				gap[level[u] = minLev + 1]++;
				// U点重新遍历
				nextV[u] = 1;
				// 距离标号改变后，从父结点重新搜索，S的例外
				if (u != S) {
					u = father[u];
				}
			}

		}
		return maxFlow;
	}

	public static int ISAP2(int s, int t)
	{
		int v, maxflow = 0;
		int[] cur = new int[vertexCnt + 1];
		for (int i = 0; i <= vertexCnt; i++) {
			level[i] = gap[i] = 0;
			cur[i] = 1;
		}
		int u = s, aug = MAX;
		gap[0] = vertexCnt;

		while (level[s] <= vertexCnt) {
			for (v = cur[u]; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] + 1 == level[u]) {
					aug = Math.min(aug, capacity[u][v]);
					father[v] = u;
					cur[u] = v;
					u = v;
					if (v == t) {
						maxflow += aug;
						while (u != s) {
							capacity[father[u]][u] -= aug;
							capacity[u][father[u]] += aug;
							u = father[u];
						}
						father = new int[vertexCnt + 1];
						v = cur[u] - 1;
						continue;
					}
				}
			}
			// 不能找到U点的
			int mindis = vertexCnt;
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] < mindis) {
					mindis = level[v];
				}
			}
			if ((--gap[level[u]]) == 0) {
				break;
			}
			gap[level[u] = mindis + 1]++;
			cur[u] = 1;
			if (u != s) {
				u = father[u];
			}
		}
		return maxflow;
	}
}
/**
 * 网络流ISAP算法的简单介绍
 * 
 * http://blog.csdn.net/nomad2/article/details/7422527
 * 
 * 这几天由于种种原因经常接触到网络流的题目，这一类型的题给人的感觉，就是要非常使劲的YY才能出来点比较正常的模型。尤其是看了Amber最小割应用的文章，
 * 里面的题目思路真是充满了绵绵不绝的YD思想
 * 。然而比赛中，当你YD到了这一层后，您不得不花比较多的时间去纠结于大量细节的实现，而冗长的代码难免会使敲错版后的调试显得异常悲伤
 * ，因此一些巧妙简短高效的网络流算法在此时便显得犹为重要了
 * 。本文力求以最简短的描述，对比较流行的网络流算法作一定的总结，并借之向读者强烈推荐一种效率与编程复杂度相适应的算法。
 * 
 * 　　众所周知，在网络流的世界里，存在2类截然不同的求解思想，就是比较著名的预流推进与增广路，两者都需要反向边的小技巧。
 * 
 * 　　其中预流推进的算法思想是以边为单元进行推流操作。具体流程如下:置初始点邻接边满流并用一次反向bfs对每个结点计算反向距离标号，
 * 定义除汇点外存量大于出量的结点为活动结点
 * ，每次对活动结点按允许边（u->v:d[u]=d[v]+1）进行推流操作，直到无法推流或者该点存量为0，若u点此时仍为活动结点
 * ，则进行重标号，使之等于原图中进行推操作后的邻接结点的最小标号
 * +1，并将u点入队。当队列为空时，算法结束，只有s点和t点存量非0，网络中各顶点无存量，无法找到增广路继续增广，则t点存量为最大流。
 * 
 * 　　而增广路的思想在于每次从源点搜索出一条前往汇点的增广路，并改变路上的边权，直到无法再进行增广，此时汇点的增广量即为最大流。
 * 两者最后的理论基础依然是增广路定理
 * ，而在理论复杂度上预流推进要显得比较优秀。其中的HLPP高标预流推进的理论复杂度已经达到了另人发指的O（sqrt(m)*n
 * *n），但是其编程复杂度也是同样的令人发指- -
 * 
 * 　　于是我们能否在编程复杂度和算法复杂度上找到一个平衡呢，答案是肯定的。我们使用增广路的思想，而且必须进行优化。因为原始的增广路算法（例如EK）
 * 是非常悲剧的。于是有人注意到了预流推进中的标号法，在增广路算法中引入允许弧概念，每次反搜残留网络得到结点标号，在正向增广中利用递归进行连续增广，
 * 于是产生了基于分层图的Dinic算法
 * 。一些人更不满足于常规Dinic所带来的提升，进而加入了多路分流增广的概念，即对同一顶点的流量，分多路同时推进，再加上比较复杂的手工递归
 * ，使得Dinic已经满足大部分题目的需要。
 * 
 * 　　然而这样做就是增广路算法优化的极限么？答案永远是不。人们在Dinic中只类比了预流推进的标号技术，而重标号操作却没有发挥得淋漓尽致。
 * 于是人们在Dinic的基础上重新引入了重标号的概念
 * ，使得算法无须在每次增广后再进行BFS每个顶点进行距离标号，这种主动标号技术使得修正后算法的速度有了不少提高
 * 。但这点提高是不足称道的，人们又发现当某个标号的值没有对应的顶点后
 * ，即增广路被截断了，于是算法便可以提前结束，这种启发式的优化称为Gap优化。最后人们结合了连续增广
 * ，分层图，多路增广，Gap优化，主动标号等穷凶极恶的优化，更甚者在此之上狂搞个手动递归，于是产生了增广路算法的高效算法–ISAP算法。
 * 
 * 　　虽然ISAP算法的理论复杂度仍然不可超越高标预流推进，但其编程复杂度已经简化到发指，如此优化，加上不逊于Dinic的速率（
 * 在效率上手工Dinic有时甚至不如递归ISAP），我们没有不选择它的理由。
 */
