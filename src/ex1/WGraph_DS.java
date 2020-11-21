package ex1;

import java.util.*;

public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private HashMap<Integer, NodeInfo> myG2;//declaring a graph
    private int mc, Ec;//counter for changed

    //empty constructor
    public WGraph_DS() {
        myG2 = new HashMap<Integer, NodeInfo>();//creating the new graph
        this.mc = 0;
        this.Ec = 0;
    }

    @Override
    public node_info getNode(int key) {
        if (myG2.containsKey(key))
            return myG2.get(key);//returns the specific node from the map
        return null;
    }

    public HashMap<Integer, NodeInfo> getMAP() {
        return myG2;
    }

    public NodeInfo getNodeBig(int key) {
        if (myG2.containsKey(key))
            return myG2.get(key);//returns the specific node from the map
        return null;
    }

    @Override
    public Collection<node_info> getV() {
        return new LinkedList<node_info>(myG2.values());
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 != node2 && !myG2.isEmpty() && myG2.containsKey(node1) && myG2.containsKey(node2)) {
            if (myG2.get(node1).getMap().get(myG2.get(node2)) != null && myG2.get(node2).getMap().get(myG2.get(node1)) != null)
                return true;

        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            return myG2.get(node1).getMap().get(myG2.get(node2));
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if (!myG2.containsKey(key)) {
            NodeInfo n = new NodeInfo(key);
            //node_info n2 = new NodeInfo(key);
            myG2.put(key, n);
            mc++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (hasEdge(node1, node2) && w != getEdge(node1, node2) && w >= 0) {
            myG2.get(node1).getMap().put(myG2.get(node2), w);
            myG2.get(node2).getMap().put(myG2.get(node1), w);
            mc++;
        } else if (node1 != node2 && !hasEdge(node1, node2) && myG2.containsKey(node1) && myG2.containsKey(node2) && w >= 0) {
            myG2.get(node1).getMap().put(myG2.get(node2), w);
            myG2.get(node2).getMap().put(myG2.get(node1), w);
            mc++;
            Ec++;
        }
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        Collection<node_info> n = new ArrayList<>();
        for (NodeInfo i : myG2.get(node_id).getNi()) {
            n.add((node_info) i);
        }
        return n;
    }

    @Override
    public node_info removeNode(int key) {
        if (myG2.containsKey(key)) {
            for (NodeInfo i : myG2.get(key).getNi()) {
                //removeEdge(key,i.getKey());
                if (hasEdge(key, i.getKey())) {
                    myG2.get(key).getNi().remove(i.getKey());
                    myG2.get(i.getKey()).getNi().remove(key);
                    Ec--;//counter for the edges

                }
            }
            node_info n = new NodeInfo(key);
            myG2.remove(key);
            //myG.remove(key);
            mc++;
            return n;
        }
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (!myG2.isEmpty() && node1 != node2 && hasEdge(node1, node2)) {
            myG2.get(node1).removeNode(myG2.get(node2));
            myG2.get(node2).removeNode(myG2.get(node1));
            Ec--;//counter for the edges
            mc++;
        }
    }

    @Override
    public int nodeSize() {
        if (myG2 != null && !myG2.isEmpty()) {
            return myG2.size();
        }
        return 0;
    }

    @Override
    public int edgeSize() {
        return Ec;
    }

    @Override
    public int getMC() {
        return mc;
    }


    public class NodeInfo implements node_info {
        public HashMap<NodeInfo, Double> map;
        private int key;
        private double tag;
        private double dis;
        private String info;

        public NodeInfo() {
            //empty constructor
            map = new HashMap<NodeInfo, Double>();//neighbors list
            this.key = key++;
            this.tag = 0;
            this.info = " ";
            this.dis = Double.MAX_VALUE;
        }
        public NodeInfo(node_info n) {
            this.key=n.getKey();
            this.tag=n.getTag();
            this.info=n.getInfo();

        }
        public NodeInfo(int key) {
            //empty constructor
            map = new HashMap<NodeInfo, Double>();//neighbors list
            this.key = key;
            this.tag = 0;
            this.info = " ";
            this.dis = Double.MAX_VALUE;
        }


        public double getDis() {
            return this.dis;
        }

        public void setDis(double d) {
            this.dis = d;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;

        }

        public HashMap<NodeInfo, Double> getMap() {
            return this.map;
        }

        public void addNi(NodeInfo t, double we) {
            if (t != null || this != t || !myG2.containsKey(t.getKey()))
                map.put(t, we);
        }

        public Collection<NodeInfo> getNi() {
            return map.keySet();
        }

        public void removeNode(NodeInfo node) {
            if (null == node)
                return;
            map.remove(node);
            mc++;
        }

//        public boolean equals(Object g) {
//            weighted_graph gr = (WGraph_DS) g;
//            if (gr == myG2)
//                return true;
//            boolean b = true;
//            for (node_info i : myG2.values()) {
//                for (node_info j : myG2.values()) {
//                    if (gr.getV().contains(i) && gr.getV(i.getKey()).contains(j)
//                            && getEdge(i.getKey(), j.getKey()) == gr.getEdge(i.getKey(), j.getKey())) {
//                        b &= true;
//                    } else {
//                        b &= false;
//                        break;
//                    }
//                }
//            }
//            return b;
//        }
    }
    public boolean equals(WGraph_Algo sec) {
        WGraph_DS second=( WGraph_DS)sec.getGraph();
        WGraph_DS first=( WGraph_DS)sec.getGraph();
        boolean yah=true;
        for(node_info i: first.getV()) {
            for(node_info j: first.getV(i.getKey()) ) {
                yah&=second.hasEdge(i.getKey(), j.getKey());
            }
        }
        return yah;
    }
}