/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Waver
 */
public class Teststructure {
    

    public static void main(String[] args) {
    
        BPlusTree BT=new BPlusTree();
       Tests t1=new Tests();
       t1.testLargeTree();
       BT.insert(1, "2");
       BT.insert(2, "2");
       BT.insert(3, "2");
       BT.insert(4, "2");
       BT.insert(5, "2");
       BT.insert(6, "2");
      
       
        
    }    
}
