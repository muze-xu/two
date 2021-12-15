package com.test;

import java.util.*;

public class Main {
    private static class AcNode{
        //孩子节点用HashMap存储，能够在O(1)的时间内查找到，效率高
        Map<Character,AcNode> children=new HashMap<>();
        AcNode failNode;
        //使用set集合存储字符长度，防止敏感字符重复导致集合内数据重复
        Set<Integer> wordLengthList = new HashSet<>();
    }

    public static void insert(AcNode root,String s){
        AcNode temp=root;
        char[] chars=s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            //如果不包含这个字符就创建孩子节点
            if (!temp.children.containsKey(chars[i])){
                temp.children.put(chars[i],new AcNode());
            }
            //temp指向孩子节点
            temp=temp.children.get(chars[i]);
        }
        //一个字符串遍历完了后，将其长度保存到最后一个孩子节点信息中
        temp.wordLengthList.add(s.length());
    }

    public static void buildFailPath(AcNode root,int n,String[] s){
        //第一层的fail指针指向root,并且让第一层的节点入队，方便BFS
        Queue<AcNode> queue=new LinkedList<>();
        Map<Character,AcNode> childrens=root.children;
        Iterator iterator=childrens.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Character, AcNode> next = (Map.Entry<Character, AcNode>) iterator.next();
            queue.offer(next.getValue());
            next.getValue().failNode=root;
        }
        //构建剩余层数节点的fail指针,利用层次遍历
        while(!queue.isEmpty()){
            AcNode x=queue.poll();
            childrens=x.children; //取出当前节点的所有孩子
            iterator=childrens.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Character, AcNode> next = (Map.Entry<Character, AcNode>) iterator.next();
                AcNode y=next.getValue();  //得到当前某个孩子节点
                AcNode fafail=x.failNode;  //得到孩子节点的父节点的fail节点
                //如果 fafail节点没有与 当前节点父节点具有相同的转移路径，则继续获取 fafail 节点的失败指针指向的节点，将其赋值给 fafail
                while(fafail!=null&&(!fafail.children.containsKey(next.getKey()))){
                    fafail=fafail.failNode;
                }
                //回溯到了root节点，只有root节点的fail才为null
                if (fafail==null){
                    y.failNode=root;
                }
                else {
                    //fafail节点有与当前节点父节点具有相同的转移路径，则把当前孩子节点的fail指向fafail节点的孩子节点
                    y.failNode=fafail.children.get(next.getKey());
                }
                //如果当前节点的fail节点有保存字符串的长度信息，则把信息存储合并到当前节点
                if (y.failNode.wordLengthList!=null){
                    y.wordLengthList.addAll(y.failNode.wordLengthList);
                }
                queue.offer(y);//最后别忘了把当前孩子节点入队
            }
        }
    }



    public static void query(AcNode root, String s){
        AcNode temp=root;
        char[] c=s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            //如果这个字符在当前节点的孩子里面没有或者当前节点的fail指针不为空，就有可能通过fail指针找到这个字符
            //所以就一直向上更换temp节点
            while(temp.children.get(c[i])==null&&temp.failNode!=null){
                temp=temp.failNode;
            }
            //如果因为当前节点的孩子节点有这个字符，则将temp替换为下面的孩子节点
            if (temp.children.get(c[i])!=null){
                temp=temp.children.get(c[i]);
            }
            //如果temp的failnode为空，代表temp为root节点，没有在树中找到符合的敏感字，故跳出循环，检索下个字符
            else{
                continue;
            }
            //如果检索到当前节点的长度信息存在，则代表搜索到了敏感词，打印输出即可
            if (temp.wordLengthList.size()!=0){
                handleMatchWords(temp,s,i);
            }
        }
    }

    //利用节点存储的字符长度信息，打印输出敏感词及其在搜索串内的坐标
    public static void handleMatchWords(AcNode node, String word, int currentPos)
    {
        for (Integer wordLen : node.wordLengthList)
        {
            int startIndex = currentPos - wordLen + 1;
            String matchWord = word.substring(startIndex, currentPos + 1);
            System.out.println("匹配到的敏感词为:"+matchWord+",其在搜索串中下标为:"+startIndex+","+currentPos);
        }
    }

    private static AcNode root = new AcNode();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //输入样例个数
        int n = sc.nextInt();
        //输入M
        int M = sc.nextInt();
        //输入N
        int N = sc.nextInt();
        String[] str = new String[n];
        for (int i = 0; i < n; i++) {
            String word = sc.nextLine();
            insert(root,word);
            str[i]=word;
        }
        buildFailPath(root,0,str);
        int[][][][] bp = new int[N+1][M+1][][];
        for(int i=1;i<=N;i++){
            for(int j=1;j<=M;j++){
                if (N>1){
                    //向下
                }
                if (M>1){
                    //向右
                }
            }
        }
    }

}
