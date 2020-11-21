package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms,java.io.Serializable {
    private WGraph_DS graph;//declaring a new graph
    private Queue<node_info> myQ;

    public WGraph_Algo() {//empty constructor
        graph = new WGraph_DS();//creating actual new graph
    }


    @Override
    public void init(weighted_graph g) {
        this.graph = (WGraph_DS) g;
    }

    public WGraph_Algo(WGraph_DS g) {//constructor
        this.graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public weighted_graph copy() {
//        graph = new WGraph_DS((WGraph_DS)graph);
        return graph;
    }

    @Override
    public boolean isConnected() {
        if ((graph.nodeSize() == 0) || (graph.nodeSize() == 1)) return true;
        myQ = new LinkedList<node_info>();
        for (node_info t : graph.getV())
            t.setTag(0);//reset all the nodes as '0'
        for (node_info h : graph.getV()) {
            myQ.add(h);//pushing the first element
            h.setTag(1.0);
            break;//now i the first node to start with , never mind which one
        }
        node_info keep;
        while (!myQ.isEmpty()) {
            keep = myQ.poll();
            for (node_info r : graph.getV(keep.getKey())) {
                if (r.getTag() == 0) {
                    r.setTag(1.0);
                    myQ.add(r);
                }
            }
            if (myQ.peek() == null) break;
        }
        boolean b = true;
        for (node_info t : graph.getV())
            b &= t.getTag() == 1;
        return b;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph == null || graph.nodeSize() == 1 || graph.getNode(src) == null || graph.getNode(dest) == null)
            return -1;
        List<node_info> l=new LinkedList<>();
       l= shortestPath(src,dest);
      int x= l.get(l.size()-1).getKey();
      return graph.getNodeBig(x).getDis();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (graph == null || graph.nodeSize() == 1 || graph.getNode(src) == null || graph.getNode(dest) == null)
            return null;
        WGraph_DS.NodeInfo keep;
        HashMap<Integer, List<node_info>> shortWay = new HashMap<Integer, List<node_info>>();
        for (node_info i : graph.getV())
            i.setTag(0);//to know who we already visited in the future
        graph.getNode(src).setTag(1);//the other tracked nodes will be tagged in minDIS function
        graph.getNodeBig(src).setDis(0.0);//the distance from itself is 0
        List<node_info> l = new LinkedList<>();
        l.add(graph.getNode(src));
        shortWay.put(src, l);
        Stack<node_info> st = new Stack<node_info>();
        PriorityQueue<WGraph_DS.NodeInfo> pQ = new PriorityQueue<>(new Comparator<WGraph_DS.NodeInfo>() {
            @Override
            public int compare(WGraph_DS.NodeInfo a, WGraph_DS.NodeInfo b) {
                return -Double.compare(b.getDis(),a.getDis());
//                if (a.getDis() > b.getDis())
//                    return 1;
//                if (a.getDis() == b.getDis())
//                    return 0;
//                else
//                    return -1;
            }  });
        pQ.add(graph.getNodeBig(src));
        while(!pQ.isEmpty())
            {
                keep= pQ.poll();
                if(graph.getNodeBig(keep.getKey()).getNi()!=null){
                for (WGraph_DS.NodeInfo j : graph.getNodeBig(keep.getKey()).getNi()) {
                    if(graph.getNodeBig(j.getKey()).getTag()==0){
                   if(graph.getEdge(j.getKey(),keep.getKey())+keep.getDis()<j.getDis()){
                       j.setDis(graph.getEdge(j.getKey(),keep.getKey())+keep.getDis());//update the smallest distance
                      List<node_info> s=new LinkedList<node_info>();
                      //node_info keep2=keep;
                      Iterator<node_info> it=shortWay.get((keep.getKey())).iterator();
                      while(it.hasNext()){
                          s.add(it.next());
                      }
                         //deep copy for the list
                       s.add(j);
                      if(pQ.contains(j)){
                          pQ.remove(j);
                      }
                       pQ.add(j);
                       shortWay.put(j.getKey(),s);
                    }
                    if (keep.getKey() == graph.getNode(dest).getKey()) {
                        pQ.clear();//to end the while loop
                        break;//to end the for loop
                    }}}}
                keep.setTag(1);

            }
        if(!shortWay.containsKey(dest))
            return null;
        return shortWay.get(dest);
        }

    public boolean equals(Object g) {
        weighted_graph gr = (WGraph_DS) g;
        if (gr == graph)
            return true;
        boolean b = true;
        for (node_info i : graph.getV()) {
            for (node_info j : graph.getV(i.getKey())) {
                if (gr.getV().contains(i) && gr.getV(i.getKey()).contains(j)
                        && graph.getEdge(i.getKey(), j.getKey()) == gr.getEdge(i.getKey(), j.getKey())) {
                    b &= true;
                } else {
                    b &= false;
                    break;
                }
            }
        }
        return b;
    }

    @Override
    public boolean save(String file) {
        WGraph_Algo n=this;
        try {
            FileOutputStream fille = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fille);
            out.writeObject(n);
            out.close();
            fille.close();


        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean load(String file) {
        try{
            FileInputStream fille= new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fille);
            WGraph_Algo newGr = (WGraph_Algo) input.readObject();
            this.init(newGr.graph);
        }catch (IOException | ClassNotFoundException e){
            return false;

        }
        return true;
    }
    public boolean equals(WGraph_Algo sec) {
        WGraph_DS second=sec.graph;
        WGraph_DS first=this.graph;
        boolean yah=true;
        for(node_info i: first.getV()) {
            for(node_info j: first.getV(i.getKey()) ) {
                yah&=second.hasEdge(i.getKey(), j.getKey());
            }
        }
        return yah;
    }
}
