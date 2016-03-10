import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 * TODO: Rename to BPlusTree
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;
        
	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		// Search from root
        return tree_search(root, key);
    }

    public T tree_search(Node<K,T> startNode, K key) {
        // If the starting node is a leafNode
        if (startNode.isLeafNode) {
        	// Get the position of the key in the list of keys

        	int position = startNode.keys.indexOf(key);
        	// Get its value from the list of values.
        	return (T)((LeafNode)startNode).values.get(position);  // Corrected 3/9/16
        }
        
        // If not, find the right subtree to start the search.
        else {
        	// If the key is smaller than all of the keys in the current node, start searching from the leftmost child.
        	if (key.compareTo(startNode.keys.get(0)) < 0) {
        		return tree_search((Node<K,T>)((IndexNode)startNode).children.get(0), key);
        	}
        	// If the key is greater then all of the keys in the current node, start searching from the rightmost child.
        	else if (key.compareTo(startNode.keys.get(startNode.keys.size() - 1)) >= 0) {
        		return tree_search((Node<K,T>)((IndexNode)startNode).children.get(((IndexNode)startNode).children.size() - 1), key);
        	}
        	// Else, find the index i in the list of keys such that K(i) <= key < K(i+1), then search from children(i+1).
        	else {
        		ListIterator<K> iterator = startNode.keys.listIterator();
        		while (iterator.hasNext()) {
        			if (iterator.next().compareTo(key) > 0) {
        				int position = iterator.previousIndex() + 1;
        				return tree_search((Node<K,T>)((IndexNode)startNode).children.get(position), key);
        			}
        		return null;
        		}
        	}
        }
        return null;
    }

    /**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(K key, T value) {
		// If root does not exist, create a leaf node containing the key and the value.
		if (root == null) {
			root = new LeafNode(key, value);
		}
		// Insert from root using the recursive method
		tree_insert(root, key, value);
		return;
	}
	
	public Entry<K, Node<K,T>> tree_insert(Node<K,T> startNode, K key, T value) {
		// If we are trying to insert starting from an index node
		if (!startNode.isLeafNode) {
			
			// Find the index i in the list of keys such that K(i) <= key < K(i+1)
			ListIterator<K> iterator = startNode.keys.listIterator();
			int insert_position = -1;
			if (key.compareTo(startNode.keys.get(0)) < 0) 
				insert_position = 0;
			else if (key.compareTo(startNode.keys.get(startNode.keys.size() - 1)) >= 0) 
				insert_position = startNode.keys.size();
			else {
				while (iterator.hasNext()) {
					if (iterator.next().compareTo(key) > 0) {
						insert_position = iterator.previousIndex() + 1;
					}
				}	
				
				// Insert starting from that child node. 
				Entry<K, Node<K,T>> indexToInsert = tree_insert((Node<K,T>)((IndexNode)startNode).children.get(insert_position), key, value);
				// If there are no index node coming form the bottom to insert, then we're done.
				if (indexToInsert == null) 
					return null;
				// Else, we need to recursively insert the index node coming from the bottom to the current index node.
				else {
					K index_key = indexToInsert.getKey();
					// Find the position to insert the key and insert it.
					int index_insert_position;
					if (index_key.compareTo(startNode.keys.get(0)) < 0) 
						startNode.keys.add(0, index_key);
					else if (index_key.compareTo(startNode.keys.get(startNode.keys.size() - 1)) > 0)
						startNode.keys.add(index_key);
					else {
						ListIterator<K> key_iterator = startNode.keys.listIterator();
						while (key_iterator.hasNext()) {
							if (key_iterator.next().compareTo(index_key) > 0) 
								startNode.keys.add(key_iterator.previousIndex(), index_key);	
						}
					}
					// Now, check whether the starting index node has overflowed.
					if (!startNode.isOverflowed()) return null;
					else {
						// If it has overflowed, check whether the start node is the root node.
						if (startNode != root)
							// If not, just split the start node and return the index node to be pushed up.
							return splitIndexNode((IndexNode)startNode);
						else {
							// If it is the root node, split it and set the new index node to be the new root node.
							Entry<K, Node<K,T>> split_result = splitIndexNode((IndexNode)startNode);
							Node<K,T> right_child = split_result.getValue();
							K root_key = split_result.getKey();
							Node<K,T> new_root = new IndexNode(root_key, startNode, right_child);
							return null;
						}
					}
				}		
			}
			return null;
		}
		
		// If we are inserting directly into a leaf node
		else {
			((LeafNode)startNode).insertSorted(key, value);
			// Check if the node is overflowed. 
			if (startNode.isOverflowed()) 
				//If it is, split the leaf node and return the index node to push upwards. 
				return splitLeafNode((LeafNode)startNode);							
			// If the node is not overflowed, return null and we're done.
			else return null;
		}
	}
	
        

	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<splitingKey, RightNode>
	 * 
	 * @param leaf, any other relevant data
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {

		return null;
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index, any other relevant data
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {

		return null;
	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {
		return -1;

	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		return -1;
	}

}
